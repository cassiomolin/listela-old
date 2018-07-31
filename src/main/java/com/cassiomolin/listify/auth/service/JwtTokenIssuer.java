package com.cassiomolin.listify.auth.service;

import com.cassiomolin.listify.auth.AuthenticationTokenDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenIssuer {

    @Autowired
    private JwtSettings settings;

    /**
     * Issue a JWT token
     *
     * @param authenticationTokenDetails
     * @return
     */
    public String issueToken(AuthenticationTokenDetails authenticationTokenDetails) {

        return Jwts.builder()
                .setId(authenticationTokenDetails.getId())
                .setIssuer(settings.getIssuer())
                .setAudience(settings.getAudience())
                .setSubject(authenticationTokenDetails.getUsername())
                .setIssuedAt(Date.from(authenticationTokenDetails.getIssuedDate().toInstant()))
                .setExpiration(Date.from(authenticationTokenDetails.getExpirationDate().toInstant()))
                .signWith(SignatureAlgorithm.HS256, settings.getSecret())
                .compact();
    }
}