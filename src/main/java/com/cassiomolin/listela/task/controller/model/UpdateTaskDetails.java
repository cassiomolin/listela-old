package com.cassiomolin.listela.task.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * API model that holds details for updating a task.
 *
 * @author cassiomolin
 */
@Data
@NoArgsConstructor
public class UpdateTaskDetails {

    @NotBlank
    private String description;

    @NotNull
    private Boolean completed;
}