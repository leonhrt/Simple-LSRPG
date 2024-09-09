package business.exceptions;

/**
 * This exception is thrown when the amount of bosses in an encounter exceeds the maximum allowed.
 */
public class BossAmountExceededException extends BusinessException {
    /**
     * Constructs a new BossAmountExceededException with the specified detail message.
     *
     * @param message the detail message
     */
    public BossAmountExceededException(String message) {
        super(message);
    }
}
