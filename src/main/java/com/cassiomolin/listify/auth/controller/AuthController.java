package com.cassiomolin.listify.auth.controller;

import com.cassiomolin.listify.auth.model.AuthenticationToken;
import com.cassiomolin.listify.auth.model.Credentials;
import com.cassiomolin.listify.auth.service.AuthenticationTokenService;
import com.cassiomolin.listify.checklist.controller.mapper.ChecklistMapper;
import com.cassiomolin.listify.checklist.controller.model.CreateChecklistDetails;
import com.cassiomolin.listify.checklist.controller.model.QueryChecklistDetails;
import com.cassiomolin.listify.checklist.domain.Checklist;
import com.cassiomolin.listify.checklist.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationToken> issueToken(@Valid @NotNull @RequestBody Credentials credentials) {

        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String token = authenticationTokenService.issueToken(username);
        return ResponseEntity.ok(new AuthenticationToken((token)));
    }
}
