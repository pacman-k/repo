package by.epam.training.validator;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    private Map<String, String> result;

    public ValidationResult() {
        result = new HashMap<>();
    }

    public void addError(String key, String value) {
        result.put(key, value);
    }

    public boolean isValid() {
        return result.isEmpty();
    }

    public Map<String, String> getResult() {
        return result;
    }

    public String getAllVales() {
        return String.join(";\n", result.values());
    }
}
