package frechsack.prod.util.filesystem;

public class JSONKeyValueFactory {

    static JSONKeyValue parseObject(String text){
        return parseObject(text, 0, text.length() - 1);
    }

    public static void main(String[] args) {
        var obj = "{ \"key\": \"value\", \"key2\": false, \"key3\": { \"key5\": null  }, \"key3\": [ 13.2, 19, \"HelloWorld\" ] }";
        JSONKeyValue o = parseObject(obj);
        System.out.println(o.toString());
    }


    private static JSONKeyValue parseObject(String text, int startInclusive, int endInclusive){
        System.out.println("ParseObject:"+text.substring(startInclusive, endInclusive + 1));
        assert text.charAt(startInclusive) == '{' && text.charAt(endInclusive) == '}';
        final var node = new JSONKeyValue();

        var isEntryParsingComplete = true;
        var isNextEntryExpected = false;
        mainLoop: for (int i = startInclusive + 1; i <= endInclusive; i++) {
            // Skip until start of key
            if (text.charAt(i) == '"') {
                isNextEntryExpected = false;
                isEntryParsingComplete = false;
                var keyStartIndex = i;
                var keyEndIndex = -1;
                for (i++; i < endInclusive; i++) {
                    if (text.charAt(i) == '"' && text.charAt(i - 1) != '\\') {
                        keyEndIndex = i;
                        break;
                    }
                }

                if (keyEndIndex == -1)
                    throw new IllegalStateException("EndOfKeyExpected");

                final var key = text.substring(keyStartIndex + 1, keyEndIndex);
                final var separatorIndex = text.indexOf(':', keyEndIndex);
                if (separatorIndex == -1)
                    throw new IllegalStateException("NoSeparator");

                for (i = separatorIndex + 1; i <= endInclusive; i++) {
                    if (text.charAt(i) == '[') {
                        final var elementStartIndex = i;
                        var elementEndIndex = -1;
                        var isStringEscaped = false;
                        var objectDepth = 0;
                        var arrayDepth = 0;
                        for (i++; i <= endInclusive; i++) {
                            if (text.charAt(i) == '"')
                                isStringEscaped = !isStringEscaped;
                            else if (text.charAt(i) == '[' && !isStringEscaped)
                                arrayDepth++;
                            else if (text.charAt(i) == '{' && !isStringEscaped)
                                objectDepth++;
                            else if (text.charAt(i) == '}' && !isStringEscaped)
                                objectDepth--;
                            else if (text.charAt(i) == ']' && !isStringEscaped)
                                if (arrayDepth == 0 && objectDepth == 0) {
                                    elementEndIndex = i;
                                    break;
                                }
                                else
                                    arrayDepth--;
                        }
                        if (elementEndIndex == -1)
                            throw new IllegalStateException("EndOfArrayNotFound");

                        node.setArray(key, parseArray(text, elementStartIndex, elementEndIndex));
                        isEntryParsingComplete = true;

                        var endObjectSeparatorIndex = text.indexOf('}', elementEndIndex + 1);
                        var endCommaSeparatorIndex = text.indexOf(',', elementEndIndex + 1);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new IllegalStateException("NoSeparationOrEndCharacter");

                        i = endSeparatorIndex;
                        continue mainLoop;
                    }
                    else if (text.charAt(i) == '{') {
                        final var elementStartIndex = i;
                        var elementEndIndex = -1;
                        var isStringEscaped = false;
                        var objectDepth = 0;
                        var arrayDepth = 0;
                        for (i++; i <= endInclusive; i++) {
                            if (text.charAt(i) == '"')
                                isStringEscaped = !isStringEscaped;
                            else if (text.charAt(i) == '[' && !isStringEscaped)
                                arrayDepth++;
                            else if (text.charAt(i) == '{' && !isStringEscaped)
                                objectDepth++;
                            else if (text.charAt(i) == ']' && !isStringEscaped)
                                arrayDepth--;
                            else if (text.charAt(i) == '}' && !isStringEscaped)
                                if (arrayDepth == 0 && objectDepth == 0) {
                                    elementEndIndex = i;
                                    break;
                                }
                                else
                                    objectDepth--;
                        }
                        if (elementEndIndex == -1)
                            throw new IllegalStateException("EndOfObjectNotFound");

                        node.setValues(key, parseObject(text, elementStartIndex, elementEndIndex));
                        isEntryParsingComplete = true;

                        var endObjectSeparatorIndex = text.indexOf('}', elementEndIndex + 1);
                        var endCommaSeparatorIndex = text.indexOf(',', elementEndIndex + 1);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new IllegalStateException("NoSeparationOrEndCharacter");

                        i = endSeparatorIndex;
                        continue mainLoop;
                    }
                    else if (text.charAt(i) == '"') {
                        final var elementStartIndex = i;
                        var elementEndIndex = -1;
                        for (i++; i <= endInclusive; i++) {
                            if (text.charAt(i) == '"' && text.charAt(i - 1) != '\\') {
                                elementEndIndex = i;
                                break;
                            }
                        }
                        if (elementEndIndex == -1)
                            throw new IllegalStateException("EndOfStringExpected");
                        node.setString(key, text.substring(elementStartIndex + 1, elementEndIndex));
                        isEntryParsingComplete = true;

                        var endObjectSeparatorIndex = text.indexOf('}', elementEndIndex + 1);
                        var endCommaSeparatorIndex = text.indexOf(',', elementEndIndex + 1);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new IllegalStateException("NoSeparationOrEndCharacter");

                        i = endSeparatorIndex;
                        continue mainLoop;
                    }
                    else if (Character.toLowerCase(text.charAt(i)) == 't') {
                        if (i + 3 > endInclusive)
                            throw new IllegalStateException("IllegalStartOfItem");
                        if (!(Character.toLowerCase(text.charAt(i + 1)) == 'r'))
                            throw new IllegalStateException("IllegalCharacter");
                        if (!(Character.toLowerCase(text.charAt(i + 2)) == 'u'))
                            throw new IllegalStateException("IllegalCharacter");
                        if (!(Character.toLowerCase(text.charAt(i + 3)) == 'e'))
                            throw new IllegalStateException("IllegalCharacter");
                        node.setBoolean(key, true);
                        isEntryParsingComplete = true;
                        var endObjectSeparatorIndex = text.indexOf('}', i + 4);
                        var endCommaSeparatorIndex = text.indexOf(',', i + 4);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new IllegalStateException("NoSeparationOrEndCharacter");

                        i = endSeparatorIndex;
                        continue mainLoop;
                    }
                    else if (Character.toLowerCase(text.charAt(i)) == 'n') {
                        if (i + 3 > endInclusive)
                            throw new IllegalStateException("IllegalStartOfItem");
                        if (!(Character.toLowerCase(text.charAt(i + 1)) == 'u'))
                            throw new IllegalStateException("IllegalCharacter");
                        if (!(Character.toLowerCase(text.charAt(i + 2)) == 'l'))
                            throw new IllegalStateException("IllegalCharacter");
                        if (!(Character.toLowerCase(text.charAt(i + 3)) == 'l'))
                            throw new IllegalStateException("IllegalCharacter");
                        node.setNull(key);
                        isEntryParsingComplete = true;
                        var endObjectSeparatorIndex = text.indexOf('}', i + 4);
                        var endCommaSeparatorIndex = text.indexOf(',', i + 4);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new IllegalStateException("NoSeparationOrEndCharacter");

                        i = endSeparatorIndex;
                        continue mainLoop;
                    }
                    else if (Character.toLowerCase(text.charAt(i)) == 'f') {
                        if (i + 4 > endInclusive)
                            throw new IllegalStateException("IllegalStartOfItem");
                        if (!(Character.toLowerCase(text.charAt(i + 1)) == 'a'))
                            throw new IllegalStateException("IllegalCharacter");
                        if (!(Character.toLowerCase(text.charAt(i + 2)) == 'l'))
                            throw new IllegalStateException("IllegalCharacter");
                        if (!(Character.toLowerCase(text.charAt(i + 3)) == 's'))
                            throw new IllegalStateException("IllegalCharacter");
                        if (!(Character.toLowerCase(text.charAt(i + 4)) == 'e'))
                            throw new IllegalStateException("IllegalCharacter");
                        node.setBoolean(key, false);
                        isEntryParsingComplete = true;
                        var endObjectSeparatorIndex = text.indexOf('}', i + 5);
                        var endCommaSeparatorIndex = text.indexOf(',', i + 5);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new IllegalStateException("NoSeparationOrEndCharacter");

                        i = endSeparatorIndex;
                        continue mainLoop;
                    }
                    else if (Character.isDigit(text.charAt(i))) {
                        final var elementStartIndex = i;
                        var elementEndIndex = -1;
                        for (i++; i <= endInclusive; i++) {
                            if (text.charAt(i) == ',' || text.charAt(i) == '}') {
                                elementEndIndex = i - 1;
                                break;
                            }
                            if (!(text.charAt(i) == '.' || Character.isDigit(text.charAt(i))))
                                throw new IllegalStateException("NonNumeric Character");
                        }
                        if (elementEndIndex == -1)
                            throw new IllegalStateException("Number not terminated");
                        final var possibleNumber = text.substring(elementStartIndex, elementEndIndex + 1);
                        try {
                            if (possibleNumber.indexOf('.') == -1)
                                node.setLong(key, Long.parseLong(possibleNumber));
                            else
                                node.setDouble(key, Double.parseDouble(possibleNumber));
                        }
                        catch (Exception e){
                            throw new IllegalStateException("Number not parseable");
                        }
                        isEntryParsingComplete = true;
                        var endObjectSeparatorIndex = text.indexOf('}', elementEndIndex + 1);
                        var endCommaSeparatorIndex = text.indexOf(',', elementEndIndex + 1);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new IllegalStateException("NoSeparationOrEndCharacter");

                        i = endSeparatorIndex;
                        continue mainLoop;

                    }
                }
            }
            else if (text.charAt(i) == '}') {
                if (i < endInclusive)
                    throw new IllegalStateException("IllegalCharacter");
            }
            else if (text.charAt(i) != ' ')
                throw new IllegalStateException("IllegalCharacter");
        }

        if (!isEntryParsingComplete)
            throw new IllegalStateException("ParsingIsNotComplete");
        if (isNextEntryExpected)
            throw new IllegalStateException("AnotherEntryExpected");
        return node;
    }

