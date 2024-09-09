package business.exceptions;

/**
 * This exception is thrown when no characters are found.
 */
public class NoCharactersFoundException extends BusinessException {
    /**
     * Constructs a new NoCharactersFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public NoCharactersFoundException(String message) {
        super(message);
    }
}
