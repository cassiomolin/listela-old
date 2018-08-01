package com.cassiomolin.listela.checklist.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreateChecklistItemDetails {

    @NotNull
    @NotBlank
    private String description;
}