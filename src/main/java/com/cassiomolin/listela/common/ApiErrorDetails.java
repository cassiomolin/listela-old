package com.cassiomolin.listela.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApiErrorDetails {

    private Integer status;

    private OffsetDateTime timestamp = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    private String title;

    private String message;

    private String path;
}
