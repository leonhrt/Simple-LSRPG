package business.exceptions;

/**
 * This exception is thrown when an adventure with the same name already exists in the system.
 */
public class AdventureNameAlreadyExistsException extends BusinessException {
    /**
     * Constructs a new AdventureNameAlreadyExistsException with default message.
     */
    public AdventureNameAlreadyExistsException() {
        super("\nError: The adventure's name already exists in the system.");
    }
}
