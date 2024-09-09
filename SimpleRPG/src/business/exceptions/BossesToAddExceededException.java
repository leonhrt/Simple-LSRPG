package business.exceptions;

/**
 * This exception is thrown when the number of bosses to add to an encounter exceeds the maximum allowed.
 */
public class BossesToAddExceededException extends BusinessException {
    /**
     * Constructs a new BossesToAddExceededException with the specified detail message.
     *
     * @param message the detail message
     */
    public BossesToAddExceededException(String message) {
        super(message);
    }
}
