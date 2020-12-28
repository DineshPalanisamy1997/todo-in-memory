package com.apress.todo.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class TodoValidationErrorBuilder {
    public static TodoValidationError fromBindingError(Errors error){
        TodoValidationError todoValidationError =
                new TodoValidationError("Validation failed"+
                error.getErrorCount()+" errors");
        for(ObjectError objectError : error.getAllErrors())
        {
            todoValidationError.addValidation(objectError.getDefaultMessage());
        }
        return todoValidationError;
    }
}
