package com.cassiomolin.listela.checklist.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * API model that holds details for updating a task.
 *
 * @author cassiomolin
 */
@Data
@NoArgsConstructor
public class UpdateChecklistItemDetails {

    @NotBlank
    private String title;

    @NotNull
    private Boolean completed;
}