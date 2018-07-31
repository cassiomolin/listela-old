package com.cassiomolin.listify.auth;


import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationTokenException extends AuthenticationException {

    public InvalidAuthenticationTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}