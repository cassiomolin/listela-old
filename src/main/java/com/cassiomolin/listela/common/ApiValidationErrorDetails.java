package com.cassiomolin.listela.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApiValidationErrorDetails extends ApiErrorDetails {

    private List<ValidationError> validationErrors;

    @Data
    @NoArgsConstructor
    public static class ValidationError {

        private Object value;

        private String path;

        private String message;
    }
}
