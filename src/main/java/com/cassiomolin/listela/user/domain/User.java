package com.cassiomolin.listela.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private boolean active;

    private OffsetDateTime createdDate;
}
