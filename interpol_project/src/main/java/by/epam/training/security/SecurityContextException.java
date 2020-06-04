package by.epam.training.security;

public class SecurityContextException extends RuntimeException {
    public SecurityContextException(String message) {
        super(message);
    }

    public SecurityContextException(String message, Exception e) {
        super(message, e);
    }

}
