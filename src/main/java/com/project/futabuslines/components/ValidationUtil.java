package com.project.futabuslines.components;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
public class ValidationUtil {
    public List<String> getErrorMessages(BindingResult result) {
        return result.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
    }

    public boolean hasErrors(BindingResult result) {
        return result.hasErrors();
    }
}
