package com.cassiomolin.listela.user;

import com.cassiomolin.listela.AbstractIntegrationTest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Value("${auth.jwt.secret}")
    private String jwtKey;

    @Value("${auth.jwt.issuer}")
    private String jwtIssuer;

    @Value("${auth.jwt.audience}")
    private String jwtAudience;

    @Before
    public void before() {
        cleanDatabase();
    }

    @Test
    public void shouldRegisterUser() {

        var userDetails = new HashMap<String, String>();
        userDetails.put("firstName", "Jane");
        userDetails.put("lastName", "Doe");
        userDetails.put("email", "jane.doe@mail.com");
        userDetails.put("password", "password");
        registerUser(userDetails);
    }

    private void registerUser(Map<String, String> userDetails) {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(userDetails)
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .body(isEmptyString())
                .header(HttpHeaders.LOCATION, is(not(nullValue())));
    }

    @Test
    public void shouldRegisterUserAndAuthenticate() {

        var userDetails = new HashMap<String, String>();
        userDetails.put("firstName", "Jane");
        userDetails.put("lastName", "Doe");
        userDetails.put("email", "jane.doe@mail.com");
        userDetails.put("password", "password");
        registerUser(userDetails);

        var credentials = new HashMap<String, String>();
        credentials.put("email", "jane.doe@mail.com");
        credentials.put("password", "password");
        String token = authenticate(credentials);
        parseToken(token);

        given()
                .port(port)
                .accept(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .when()
                .get("/users/me")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", not(isEmptyOrNullString()))
                .body("firstName", equalTo(userDetails.get("firstName")))
                .body("lastName", equalTo(userDetails.get("lastName")))
                .body("email", equalTo(userDetails.get("email")))
                .body("password", isEmptyOrNullString());
    }

    private String authenticate(Map<String, String> credentials) {

        String token =
        given()
                .port(port)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(credentials)
        .when()
                .post("/auth")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("token", is(not(nullValue())))
        .extract()
                .path("token");

        return token;
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .requireAudience(jwtAudience)
                .parseClaimsJws(token);
    }

    @Test
    public void shouldNotAllowTwoUsersWithSameEmail() {

        var userDetails = new HashMap<String, String>();
        userDetails.put("firstName", "Jane");
        userDetails.put("lastName", "Doe");
        userDetails.put("email", "jane.doe@mail.com");
        userDetails.put("password", "password");
        registerUser(userDetails);

         given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(userDetails)
        .when()
                .post("/users")
        .then()
                .statusCode(409);
    }
}
