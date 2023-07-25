package frechsack.prod.util.filesystem;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

class JSONKeyValueFactory {

    static JSONKeyValue parseObject(String text, boolean isAsync){
        return parseObject(text, 0, text.length() - 1, isAsync).join();
    }

    private static CompletableFuture<JSONKeyValue> parseObject(String text, int startInclusive, int endInclusive, boolean isAsync){
        assert text.charAt(startInclusive) == '{' && text.charAt(endInclusive) == '}';
        final var computations = new ArrayList<CompletableFuture<?>>();
        final var node = new JSONKeyValue();
        final var safeNode = KeyValue.synchronizedKeyValue(node);
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
                    throw new JSONParseException("JSON key is not completed.", keyStartIndex);

                final var key = text.substring(keyStartIndex + 1, keyEndIndex);
                final var separatorIndex = text.indexOf(':', keyEndIndex);
                if (separatorIndex == -1)
                    throw new JSONParseException("JSON key is not separated from it´s value.", keyEndIndex);

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
                                } else
                                    arrayDepth--;
                        }
                        if (elementEndIndex == -1)
                            throw new JSONParseException("JSON array is not closed.", i);

                        if (isAsync)
                            computations.add(parseArray(text, elementStartIndex, elementEndIndex, true)
                                    .thenAccept(array -> safeNode.setArray(key, array)));
                        else
                            safeNode.setArray(key, parseArray(text, elementStartIndex, elementEndIndex, false).join());
                        isEntryParsingComplete = true;

                        var endObjectSeparatorIndex = text.indexOf('}', elementEndIndex + 1);
                        var endCommaSeparatorIndex = text.indexOf(',', elementEndIndex + 1);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new JSONParseException("JSON node is not closed or it´s elements are not separated.", i);

                        i = endSeparatorIndex;
                        continue mainLoop;
                    } else if (text.charAt(i) == '{') {
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
                                } else
                                    objectDepth--;
                        }
                        if (elementEndIndex == -1)
                            throw new JSONParseException("JSON node is not closed.", i);

                        if (isAsync)
                            computations.add(parseObject(text, elementStartIndex, elementEndIndex, true)
                                    .thenAccept(obj -> safeNode.setValues(key, obj)));
                        else
                            safeNode.setValues(key, parseObject(text, elementStartIndex, elementEndIndex, false).join());
                        isEntryParsingComplete = true;
                        var endObjectSeparatorIndex = text.indexOf('}', elementEndIndex + 1);
                        var endCommaSeparatorIndex = text.indexOf(',', elementEndIndex + 1);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new JSONParseException("JSON node is not closed or it´s elements are not separated.", i);

                        i = endSeparatorIndex;
                        continue mainLoop;
                    } else if (text.charAt(i) == '"') {
                        final var elementStartIndex = i;
                        var elementEndIndex = -1;
                        for (i++; i <= endInclusive; i++) {
                            if (text.charAt(i) == '"' && text.charAt(i - 1) != '\\') {
                                elementEndIndex = i;
                                break;
                            }
                        }
                        if (elementEndIndex == -1)
                            throw new JSONParseException("JSON string is not closed.", i);
                        safeNode.setString(key, text.substring(elementStartIndex + 1, elementEndIndex));
                        isEntryParsingComplete = true;

                        var endObjectSeparatorIndex = text.indexOf('}', elementEndIndex + 1);
                        var endCommaSeparatorIndex = text.indexOf(',', elementEndIndex + 1);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new JSONParseException("JSON node is not closed or it´s elements are not separated.", i);

                        i = endSeparatorIndex;
                        continue mainLoop;
                    } else if (Character.toLowerCase(text.charAt(i)) == 't') {
                        if (i + 3 > endInclusive)
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        if (!(Character.toLowerCase(text.charAt(i + 1)) == 'r'))
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        if (!(Character.toLowerCase(text.charAt(i + 2)) == 'u'))
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        if (!(Character.toLowerCase(text.charAt(i + 3)) == 'e'))
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        safeNode.setBoolean(key, true);
                        isEntryParsingComplete = true;
                        var endObjectSeparatorIndex = text.indexOf('}', i + 4);
                        var endCommaSeparatorIndex = text.indexOf(',', i + 4);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new JSONParseException("JSON node is not closed or it´s elements are not separated.", i);
                        i = endSeparatorIndex;
                        continue mainLoop;
                    } else if (Character.toLowerCase(text.charAt(i)) == 'n') {
                        if (i + 3 > endInclusive)
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        if (!(Character.toLowerCase(text.charAt(i + 1)) == 'u'))
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        if (!(Character.toLowerCase(text.charAt(i + 2)) == 'l'))
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        if (!(Character.toLowerCase(text.charAt(i + 3)) == 'l'))
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        safeNode.setNull(key);
                        isEntryParsingComplete = true;
                        var endObjectSeparatorIndex = text.indexOf('}', i + 4);
                        var endCommaSeparatorIndex = text.indexOf(',', i + 4);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }

                        if (endSeparatorIndex == -1)
                            throw new JSONParseException("JSON node is not closed or it´s elements are not separated.", i);
                        i = endSeparatorIndex;
                        continue mainLoop;
                    } else if (Character.toLowerCase(text.charAt(i)) == 'f') {
                        if (i + 4 > endInclusive)
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        if (!(Character.toLowerCase(text.charAt(i + 1)) == 'a'))
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        if (!(Character.toLowerCase(text.charAt(i + 2)) == 'l'))
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        if (!(Character.toLowerCase(text.charAt(i + 3)) == 's'))
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        if (!(Character.toLowerCase(text.charAt(i + 4)) == 'e'))
                            throw new JSONParseException("JSON boolean is not completed.", i);
                        safeNode.setBoolean(key, false);
                        isEntryParsingComplete = true;
                        var endObjectSeparatorIndex = text.indexOf('}', i + 5);
                        var endCommaSeparatorIndex = text.indexOf(',', i + 5);
                        var endSeparatorIndex = endObjectSeparatorIndex;
                        if (endCommaSeparatorIndex != -1 && endCommaSeparatorIndex < endObjectSeparatorIndex) {
                            endSeparatorIndex = endCommaSeparatorIndex;
                            isNextEntryExpected = true;
                        }
                        if (endSeparatorIndex == -1)
                            throw new JSONParseException("JSON node is not closed or it´s elements are not separated.", i);

                        i = endSeparatorIndex;
                        continue mainLoop;
                    } else if (Character.isDigit(text.charAt(i))) {
                        final var elementStartIndex = i;
                        var elementEndIndex = -1;
                        for (i++; i <= endInclusive; i++) {
                            if (text.charAt(i) == ',' || text.charAt(i) == '}') {
                                elementEndIndex = i - 1;
                                break;
                            }
                            if (!(text.charAt(i) == '.' || Character.isDigit(text.charAt(i))))
                                throw new JSONParseException("JSON number includes non numerical character.", i);
                        }
                        if (elementEndIndex == -1)
                            throw new JSONParseException("JSON number is not completed.", i);
                        final var possibleNumber = text.substring(elementStartIndex, elementEndIndex + 1);
                        try {
                            if (possibleNumber.indexOf('.') == -1) {
                                final var value = Long.parseLong(possibleNumber);
                                if (value <= (long) Integer.MAX_VALUE && value >= Integer.MIN_VALUE)
                                    safeNode.setInt(key, (int) value);
                                else
                                    safeNode.setLong(key, value);
                            } else
                                safeNode.setDouble(key, Double.parseDouble(possibleNumber));
                        } catch (Exception e) {
                            throw new JSONParseException("JSON number could not be parsed.", i);
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
                            throw new JSONParseException("JSON node is not closed or it´s elements are not separated.", i);

                        i = endSeparatorIndex;
                        continue mainLoop;

                    }
                }
            } else if (text.charAt(i) == '}') {
                if (i < endInclusive)
                    throw new JSONParseException("JSON node is closed.", i);
            } else if (text.charAt(i) != ' ') {
                throw new JSONParseException("JSON node contains illegal character.", i);
            }
        }

        if (!isEntryParsingComplete)
            throw new JSONParseException("JSON node is missing element for key.", endInclusive);
        if (isNextEntryExpected)
            throw new JSONParseException("JSON node expects another element.", endInclusive);
        return CompletableFuture.allOf(computations.toArray(CompletableFuture[]::new))
                .thenApply(__ -> node);
    }

    private static CompletableFuture<IndexedValue> parseArray(String text, int startInclusive, int endInclusive, boolean isAsync){
        assert text.charAt(startInclusive) == '[' && text.charAt(endInclusive) == ']';
        final var array = new IndexedValue();
        final var saveArray = KeyValue.synchronizedKeyValue(array);
        final var computations = new ArrayList<CompletableFuture<?>>();
        var isElementExpected = false;
        mainLoop:
        for (int i = startInclusive + 1; i <= endInclusive; i++) {
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
                            throw new JSONParseException("JSON array is closed but never opened.", i);
                        else
                            arrayOpenCount--;
                    else if (text.charAt(i) == '{' && !isStringEscaped)
                        objectOpenCount++;
                    else if (text.charAt(i) == '}' && !isStringEscaped)
                        if (objectOpenCount == 0) {
                            if (isAsync)
                                computations.add(parseObject(text, elementStartIndex, i, true)
                                        .thenAccept(saveArray::addValues));
                            else
                                saveArray.addValues(parseObject(text, elementStartIndex, i, false).join());

                            // Skip anything from here to the spliterator
                            for (i++; i <= endInclusive; i++) {
                                if (text.charAt(i) == ',') {
                                    isElementExpected = true;
                                    continue mainLoop;
                                } else if (text.charAt(i) == ']' && i == endInclusive) {
                                    isElementExpected = false;
                                    continue mainLoop;
                                } else if (text.charAt(i) != ' ')
                                    throw new JSONParseException("JSON node illegal character.", i);
                            }
                            // No continue was reached
                            throw new JSONParseException("JSON node no separator found.", i);
                        } else
                            objectOpenCount--;
                }
            } else if (text.charAt(i) == '[') {
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
                            throw new JSONParseException("JSON node is closed but never opened.", i);
                        else
                            objectOpenCount--;
                    else if (text.charAt(i) == '{' && !isStringEscaped)
                        objectOpenCount++;
                    else if (text.charAt(i) == ']' && !isStringEscaped)
                        if (arrayOpenCount == 0) {
                            if (isAsync)
                                computations.add(parseArray(text, elementStartIndex, i, true)
                                        .thenAccept(saveArray::addArray));
                            else
                                saveArray.addArray(parseArray(text, elementStartIndex, i, false).join());
                            // Skip anything from here to the spliterator
                            for (i++; i <= endInclusive; i++) {
                                if (text.charAt(i) == ',') {
                                    isElementExpected = true;
                                    continue mainLoop;
                                } else if (text.charAt(i) == ']' && i == endInclusive) {
                                    isElementExpected = false;
                                    continue mainLoop;
                                } else if (text.charAt(i) != ' ')
                                    throw new JSONParseException("JSON node illegal character.", i);
                            }
                            // No continue was reached
                            throw new JSONParseException("JSON array no separator found.", i);
                        } else
                            arrayOpenCount--;
                }
            } else if (text.charAt(i) == ',')
                throw new JSONParseException("JSON array illegal character.", i);
            else if (text.charAt(i) == '}')
                throw new JSONParseException("JSON array illegal character.", i);
            else if (text.charAt(i) == ']' && i < endInclusive)
                throw new JSONParseException("JSON array is closed.", i);
            else if (text.charAt(i) == '"') {
                // String element
                final var elementStartIndex = i;
                for (i++; i <= endInclusive; i++) {
                    if (text.charAt(i) == '"' && text.charAt(i - 1) != '\\') {
                        saveArray.addString(text.substring(elementStartIndex + 1, i));

                        // Skip anything from here to the spliterator
                        for (i++; i <= endInclusive; i++) {
                            if (text.charAt(i) == ',') {
                                isElementExpected = true;
                                continue mainLoop;
                            } else if (text.charAt(i) == ']' && i == endInclusive) {
                                isElementExpected = false;
                                continue mainLoop;
                            } else if (text.charAt(i) != ' ')
                                throw new JSONParseException("JSON illegal character.", i);
                        }
                        // No continue was reached
                        throw new JSONParseException("JSON string is not terminated.", i);
                    }
                }
                // No continue was reached
                throw new JSONParseException("JSON array is not closed or separated.", i);
            } else if (Character.toLowerCase(text.charAt(i)) == 't') {
                if (i + 3 > endInclusive || !(
                        Character.toLowerCase(text.charAt(i + 1)) == 'r'
                                && Character.toLowerCase(text.charAt(i + 2)) == 'u'
                                && Character.toLowerCase(text.charAt(i + 3)) == 'e'))
                    throw new JSONParseException("JSON boolean is not completed.", i);
                saveArray.addBoolean(true);
                // Skip anything from here to the spliterator
                for (i += 4; i <= endInclusive; i++) {
                    if (text.charAt(i) == ',') {
                        isElementExpected = true;
                        continue mainLoop;
                    } else if (text.charAt(i) == ']' && i == endInclusive) {
                        isElementExpected = false;
                        continue mainLoop;
                    } else if (text.charAt(i) != ' ')
                        throw new JSONParseException("JSON illegal character.", i);
                }
                // No continue was reached
                throw new JSONParseException("JSON boolean is not completed.", i);
            } else if (Character.toLowerCase(text.charAt(i)) == 'f') {
                if (i + 4 > endInclusive || !(
                        Character.toLowerCase(text.charAt(i + 1)) == 'a'
                                && Character.toLowerCase(text.charAt(i + 2)) == 'l'
                                && Character.toLowerCase(text.charAt(i + 3)) == 's'
                                && Character.toLowerCase(text.charAt(i + 4)) == 'e'))
                    throw new JSONParseException("JSON boolean is not completed.", i);
                saveArray.addBoolean(false);
                // Skip anything from here to the spliterator
                for (i += 5; i <= endInclusive; i++) {
                    if (text.charAt(i) == ',') {
                        isElementExpected = true;
                        continue mainLoop;
                    } else if (text.charAt(i) == ']' && i == endInclusive) {
                        isElementExpected = false;
                        continue mainLoop;
                    } else if (text.charAt(i) != ' ')
                        throw new JSONParseException("JSON illegal character.", i);
                }
                // No continue was reached
                throw new JSONParseException("JSON boolean is not completed.", i);
            } else if (Character.toLowerCase(text.charAt(i)) == 'n') {
                if (i + 3 > endInclusive || !(
                        Character.toLowerCase(text.charAt(i + 1)) == 'u'
                                && Character.toLowerCase(text.charAt(i + 2)) == 'l'
                                && Character.toLowerCase(text.charAt(i + 3)) == 'l'))
                    throw new JSONParseException("JSON null is not completed.", i);
                saveArray.addNull();
                // Skip anything from here to the spliterator
                for (i += 4; i <= endInclusive; i++) {
                    if (text.charAt(i) == ',') {
                        isElementExpected = true;
                        continue mainLoop;
                    } else if (text.charAt(i) == ']' && i == endInclusive) {
                        isElementExpected = false;
                        continue mainLoop;
                    } else if (text.charAt(i) != ' ')
                        throw new JSONParseException("JSON illegal character.", i);
                }
                // No continue was reached
                throw new JSONParseException("JSON null is not completed.", i);
            } else if (Character.isDigit(text.charAt(i))) {
                final var elementStartIndex = i;
                for (i++; i <= endInclusive; i++) {
                    if (!Character.isDigit(text.charAt(i)) && text.charAt(i) != '.') {
                        try {
                            final var possibleNumber = text.substring(elementStartIndex, i);
                            if (possibleNumber.indexOf('.') == -1) {
                                final var value = Long.parseLong(possibleNumber);
                                if (value <= (long) Integer.MAX_VALUE && value >= Integer.MIN_VALUE)
                                    saveArray.addInt((int) value);
                                else
                                    saveArray.addLong(value);
                            } else
                                saveArray.addDouble(Double.parseDouble(possibleNumber));
                        } catch (Exception ignored) {
                            throw new JSONParseException("JSON number illegal character.", i);
                        }


                        // Skip anything from here to the spliterator
                        for (; i <= endInclusive; i++) {
                            if (text.charAt(i) == ',') {
                                isElementExpected = true;
                                continue mainLoop;
                            } else if (text.charAt(i) == ']' && i == endInclusive) {
                                isElementExpected = false;
                                continue mainLoop;
                            } else if (text.charAt(i) != ' ')
                                throw new JSONParseException("JSON illegal character.", i);
                        }
                        // No continue was reached
                        throw new JSONParseException("JSON illegal character.", i);
                    }
                }
                // No continue was reached
                throw new JSONParseException("JSON number is not completed.", i);
            }
        }
        if (isElementExpected)
            throw new JSONParseException("JSON array expects another element.", endInclusive);

        return CompletableFuture.allOf(computations.toArray(CompletableFuture[]::new))
                .thenApply(__ -> array);
    }
}
