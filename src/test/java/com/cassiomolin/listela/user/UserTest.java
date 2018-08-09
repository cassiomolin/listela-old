package com.cassiomolin.listela.user;

import com.cassiomolin.listela.AbstractTest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserTest extends AbstractTest {

    @LocalServerPort
    private int port;

    @Value("${auth.jwt.secret}")
    private String key;

    @Value("${auth.jwt.issuer}")
    private String issuer;

    @Value("${auth.jwt.audience}")
    private String audience;

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

        shouldRegisterUser();

        var authDetails = new HashMap<String, String>();
        authDetails.put("email", "jane.doe@mail.com");
        authDetails.put("password", "password");

        String token =
        given()
                .port(port)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(authDetails)
        .when()
                .post("/auth")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("token", is(not(nullValue())))
                .extract().path("token");

        Claims claims = parseToken(token).getBody();
        assertNotNull(claims.getExpiration());
        assertEquals(audience, claims.getAudience());
        assertEquals(authDetails.get("email"), claims.getSubject());

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
                .body("firstName", equalTo("Jane"))
                .body("lastName", equalTo("Doe"))
                .body("email1", equalTo(authDetails.get("email")))
                .body("password", isEmptyOrNullString());
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .requireAudience(audience)
                .parseClaimsJws(token);
    }
}
