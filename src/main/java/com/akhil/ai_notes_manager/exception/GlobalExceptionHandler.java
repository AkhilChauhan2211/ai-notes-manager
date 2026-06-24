package com.akhil.ai_notes_manager.exception;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
            error->errors.put(
                error.getField(),
                error.getDefaultMessage()
            )
        );
        return errors;
    }

    @ExceptionHandler(AIServiceException.class)
    public Map<String,String> handleAIException(AIServiceException exe){
        Map<String,String> error=new HashMap<>();
        error.put("Error",exe.getMessage());
        return error;
    }



}
