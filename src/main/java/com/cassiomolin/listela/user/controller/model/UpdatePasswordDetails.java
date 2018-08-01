package com.cassiomolin.listela.user.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UpdatePasswordDetails {

    @NotNull
    @NotBlank
    @Size(min = 8)
    private String password;

}
