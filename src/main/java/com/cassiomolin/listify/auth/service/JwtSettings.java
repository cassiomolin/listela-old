package com.cassiomolin.listify.auth.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
class JwtSettings {

    /**
     * Secret for signing and verifying the token signature.
     */
    @Value("${auth.jwt.secret}")
    private String secret;

    /**
     * Allowed clock skew for verifying the token signature (in seconds).
     */
    @Value("${auth.jwt.clockSkew}")
    private Long clockSkew;

    /**
     * Identifies the recipients that the JWT token is intended for.
     */
    @Value("${auth.jwt.audience}")
    private String audience;

    /**
     * Identifies the JWT token issuer.
     */
    @Value("${auth.jwt.issuer}")
    private String issuer;

    /**
     * How long the token is valid for (in seconds).
     */
    @Value("${auth.jwt.validFor}")
    private Long validFor;
}