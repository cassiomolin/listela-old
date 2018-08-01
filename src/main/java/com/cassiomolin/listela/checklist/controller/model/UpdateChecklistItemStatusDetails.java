package com.cassiomolin.listela.checklist.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * API model that holds details for updating a task status.
 *
 * @author cassiomolin
 */
@Data
@NoArgsConstructor
public class UpdateChecklistItemStatusDetails {

    @NotNull
    private Boolean value;
}