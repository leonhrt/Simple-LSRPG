package business.exceptions;

/**
 * This exception is thrown when the number of characters is insufficient to start an adventure.
 */
public class InsufficientCharactersAmountException extends BusinessException {
    /**
     * Constructs a new InsufficientCharactersAmountException with default message.
     */
    public InsufficientCharactersAmountException() {
        super("\nError: The system needs at least 3 characters to start an adventure.");
    }
}
