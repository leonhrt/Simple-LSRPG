package business.exceptions;

/**
 * This exception is thrown when an invalid option is selected for adding a character.
 */
public class InvalidCharacterToAddOptionException extends BusinessException {
    /**
     * Constructs a new InvalidCharacterToAddOptionException with default message.
     */
    public InvalidCharacterToAddOptionException() {
        super("\nError: Select a valid option.\n");
    }
}
