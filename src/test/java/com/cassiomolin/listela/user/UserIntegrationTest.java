package com.cassiomolin.listela.user;

import com.cassiomolin.listela.AbstractIntegrationTest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class UserIntegrationTest extends AbstractIntegrationTest {

    @Before
    public void before() {
        cleanDatabase();
    }

    @Test
    public void shouldRegisterUserAndAuthenticate() {

        CreateUserDetails createUserDetails = new CreateUserDetails()
                .setFirstName("Jane")
                .setLastName("Doe")
                .setEmail("jane.doe@mail.com")
                .setPassword("password");

        ResponseEntity<Void> registerUserResponse = registerUser(createUserDetails);
        assertEquals(HttpStatus.CREATED, registerUserResponse.getStatusCode());

        URI location = registerUserResponse.getHeaders().getLocation();
        assertNotNull(location);

        Credentials credentials = new Credentials()
                .setEmail("jane.doe@mail.com")
                .setPassword("password");

        ResponseEntity<AuthenticationToken> authenticateResponse = authenticate(credentials);
        assertEquals(HttpStatus.OK, authenticateResponse.getStatusCode());

        AuthenticationToken authenticationToken = authenticateResponse.getBody();
        assertNotNull(authenticationToken);

        Claims claims = parseAuthenticationToken(authenticationToken.getToken()).getBody();
        assertNotNull(claims.getId());
        assertEquals(jwtIssuer, claims.getIssuer());
        assertEquals(jwtAudience, claims.getAudience());
        assertEquals(createUserDetails.getEmail(), claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertTrue(OffsetDateTime.now().isBefore(OffsetDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault())));

        ResponseEntity<QueryUserDetails> getAuthenticatedUserResponse = getAuthenticatedUser(authenticationToken.getToken());
        assertEquals(HttpStatus.OK, getAuthenticatedUserResponse.getStatusCode());

        QueryUserDetails queryUserDetails = getAuthenticatedUserResponse.getBody();
        assertNotNull(queryUserDetails);

        assertEquals(getIdFromUri(location), queryUserDetails.getId());
        assertEquals(createUserDetails.getFirstName(), queryUserDetails.getFirstName());
        assertEquals(createUserDetails.getLastName(), queryUserDetails.getLastName());
        assertEquals(createUserDetails.getEmail(), queryUserDetails.getEmail());
    }

    @Test
    public void shouldNotAllowTwoUsersWithSameEmail() {

        CreateUserDetails createUserDetails = new CreateUserDetails()
                .setFirstName("Jane")
                .setLastName("Doe")
                .setEmail("jane.doe@mail.com")
                .setPassword("password");

        ResponseEntity<Void> registerUserResponse = registerUser(createUserDetails);
        assertEquals(HttpStatus.CREATED, registerUserResponse.getStatusCode());

        registerUserResponse = registerUser(createUserDetails);
        assertEquals(HttpStatus.CONFLICT, registerUserResponse.getStatusCode());
    }

    private ResponseEntity<Void> registerUser(CreateUserDetails createUserDetails) {
        return restTemplate.postForEntity("/users", new HttpEntity<>(createUserDetails), Void.class);
    }

    private ResponseEntity<AuthenticationToken> authenticate(Credentials credentials) {
        return restTemplate.postForEntity("/auth", new HttpEntity<>(credentials), AuthenticationToken.class);
    }

    private ResponseEntity<QueryUserDetails> getAuthenticatedUser(String authenticationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationToken);
        return restTemplate.exchange("/users/me", HttpMethod.GET, new HttpEntity<>(headers), QueryUserDetails.class);
    }

    private Jws<Claims> parseAuthenticationToken(String authenticationToken) {
        return Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(authenticationToken);
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class Credentials {
        private String email;
        private String password;
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class AuthenticationToken {
        private String token;
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class CreateUserDetails {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class QueryUserDetails {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
    }
}
