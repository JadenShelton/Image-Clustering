/**
 * Custom Exception for handling invalid images, for better error handling.
 */
public class InvalidImageFormatException extends Exception {
    public InvalidImageFormatException(String message) {
        super(message);
    }
    
}
