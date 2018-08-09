package com.cassiomolin.listela.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Email already registered")
public class EmailAlreadyRegisteredException extends RuntimeException {
}
