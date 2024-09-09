package persistence.exceptions;

/**
 * This exception is thrown when the API cannot be accessed.
 */
public class APICannotBeAccessedException extends PersistenceException {
    /**
     * Constructs a new APICannotBeAccessedException with default message.
     */
    public APICannotBeAccessedException(Exception cause) {
        super("\n Error: The API cannot be accessed at the moment.\n", cause);
    }
}
