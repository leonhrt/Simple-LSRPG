package business.exceptions;

/**
 * This exception is thrown when an encounter is empty.
 */
public class EmptyEncounterException extends BusinessException {
    /**
     * Constructs a new EmptyEncounterException with the specified detail message.
     *
     * @param message the detail message
     */
    public EmptyEncounterException(String message) {
        super(message);
    }
}
