package compiler488.ast;

/**
 * Created by gg on 01/03/17.
 */
public class InvalidASTException extends RuntimeException {
    public InvalidASTException() {
        super();
    }

    public InvalidASTException(String message) {
        super(message);
    }

    public InvalidASTException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidASTException(Throwable cause) {
        super(cause);
    }
}