    private static IndexedValue parseArray(String text, int startInclusive, int endInclusive){
        System.out.println("ParseArray:"+text.substring(startInclusive, endInclusive + 1));
        assert text.charAt(startInclusive) == '[' && text.charAt(endInclusive) == ']';
        final var array = new IndexedValue();
        var isElementExpected = false;
        mainLoop: for (int i = startInclusive + 1; i <= endInclusive; i++) {
            if (text.charAt(i) == '{') {
                final var elementStartIndex = i;
                var arrayOpenCount = 0;
                var objectOpenCount = 0;
                var isStringEscaped = false;
                for (i++; i <= endInclusive; i++) {
                    if (text.charAt(i) == '"')
                        isStringEscaped = !isStringEscaped;
                    else if (text.charAt(i) == '[' && !isStringEscaped)
                        arrayOpenCount++;
                    else if (text.charAt(i) == ']' && !isStringEscaped)
                        if (arrayOpenCount == 0)
                            throw new IllegalStateException();
                        else
                            arrayOpenCount--;
                    else if (text.charAt(i) == '{' && !isStringEscaped)
                        objectOpenCount++;
                    else if (text.charAt(i) == '}' && !isStringEscaped)
                        if (objectOpenCount == 0) {
                            array.addValues(parseObject(text, elementStartIndex, i));
                            // Skip anything from here to the spliterator
                            for (i++; i <= endInclusive; i++) {
                                if (text.charAt(i) == ',') {
                                    isElementExpected = true;
                                    continue mainLoop;
                                }
                                else if (text.charAt(i) == ']' && i == endInclusive) {
                                    isElementExpected = false;
                                    continue mainLoop;
                                }
                                else if (text.charAt(i) != ' ')
                                    throw new IllegalStateException();
                            }
                            // No continue was reached
                            throw new IllegalStateException();
                        }
                        else
                            objectOpenCount--;
                }
            }
            else if (text.charAt(i) == '[') {
                final var elementStartIndex = i;
                var arrayOpenCount = 0;
                var objectOpenCount = 0;
                var isStringEscaped = false;
                for (i++; i <= endInclusive; i++) {
                    if (text.charAt(i) == '"')
                        isStringEscaped = !isStringEscaped;
                    else if (text.charAt(i) == '[' && !isStringEscaped)
                        arrayOpenCount++;
                    else if (text.charAt(i) == '}' && !isStringEscaped)
                        if (objectOpenCount == 0)
                            throw new IllegalStateException();
                        else
                            objectOpenCount--;
                    else if (text.charAt(i) == '{' && !isStringEscaped)
                        objectOpenCount++;
                    else if (text.charAt(i) == ']' && !isStringEscaped)
                        if (arrayOpenCount == 0) {
                            array.addArray(parseArray(text, elementStartIndex, i));
                            // Skip anything from here to the spliterator
                            for (i++; i <= endInclusive; i++) {
                                if (text.charAt(i) == ',') {
                                    isElementExpected = true;
                                    continue mainLoop;
                                }
                                else if (text.charAt(i) == ']' && i == endInclusive) {
                                    isElementExpected = false;
                                    continue mainLoop;
                                }
                                else if (text.charAt(i) != ' ')
                                    throw new IllegalStateException();
                            }
                            // No continue was reached
                            throw new IllegalStateException();
                        }
                        else
                            objectOpenCount--;
                }
            }
            else if (text.charAt(i) == ',')
                throw new IllegalStateException();
            else if (text.charAt(i) == '}')
                throw new IllegalStateException();
            else if (text.charAt(i) == ']' && i < endInclusive)
                throw new IllegalStateException();
            else if (text.charAt(i) == '"') {
                // String element
                final var elementStartIndex = i;
                for (i++; i <= endInclusive; i++) {
                    if (text.charAt(i) == '"' && text.charAt(i - 1) != '\\') {
                        array.addString(text.substring(elementStartIndex + 1, i));

                        // Skip anything from here to the spliterator
                        for (i++; i <= endInclusive; i++) {
                            if (text.charAt(i) == ',') {
                                isElementExpected = true;
                                continue mainLoop;
                            }
                            else if (text.charAt(i) == ']' && i == endInclusive) {
                                isElementExpected = false;
                                continue mainLoop;
                            }
                            else if (text.charAt(i) != ' ')
                                throw new IllegalStateException();
                        }
                        // No continue was reached
                        throw new IllegalStateException();
                    }
                }
                // No continue was reached
                throw new IllegalStateException();
            }
            else if (Character.toLowerCase(text.charAt(i)) == 't') {
                if (i + 3 > endInclusive || !(
                        Character.toLowerCase(text.charAt(i + 1)) == 'r'
                        && Character.toLowerCase(text.charAt(i + 2)) == 'u'
                        && Character.toLowerCase(text.charAt(i + 3)) == 'e'))
                    throw new IllegalStateException();
                array.addBoolean(true);
                // Skip anything from here to the spliterator
                for (i+=4; i <= endInclusive; i++) {
                    if (text.charAt(i) == ',') {
                        isElementExpected = true;
                        continue mainLoop;
                    }
                    else if (text.charAt(i) == ']' && i == endInclusive) {
                        isElementExpected = false;
                        continue mainLoop;
                    }
                    else if (text.charAt(i) != ' ')
                        throw new IllegalStateException();
                }
                // No continue was reached
                throw new IllegalStateException();
            }
            else if (Character.toLowerCase(text.charAt(i)) == 'f') {
                if (i + 4 > endInclusive || !(
                        Character.toLowerCase(text.charAt(i + 1)) == 'a'
                                && Character.toLowerCase(text.charAt(i + 2)) == 'l'
                                && Character.toLowerCase(text.charAt(i + 3)) == 's'
                                && Character.toLowerCase(text.charAt(i + 4)) == 'e'))
                    throw new IllegalStateException();
                array.addBoolean(false);
                // Skip anything from here to the spliterator
                for (i += 5; i <= endInclusive; i++) {
                    if (text.charAt(i) == ',') {
                        isElementExpected = true;
                        continue mainLoop;
                    }
                    else if (text.charAt(i) == ']' && i == endInclusive) {
                        isElementExpected = false;
                        continue mainLoop;
                    }
                    else if (text.charAt(i) != ' ')
                        throw new IllegalStateException();
                }
                // No continue was reached
                throw new IllegalStateException();
            }
            else if (Character.isDigit(text.charAt(i))) {
                final var elementStartIndex = i;
                for (i++; i <= endInclusive; i++) {
                    if (!Character.isDigit(text.charAt(i)) && text.charAt(i) != '.') {
                        try {
                            final var possibleNumber = text.substring(elementStartIndex, i);
                            if (possibleNumber.contains("."))
                                array.addDouble(Double.parseDouble(possibleNumber));
                            else
                                array.addLong(Long.parseLong(possibleNumber));
                        }
                        catch (Exception ignored){
                            throw new IllegalStateException();
                        }


                        // Skip anything from here to the spliterator
                        for (; i <= endInclusive; i++) {
                            if (text.charAt(i) == ',') {
                                isElementExpected = true;
                                continue mainLoop;
                            }
                            else if (text.charAt(i) == ']' && i == endInclusive) {
                                isElementExpected = false;
                                continue mainLoop;
                            }
                            else if (text.charAt(i) != ' ')
                                throw new IllegalStateException();
                        }
                        // No continue was reached
                        throw new IllegalStateException();
                    }
                }
                // No continue was reached
                throw new IllegalStateException();
            }
        }
        if (isElementExpected)
            throw new IllegalStateException();

        return array;
    }
}
