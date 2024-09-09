package business.exceptions;

/**
 * This exception is thrown when an invalid character level is selected.
 */
public class InvalidCharacterLevelException extends BusinessException {
    /**
     * Constructs a new InvalidCharacterLevelException with default message.
     */
    public InvalidCharacterLevelException() {
        super("\nError: The character level must be between 1 and 10 both included.\n");
    }
}
