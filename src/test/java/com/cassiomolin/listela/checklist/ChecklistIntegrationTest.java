package com.cassiomolin.listela.checklist;

import com.cassiomolin.listela.AbstractIntegrationTest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

        CreateChecklistDetails createChecklistDetails = new CreateChecklistDetails().setName("Housework");
        ResponseEntity<Object> createChecklistResponse = createChecklist(createChecklistDetails, authenticationToken);
        assertEquals(HttpStatus.CREATED, createChecklistResponse.getStatusCode());

        URI location = createChecklistResponse.getHeaders().getLocation();
        assertNotNull(location);

        ResponseEntity<QueryChecklistDetails> getChecklistResponse = getChecklist(getIdFromUri(location), authenticationToken);
        assertEquals(HttpStatus.OK, getChecklistResponse.getStatusCode());

        QueryChecklistDetails queryChecklistDetails = getChecklistResponse.getBody();
        assertNotNull(queryChecklistDetails);
        assertEquals(getIdFromUri(location), queryChecklistDetails.getId());
        assertEquals(createChecklistDetails.getName(), queryChecklistDetails.getName());
    }

    private ResponseEntity<Object> createChecklist(CreateChecklistDetails checklistDetails, String authenticationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION_SCHEME + authenticationToken);
        return restTemplate.postForEntity("/checklists", new HttpEntity<>(checklistDetails, headers), Object.class);
    }

    private ResponseEntity<List<QueryChecklistDetails>> getChecklists(String id, String authenticationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION_SCHEME + authenticationToken);
        return restTemplate.exchange("/checklists/{id}", HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<QueryChecklistDetails>>() {}, id);
    }

    private ResponseEntity<QueryChecklistDetails> getChecklist(String id, String authenticationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION_SCHEME + authenticationToken);
        return restTemplate.exchange("/checklists/{id}", HttpMethod.GET, new HttpEntity<>(headers), QueryChecklistDetails.class, id);
    }

    private ResponseEntity<Object> updateChecklist(String id, UpdateChecklistDetails checklistDetails, String authenticationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION_SCHEME + authenticationToken);
        return restTemplate.exchange("/checklists/{id}", HttpMethod.PUT, new HttpEntity<>(checklistDetails, headers), Object.class, id);
    }

    private ResponseEntity<Object> deleteChecklist(String id, String authenticationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION_SCHEME + authenticationToken);
        return restTemplate.exchange("/checklists/{id}", HttpMethod.DELETE, new HttpEntity<>(headers), Object.class, id);
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class CreateChecklistDetails {
        private String name;
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class UpdateChecklistDetails {
        private String name;
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class QueryChecklistDetails {
        private String id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class Foo {
        private String id;
        private String name;
    }
}
