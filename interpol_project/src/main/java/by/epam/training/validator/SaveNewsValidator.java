package by.epam.training.validator;

import by.epam.training.core.Bean;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Bean(name = "SaveNewsValidator")
@AllArgsConstructor
public class SaveNewsValidator implements ReqValidator {
    @Override
    public ValidationResult getValidationResult(HttpServletRequest request) {
        ValidationResult validationResult = new ValidationResult();

        final String topic = request.getParameter("news.topic");
        final String header = request.getParameter("news.header");
        final String text = request.getParameter("news.text");
        if (isEmpty(topic) || isEmpty(header) || isEmpty(text)) {
            validationResult.addError("wrong input", "all fields should be initialized");
        }
        if (topic.length() > 45){
            validationResult.addError("topic error", "topic should be less than 45 symbols");
        }
        if (header.length() > 100){
            validationResult.addError("topic error", "header should be less than 100 symbols");
        }
        if (text.length() > 2000){
            validationResult.addError("topic error", "text should be less than 2000 symbols");
        }
        return validationResult;
    }

    private boolean isEmpty(String str) {
        return str == null || str.equals("");
    }
}
