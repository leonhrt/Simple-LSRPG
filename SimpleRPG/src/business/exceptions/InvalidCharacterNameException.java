package business.exceptions;

/**
 * This exception is thrown when an invalid character name is selected.
 */
public class InvalidCharacterNameException extends BusinessException {
    /**
     * Constructs a new InvalidCharacterNameException with default message.
     */
    public InvalidCharacterNameException() {
        super("\nError: The name must not have any number or special character.");
    }
}
