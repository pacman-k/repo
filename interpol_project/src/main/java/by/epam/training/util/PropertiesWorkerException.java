package by.epam.training.util;

public class PropertiesWorkerException extends RuntimeException {
    public PropertiesWorkerException() {

    }

    public PropertiesWorkerException(String message) {
        super(message);
    }

    public PropertiesWorkerException(String message, Exception e) {
        super(message, e);
    }
}
