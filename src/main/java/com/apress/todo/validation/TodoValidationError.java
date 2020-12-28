package com.apress.todo.validation;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class TodoValidationError {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<String> errors = new ArrayList<>();
    private final String errorMessage;

    public TodoValidationError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void addValidation(String error){
        errors.add(error);
    }

    public List<String> getError() {
        return errors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
