package frechsack.prod.util.filesystem;

import java.util.stream.Stream;

public class JSONKeyValueFactory {


    private record Token(int startInclusive, int endInclusive, boolean isArray, boolean isString, boolean isNull, boolean isObject, boolean isNumber, boolean isBoolean){}

    public static KeyValue<String> parseObject(String text){
        text = text.trim();
        if (!text.startsWith("{") || !text.endsWith("}"))
            throw new IllegalStateException();

        JSONKeyValue rootNode = new JSONKeyValue();

        for (int i = 1; i < text.length(); i++){
            var nextToken = findNextToken(text, i, text.length() - 1);
            if (!nextToken.isString())
                throw new IllegalStateException();
            final var key = text.substring(nextToken.startInclusive + 1, nextToken.endInclusive + 1 - 1);
            var separatorIndex = text.indexOf(':', nextToken.endInclusive + 1);
            nextToken = findNextToken(text, separatorIndex + 1, text.length());
            if (nextToken.isArray())
                rootNode.setArray(key, parseArray(nextToken, text));
            else if (nextToken.isNull())
                rootNode.setNull(key);
            else if (nextToken.isString())
                rootNode.setString(key, text.substring(nextToken.startInclusive + 1, nextToken.endInclusive + 1 - 1));
            else {
                final var tokenText = text.substring(nextToken.startInclusive, nextToken.endInclusive + 1);
                if (nextToken.isObject())
                    rootNode.setValues(key, parseObject(tokenText));
                else if (nextToken.isNumber()) {
                    try {
                        final var possibleNumber = tokenText
                                .replaceAll(" ", "")
                                .trim();
                        if (possibleNumber.contains(".")){
                            rootNode.setDouble(key, Double.parseDouble(possibleNumber));
                        }
                        else {
                            rootNode.setLong(key, Long.parseLong(possibleNumber));
                        }
                    }
                    catch (Exception e){
                        throw e;
                    }
                }
                else if(nextToken.isBoolean()){
                    final var possibleBoolean = tokenText
                            .replaceAll(" ", "")
                            .trim()
                            .toLowerCase();
                    if ("true".equals(possibleBoolean))
                        rootNode.setBoolean(key, true);
                    else if ("false".equals(possibleBoolean))
                        rootNode.setBoolean(key, false);
                    else
                        throw new IllegalArgumentException();
                }
            }
            separatorIndex = text.indexOf(',', nextToken.endInclusive + 1);
            if (separatorIndex == -1)
                separatorIndex = text.indexOf('}',nextToken.endInclusive + 1);
            // i will be incremented the next loop
            i = separatorIndex;
        }
        return rootNode;
    }

    private static KeyValue<Integer> parseArray(Token token, String text){
        if (text.charAt(token.startInclusive) != '[' || text.charAt(token.endInclusive) != ']')
            throw new IllegalStateException();

        final var arrayNode = new IndexedValue();
        var isStringEscaped = false;
        var arrayDepth = 0;
        main: for (int i = token.startInclusive+1; i <= token.endInclusive; i++){
            var charToCheck = text.charAt(i);
            if (charToCheck == ' ')
                continue;
            for (int y = i ; y <= token.endInclusive; y++) {
                charToCheck = text.charAt(y);
                if (charToCheck == '"')
                    isStringEscaped = !isStringEscaped;
                else if (charToCheck == '[' && !isStringEscaped)
                    arrayDepth++;
                if (charToCheck == ']' && !isStringEscaped){
                    // Special: Closing Bracket should not decrease depth.
                    if (token.endInclusive != y)
                        arrayDepth--;
                }
                if ((charToCheck == ',' || charToCheck == ']') && !isStringEscaped && arrayDepth == 0) {
                    final var elementToken = findNextToken(text, i, y );
                    if (elementToken.isArray())
                        arrayNode.addArray(parseArray(elementToken, text));
                    if (elementToken.isNull())
                        arrayNode.addNull();
                    else {
                        final var tokenText = text.substring(elementToken.startInclusive, elementToken.endInclusive + 1);
                        if (elementToken.isObject())
                            arrayNode.addValues(parseObject(tokenText));
                        else if (elementToken.isString())
                            arrayNode.addString(tokenText);
                        else if (elementToken.isNumber()) {
                            final var possibleNumber = tokenText
                                .replaceAll(" ", "")
                                .trim();
                            try {
                                if (possibleNumber.contains("."))
                                    arrayNode.addDouble(Double.parseDouble(possibleNumber));
                                else
                                    arrayNode.addLong(Long.parseLong(possibleNumber));
                            }
                            catch (Exception e) {
                                throw new NumberFormatException(String.format("'%s' is not a valid number", possibleNumber));
                            }
                        }
                        else if (elementToken.isBoolean()) {
                            final var possibleBoolean = tokenText
                                    .replaceAll(" ", "")
                                    .trim()
                                    .toLowerCase();
                            if ("true".equals(possibleBoolean))
                                arrayNode.addBoolean(true);
                            else if ("false".equals(possibleBoolean))
                                arrayNode.addBoolean(false);
                            else
                                throw new IllegalArgumentException(String.format("'%s' can not be read as a boolean", possibleBoolean));
                        }
                    }

                    i = y;
                    continue main;
                }
            }
        }
        return arrayNode;
    }

