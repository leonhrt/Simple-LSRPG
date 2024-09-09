package business.exceptions;

/**
 * This exception is thrown when an invalid number of characters is selected for an adventure.
 */
public class InvalidCharactersAmountForAdventureException extends BusinessException {
    /**
     * Constructs a new InvalidCharactersAmountForAdventureException with default message.
     */
    public InvalidCharactersAmountForAdventureException() {
        super("\nError: Select a valid option.\n");
    }
}
