package com.cassiomolin.listify.auth;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@Getter
public class AuthenticationTokenDetails {

    private String id;

    private String username;

    private OffsetDateTime issuedDate;

    private OffsetDateTime expirationDate;
}