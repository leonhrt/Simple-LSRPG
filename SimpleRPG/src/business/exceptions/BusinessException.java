package business.exceptions;

/**
 * This is a general exception class for business logic errors.
 */
public class BusinessException extends Exception {
    /**
     * Constructs a new BusinessException with the specified error message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public BusinessException(String message) {
        super(message);
    }
}
