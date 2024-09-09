package business.exceptions;

/**
 * This exception is thrown when a character is already in the party.
 */
public class CharacterAlreadyInThePartyException extends BusinessException {
    /**
     * Constructs a new CharacterAlreadyInThePartyException with default message.
     */
    public CharacterAlreadyInThePartyException() {
        super("\nError: That character is already in the party!\n");
    }
}
