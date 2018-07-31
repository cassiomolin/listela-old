package com.cassiomolin.listify.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApiErrorDetails {

    private Integer status;

    private String title;

    private String message;

    private String path;
}
