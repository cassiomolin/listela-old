package com.cassiomolin.listify.checklist.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreateChecklistDetails {

    @NotNull
    @NotBlank
    private String name;
}
