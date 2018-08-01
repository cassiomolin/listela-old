package com.cassiomolin.listify.auth.service;

import com.cassiomolin.listify.auth.AuthenticationTokenDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class AuthenticationTokenService {

    @Autowired
    private JwtSettings settings;

    @Autowired
    private JwtTokenIssuer tokenIssuer;

    @Autowired
    private JwtTokenParser tokenParser;

    public String issueToken(String username) {

        String id = generateTokenIdentifier();
        OffsetDateTime issuedDate = OffsetDateTime.now();
        OffsetDateTime expirationDate = calculateExpirationDate(issuedDate);

        AuthenticationTokenDetails authenticationTokenDetails = AuthenticationTokenDetails.builder()
                .id(id)
                .username(username)
                .issuedDate(issuedDate)
                .expirationDate(expirationDate)
                .build();

        return tokenIssuer.issueToken(authenticationTokenDetails);
    }

    public AuthenticationTokenDetails parseToken(String token) {
        return tokenParser.parseToken(token);
    }


    /**
     * Calculate the expiration date for a token.
     *
     * @param issuedDate
     * @return
     */
    private OffsetDateTime calculateExpirationDate(OffsetDateTime issuedDate) {
        return issuedDate.plusSeconds(settings.getValidFor());
    }

    /**
     * Generate a token identifier.
     *
     * @return
     */
    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}