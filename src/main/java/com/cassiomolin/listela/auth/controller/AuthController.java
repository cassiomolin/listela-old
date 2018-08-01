package com.cassiomolin.listela.auth.controller;

import com.cassiomolin.listela.auth.controller.model.AuthenticationToken;
import com.cassiomolin.listela.auth.controller.model.CredentialDetails;
import com.cassiomolin.listela.auth.service.AuthenticationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationToken> issueToken(@Valid @NotNull @RequestBody CredentialDetails credentialDetails) {

        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(credentialDetails.getEmail(), credentialDetails.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);

        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String token = authenticationTokenService.issueToken(username);
        return ResponseEntity.ok(new AuthenticationToken((token)));
    }
}
