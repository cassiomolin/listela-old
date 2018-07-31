package com.cassiomolin.listify.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Wither;

import java.time.OffsetDateTime;
import java.util.Set;

@Builder
@Getter
public class AuthenticationTokenDetails {

    private String id;

    private String username;

    private OffsetDateTime issuedDate;

    private OffsetDateTime expirationDate;
}