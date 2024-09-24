package common.exceptions;

public class NotAllowedCommandException extends RuntimeException {
    public NotAllowedCommandException(String message) {
        super(message);
    }
}
