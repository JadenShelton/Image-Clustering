//custom exception for more accurate error handling
public class InvalidImageFormatException extends Exception {
    public InvalidImageFormatException(String message) {
        super(message);
    }
    
}
