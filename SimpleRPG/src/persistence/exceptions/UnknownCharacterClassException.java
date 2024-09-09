package persistence.exceptions;

/**
 * This exception is thrown when an unknown character class is found during persistence operations.
 */
public class UnknownCharacterClassException extends PersistenceException {
    /**
     * Constructs a new UnknownCharacterClassException with default message.
     */
    public UnknownCharacterClassException(String className) {
        super("Error: Unknown character class: " + className, null);

    }
}
