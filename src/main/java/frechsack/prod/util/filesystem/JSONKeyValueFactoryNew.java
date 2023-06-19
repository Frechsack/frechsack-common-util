package frechsack.prod.util.filesystem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JSONKeyValueFactoryNew {

    private record Token(int startInclusive, int endInclusive, boolean isArray, boolean isString,
                         boolean isNull, boolean isObject, boolean isNumber, boolean isBoolean){}

    public static JSONKeyValue parseJSON(String text){
        return parseObject(text, 0, text.length() - 1);
    }


    private static <Key> void parseToken(@NotNull final String text, @NotNull final Token token, @NotNull KeyValue<Key> keyValue, @Nullable Key key){
        if (token.isArray) {
            final var array = parseArray(text, token.startInclusive, token.endInclusive);
            if (key == null)
                keyValue.addArray(array);
            else
                keyValue.setArray(key, array);
        }
        else if (token.isString) {
            final var str = text.substring(token.startInclusive, token.endInclusive + 1);
            if (key == null)
                keyValue.addString(str);
            else
                keyValue.setString(key, str);
        }
        else if (token.isObject) {
            final var obj = parseObject(text, token.startInclusive, token.endInclusive);
            if (key == null)
                keyValue.addValues(obj);
            else
                keyValue.setValues(key, obj);
        }
        else if (token.isNull) {
            if (key == null)
                keyValue.addNull();
            else
                keyValue.setNull(key);
        }
        else if (token.isBoolean) {
            final var bool = Boolean.parseBoolean(text.substring(token.startInclusive, token.endInclusive + 1).toLowerCase());
            if (key == null)
                keyValue.addBoolean(bool);
            else
                keyValue.setBoolean(key, bool);
        }
        else if (token.isNumber){
            try {
                final var str = text.substring(token.startInclusive, token.endInclusive + 1);
                if (str.contains(".")) {
                    final var num = Double.parseDouble(str);
                    if (key == null)
                        keyValue.addDouble(num);
                    else
                        keyValue.setDouble(key, num);
                }
                else {
                    final var num = Long.parseLong(str);
                    if (key == null)
                        keyValue.addLong(num);
                    else
                        keyValue.setLong(key, num);
                }
            }
            catch (Exception e){
                throw e;
            }
        }
    }

    private static JSONKeyValue parseObject(final String text, int startInclusive, int endInclusive){
        assert text.charAt(startInclusive) == '{' && text.charAt(endInclusive) == '}';
        final var objectNode = new JSONKeyValue();
        for (int i = startInclusive + 1; i <= endInclusive; i++){
            final var keyToken = nextTokenWithin(text, i, endInclusive);
            if (!keyToken.isString())
                throw new IllegalStateException();
            final var key = text.substring(keyToken.startInclusive + 1, keyToken.endInclusive);
            final var separatorIndex = text.indexOf(':', keyToken.endInclusive + 1);
            final var valueToken = nextTokenWithin(text, separatorIndex + 1, endInclusive);
            parseToken(text, valueToken, objectNode, key);

            // End of token
            var nextCommaIndex = text.indexOf(',', valueToken.endInclusive + 1);
            if (nextCommaIndex > -1)
                i = nextCommaIndex;
        }
        return objectNode;
    }

    private static IndexedValue parseArray(final String text, int startInclusive, int endInclusive){
        assert text.charAt(startInclusive) == '[' && text.charAt(endInclusive) == ']';
        final var arrayNode = new IndexedValue();
        for (int startOfElement = startInclusive + 1; startOfElement <= endInclusive; startOfElement++){
            var currentCharacter = text.charAt(startOfElement);
            var arrayDepth = 0;
            var objectDepth = 0;
            var isStringEscaped = false;
            if (currentCharacter == ' ')
                continue;
            for (int endOfElement = startOfElement ; endOfElement <= endInclusive; endOfElement++){
                currentCharacter = text.charAt(endOfElement);
                if (currentCharacter == '"')
                    isStringEscaped = !isStringEscaped;
                else if (currentCharacter == '[' && !isStringEscaped)
                    arrayDepth++;
                else if (currentCharacter == '{' && !isStringEscaped)
                    objectDepth++;
                else if (currentCharacter == ']' && !isStringEscaped && endOfElement < endInclusive)
                    arrayDepth--;
                else if (currentCharacter == '}' && !isStringEscaped)
                    objectDepth--;

                if ((currentCharacter == ',' || (currentCharacter == ']' && endOfElement == endInclusive))
                        && !isStringEscaped && arrayDepth == 0 && objectDepth == 0){
                    final var elementToken = nextTokenWithin(text, startOfElement, endOfElement - 1);
                    parseToken(text, elementToken, arrayNode, null);
                    startOfElement = endOfElement;
                    break;
                }
            }
        }
        return arrayNode;
    }

    private static Token nextTokenWithin(final String text, int startInclusive, int endInclusive){
        for (int i = startInclusive; i <= endInclusive; i++){
            var currentCharacter = text.charAt(i);
            if (currentCharacter == ' ')
                continue;
            // Find Start of Token
            if (currentCharacter == '"') {
                // String token
                for (int y = i + 1; y <= endInclusive; y++) {
                    currentCharacter = text.charAt(y);
                    if (currentCharacter != '"')
                        continue;
                    if (text.charAt(y - 1) == '\\')
                        continue;
                    return new Token(i, y, false, true, false, false, false, false);
                }
                throw new IllegalArgumentException();
            }
            else if (Character.toLowerCase(currentCharacter) == 'f') {
                // Boolean token
                if (text.toLowerCase().indexOf("false", i) == i)
                    return new Token(i, i + 4, false, false, false, false, false, true);
                throw new IllegalArgumentException();
            }
            else if (Character.toLowerCase(currentCharacter) == 't'){
                // Boolean token
                if (text.toLowerCase().indexOf("true", i) == i)
                    return new Token(i, i + 3, false, false, false, false, false, true);
                throw new IllegalArgumentException();
            }
            else if (currentCharacter =='{'){
                // Object token
                var objectDepth = 0;
                for (int y = i + 1; y <= endInclusive; y++){
                    currentCharacter = text.charAt(y);
                    if (currentCharacter == '{')
                        objectDepth++;
                    else if (currentCharacter == '}') {
                        if (objectDepth == 0)
                            return new Token(i, y, false, false, false, true, false, false);
                        else
                            objectDepth--;
                    }
                }
                throw new IllegalArgumentException();
            }
            else if (currentCharacter == '[') {
                // Array token
                var arrayDepth = 0;
                for (int y = i + 1; y <= endInclusive; y++){
                    currentCharacter = text.charAt(y);
                    if (currentCharacter == '[')
                        arrayDepth++;
                    else if(currentCharacter == ']')
                        if (arrayDepth == 0)
                            return new Token(i, y, true, false, false, false, false, false);
                        else
                            arrayDepth--;
                }
                throw new IllegalArgumentException();
            }
            else if (Character.isDigit(currentCharacter)) {
                // Number token
                for (int y = i + 1; y <= endInclusive; y++){
                    currentCharacter = text.charAt(y);
                    if (!Character.isDigit(currentCharacter)  && currentCharacter != '.')
                        return new Token(i, y - 1, false, false, false, false, true, false);
                }
                throw new IllegalArgumentException();
            }
            else if(currentCharacter == 'n') {
                // Null token
                if (text.toLowerCase().indexOf("null", i) == i)
                    return new Token(i, i + 3, false, false, true, false, false, false);
                throw new IllegalArgumentException();
            }
        }
        throw new IllegalArgumentException();
    }
}
