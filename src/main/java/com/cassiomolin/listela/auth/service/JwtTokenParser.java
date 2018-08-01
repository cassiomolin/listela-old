package com.cassiomolin.listela.auth.service;

import com.cassiomolin.listela.auth.AuthenticationTokenDetails;
import com.cassiomolin.listela.auth.InvalidAuthenticationTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class JwtTokenParser {

    @Autowired
    private JwtSettings settings;

    /**
     * Parse a JWT token.
     *
     * @param token
     * @return
     */
    public AuthenticationTokenDetails parseToken(String token) {

        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(settings.getSecret())
                    .requireAudience(settings.getAudience())
                    .setAllowedClockSkewSeconds(settings.getClockSkew())
                    .parseClaimsJws(token)
                    .getBody();

            return AuthenticationTokenDetails.builder()
                    .id(extractTokenIdFromClaims(claims))
                    .username(extractUsernameFromClaims(claims))
                    .issuedDate(extractIssuedDateFromClaims(claims))
                    .expirationDate(extractExpirationDateFromClaims(claims))
                    .build();

        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException e) {
            throw new InvalidAuthenticationTokenException("Invalid token", e);
        } catch (ExpiredJwtException e) {
            throw new InvalidAuthenticationTokenException("Expired token", e);
        } catch (InvalidClaimException e) {
            throw new InvalidAuthenticationTokenException("Invalid value for claim \"" + e.getClaimName() + "\"", e);
        } catch (Exception e) {
            throw new InvalidAuthenticationTokenException("Invalid token", e);
        }
    }

    /**
     * Extract the token identifier from the token claims.
     *
     * @param claims
     * @return Identifier of the JWT token
     */
    private String extractTokenIdFromClaims(@NotNull Claims claims) {
        return (String) claims.get(Claims.ID);
    }

    /**
     * Extract the username from the token claims.
     *
     * @param claims
     * @return Username from the JWT token
     */
    private String extractUsernameFromClaims(@NotNull Claims claims) {
        return claims.getSubject();
    }


    /**
     * Extract the issued date from the token claims.
     *
     * @param claims
     * @return Issued date of the JWT token
     */
    private OffsetDateTime extractIssuedDateFromClaims(@NotNull Claims claims) {
        return OffsetDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault());
    }

    /**
     * Extract the expiration date from the token claims.
     *
     * @param claims
     * @return Expiration date of the JWT token
     */
    private OffsetDateTime extractExpirationDateFromClaims(@NotNull Claims claims) {
        return OffsetDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }
}