package com.cassiomolin.listela.common;

import com.cassiomolin.listela.common.ApiValidationErrorDetails.ValidationError;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class MethodArgumentNotValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<? extends ApiErrorDetails> handleException(MethodArgumentNotValidException exception) {

        if (exception.getParameter().hasParameterAnnotation(RequestBody.class)) {
            return handleUnprocessableEntity(exception);
        }

        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity<? extends ApiErrorDetails> handleUnprocessableEntity(MethodArgumentNotValidException exception) {

        List<ValidationError> validationErrors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toValidationError)
                .collect(Collectors.toList());

        ApiValidationErrorDetails errorDetails = new ApiValidationErrorDetails();
        errorDetails.setStatus(UNPROCESSABLE_ENTITY.value());
        errorDetails.setTitle(UNPROCESSABLE_ENTITY.getReasonPhrase());
        errorDetails.setMessage("Request cannot be processed due to validation errors");
        errorDetails.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorDetails.setValidationErrors(validationErrors);

        return ResponseEntity.unprocessableEntity().body(errorDetails);
    }

    private ValidationError toValidationError(FieldError fieldError) {

        ValidationError validationError = new ValidationError();
        validationError.setMessage(fieldError.getDefaultMessage());
        validationError.setValue(fieldError.getRejectedValue());
        validationError.setPath(fieldError.getField());

        return validationError;
    }
}