package com.cassiomolin.listify.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class Credentials {

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;
}