    private static Token findNextToken(String text, int startInclusive, int endInclusive){
        for (int i = startInclusive; i <= endInclusive; i++){
            var charToCheck = text.charAt(i);
            if (charToCheck == ' ')
                continue;

            // String token
            if (charToCheck == '"'){
                for (int y = i + 1; y <= endInclusive; y++){
                    charToCheck = text.charAt(y);
                    if (charToCheck == '"' && text.charAt(y-1) != '\\'){
                        return new Token(i, y, false, true, false, false, false, false);
                    }
                }
                throw new IllegalStateException();
            }
            // Array token
            else if (charToCheck == '[') {
                var isStringEscaped = false;
                var arrayDepth = 0;
                for (int y = i + 1; y <= endInclusive; y++){
                    charToCheck = text.charAt(y);
                    if (charToCheck == '"')
                        isStringEscaped = !isStringEscaped;
                    else if(charToCheck == '[' && !isStringEscaped)
                        arrayDepth++;
                    else if (charToCheck == ']' && !isStringEscaped){
                        if (arrayDepth == 0) {
                            return new Token(i, y, true, false, false, false, false, false);
                        }
                        else
                            arrayDepth--;
                    }
                }
                throw new IllegalStateException();
            }
            // Null token || TODO: Range Check
            else if (charToCheck == 'n' && text.indexOf("null",i) == i){
                return new Token(i, i + 3, false, false, true, false, false, false);
            }
            // Object token
            else if (charToCheck == '{'){
                var isStringEscaped = false;
                var objectDepth = 0;
                for (int y = i + 1; y <= endInclusive; y++){
                    charToCheck = text.charAt(y);
                    if (charToCheck == '"')
                        isStringEscaped = !isStringEscaped;
                    else if(charToCheck == '{' && !isStringEscaped)
                        objectDepth++;
                    else if (charToCheck == '}' && !isStringEscaped){
                        if (objectDepth == 0) {
                            return new Token(i, y, false, false, false, true, false, false);
                        }
                        else
                            objectDepth--;
                    }
                }
                throw new IllegalStateException();
            }
            // Number token
            else if (Character.isDigit(charToCheck)){
                var endOfNumberIndex = i;

                for (int y = i + 1; y <= endInclusive; y++){
                    charToCheck = text.charAt(y);
                    if (Character.isDigit(charToCheck) || charToCheck == '.')
                        endOfNumberIndex++;
                    else
                        break;

                }
                return new Token(i, endOfNumberIndex, false, false, false, false, true, false);
            }
            // Boolean token
            else if ((charToCheck == 't' || charToCheck == 'T') && text.toLowerCase().indexOf("true",i) == i){
                return new Token(i, i + 3, false, false, false, false, false, true);
            }
            // Boolean token
            else if ((charToCheck == 'f' || charToCheck == 'F') && text.toLowerCase().indexOf("false",i) == i){
                return new Token(i, i + 4, false, false, false, false, false, true);
            }
        }
        System.out.println(text.substring(startInclusive, endInclusive + 1));
        throw new IllegalStateException("No token found");
    }

}
