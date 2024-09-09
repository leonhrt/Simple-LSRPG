package business.exceptions;

/**
 * This exception is thrown when a character with the same name already exists in the system.
 */
public class CharacterNameAlreadyExistsException extends BusinessException {
    /**
     * Constructs a new CharacterNameAlreadyExistsException with default message.
     */
    public CharacterNameAlreadyExistsException() {
        super("\nError: The character's name already exists in the system.");
    }
}
