package business.exceptions;

/**
 * This exception is thrown when an invalid option is selected for an adventure.
 */
public class InvalidAdventureOptionException extends BusinessException {
    /**
     * Constructs a new InvalidAdventureOptionException with default message.
     */
    public InvalidAdventureOptionException() {
        super("\nError: Select a valid option.\n");
    }
}
