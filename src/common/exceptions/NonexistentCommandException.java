package common.exceptions;

public class NonexistentCommandException extends Exception{
    public NonexistentCommandException(String message) {
        super(message);
    }
}
