package com.cassiomolin.listela.common;

import com.cassiomolin.listela.common.ApiValidationErrorDetails.ValidationError;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class MethodArgumentNotValidExceptionHandler {

    @Autowired
    private ObjectMapper mapper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<? extends ApiErrorDetails> handleException(MethodArgumentNotValidException exception) {

        if (exception.getParameter().hasParameterAnnotation(RequestBody.class)) {
            return handleUnprocessableEntity(exception);
        } else {
            // Set status to 400
        }

        return ResponseEntity.unprocessableEntity().build();
    }

    private ResponseEntity<? extends ApiErrorDetails> handleUnprocessableEntity(MethodArgumentNotValidException exception) {

        ApiValidationErrorDetails errorDetails = new ApiValidationErrorDetails();
        errorDetails.setStatus(UNPROCESSABLE_ENTITY.value());
        errorDetails.setTitle(UNPROCESSABLE_ENTITY.getReasonPhrase());
        errorDetails.setMessage("Request cannot be processed due to validation errors");
        errorDetails.setPath(null); // FIXME

        JavaType type = mapper.getTypeFactory().constructType(exception.getParameter().getParameterType());
        List<BeanPropertyDefinition> properties = mapper.getSerializationConfig().introspect(type).findProperties();

        List<ValidationError> validationErrors = exception.getBindingResult().getFieldErrors().stream().map(fieldError -> {

            ValidationError validationError = new ValidationError();
            validationError.setMessage(fieldError.getDefaultMessage());
            validationError.setValue(fieldError.getRejectedValue());

            String path = properties.stream()
                    .filter(property -> property.getField().getName().equals(fieldError.getField()))
                    .map(BeanPropertyDefinition::getName)
                    .findFirst()
                    .orElse(fieldError.getField());
            validationError.setPath(path);

            return validationError;

        }).collect(Collectors.toList());

        errorDetails.setValidationErrors(validationErrors);


        return ResponseEntity.unprocessableEntity().body(errorDetails);
    }
}