package by.epam.training.core;

public class MissedAnnotationException extends BeanRegistrationException {
    public MissedAnnotationException(String message) {
        super(message);
    }

    public MissedAnnotationException(String message, Exception e) {
        super(message, e);
    }
}
