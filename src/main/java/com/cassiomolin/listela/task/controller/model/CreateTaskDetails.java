package com.cassiomolin.listela.task.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * API model that holds details for creating a task.
 *
 * @author cassiomolin
 */
@Data
@NoArgsConstructor
public class CreateTaskDetails {

    @NotNull
    @NotBlank
    private String description;
}