package com.cassiomolin.listela.checklist;

import com.cassiomolin.listela.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
public class ChecklistIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Override
    public void beforeEach() {
        super.beforeEach();
        createTestUser();
    }

    @Test
    public void shouldCreateChecklist() {

        String authenticationToken = issueAuthenticationToken("jane.doe@mail.com");

        var checklistDetails = new HashMap<String, String>();
        checklistDetails.put("name", "Housework");
        createChecklist(checklistDetails, authenticationToken);
    }

    private void createChecklist(Map<String, String> checklistDetails, String authenticationToken) {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationToken)
                .body(checklistDetails)
        .when()
                .post("/checklists")
        .then()
                .statusCode(201)
                .body(isEmptyString())
                .header(HttpHeaders.LOCATION, is(not(nullValue())));
    }
}
