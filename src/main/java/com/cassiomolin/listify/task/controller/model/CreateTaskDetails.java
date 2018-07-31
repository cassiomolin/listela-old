package com.cassiomolin.listify.task.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * API model that holds details for creating a task.
 *
 * @author cassiomolin
 */
@Data
@NoArgsConstructor
public class CreateTaskDetails {

    @NotBlank
    private String description;
}