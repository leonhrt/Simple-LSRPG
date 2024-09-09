package persistence.exceptions;

/**
 * This exception is thrown when an error occurs during persistence operations.
 */
public class PersistenceException extends Exception {
    /**
     * Constructs a new PersistenceException with the specified error message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public PersistenceException(String message, Exception cause) {
        super(message, cause);
    }
}