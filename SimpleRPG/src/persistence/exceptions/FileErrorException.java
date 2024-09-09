package persistence.exceptions;

/**
 * This exception is thrown when an error occurs while accessing a file.
 */
public class FileErrorException extends PersistenceException {
    /**
     * Constructs a new FileErrorException with default message.
     */
    public FileErrorException(String file, Exception cause) {
        super ("Error: The " + file + " file can't be accessed.", cause);
    }
}
