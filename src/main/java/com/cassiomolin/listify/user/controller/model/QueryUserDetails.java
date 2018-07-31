package com.cassiomolin.listify.user.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QueryUserDetails {

    private String id;

    private String firstName;

    private String lastName;

    private String email;
}
