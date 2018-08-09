package com.cassiomolin.listela.user;

import com.cassiomolin.listela.AbstractTest;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
/import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.isEmptyString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserTest extends AbstractTest {

    @LocalServerPort
    private int port;

    @Before
    public void before() {
        cleanDatabase();
    }

    @Test
    public void shouldRegisterUser() {

        var userDetails = new HashMap<String, Object>();
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
                .body(isEmptyString());
    }
}
