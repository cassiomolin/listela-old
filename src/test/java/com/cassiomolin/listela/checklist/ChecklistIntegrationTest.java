package com.cassiomolin.listela.checklist;

import com.cassiomolin.listela.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
public class ChecklistIntegrationTest extends AbstractIntegrationTest {

    @Override
    public void beforeEach() {
        super.beforeEach();
        createDefaultUser();
    }

    @Test
    public void shouldCreateAndGetChecklist() {

        String authenticationToken = issueAuthenticationTokenForDefaultUser();

        var checklistDetails = new HashMap<String, String>();
        checklistDetails.put("name", "Housework");
        String id = createChecklist(checklistDetails, authenticationToken);

        Map<String, Object> result = getChecklist(id, authenticationToken);
        assertEquals(id, result.get("id"));
        assertEquals(checklistDetails.get("name"), result.get("name"));
    }

    private String createChecklist(Map<String, String> checklistDetails, String authenticationToken) {

        Response response =
        given()
                .port(serverPort)
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationToken)
                .body(checklistDetails)
        .when()
                .post("/checklists")
        .then()
                .statusCode(201)
                .body(isEmptyString())
                .header(HttpHeaders.LOCATION, is(not(nullValue())))
        .extract()
                .response();

        String location = response.getHeader(HttpHeaders.LOCATION);
        return location.substring(location.lastIndexOf("/") + 1);
    }

    private Map<String, Object> getChecklist(String id, String authenticationToken) {

        Response response =
        given()
                .port(serverPort)
                .accept(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationToken)
        .when()
                .get("/checklists/{id}", id)
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
        .extract()
                .response();

        return response.body().jsonPath().getMap("");
    }
}
