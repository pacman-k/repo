package by.epam.training.core;

public class NotUniqueBeanException extends BeanRegistrationException {
    public NotUniqueBeanException(String message) {
        super(message);
    }

    public NotUniqueBeanException(String message, Exception e) {
        super(message, e);
    }
}
