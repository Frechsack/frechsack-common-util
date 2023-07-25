package frechsack.prod.util.filesystem;

public class JSONParseException extends RuntimeException {

    public JSONParseException(String message, int errorIndex) {
        super(String.format("%s Error at position: " + errorIndex, message, errorIndex));
    }
}
