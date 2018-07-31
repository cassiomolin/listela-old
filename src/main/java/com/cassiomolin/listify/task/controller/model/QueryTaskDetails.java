package com.cassiomolin.listify.task.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * API model for returning a task query result.
 *
 * @author cassiomolin
 */
@Data
@NoArgsConstructor
public class QueryTaskDetails {

    private String id;

    private String description;

    private Boolean completed;

    private ZonedDateTime createdDate;
}