package business.exceptions;

/**
 * This exception is thrown when an invalid character class is selected.
 */
public class InvalidCharacterClassException extends BusinessException {
    /**
     * Constructs a new InvalidCharacterClassException with default message.
     */
    public InvalidCharacterClassException() {
        super("\nError: Not a valid class.");
    }
}
