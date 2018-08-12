package com.cassiomolin.listela.checklist;

import com.cassiomolin.listela.AbstractIntegrationTest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

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
        ResponseEntity<Void> createChecklistResponse = createChecklist(createChecklistDetails, authenticationToken);
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

    private ResponseEntity<Void> createChecklist(CreateChecklistDetails createChecklistDetails, String authenticationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION_SCHEME + authenticationToken);
        return restTemplate.postForEntity("/checklists", new HttpEntity<>(createChecklistDetails, headers), Void.class);
    }

    private ResponseEntity<QueryChecklistDetails> getChecklist(String id, String authenticationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION_SCHEME + authenticationToken);
        return restTemplate.exchange("/checklists/{id}", HttpMethod.GET, new HttpEntity<>(headers), QueryChecklistDetails.class, id);
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
    private static class QueryChecklistDetails {
        private String id;
        private String name;
    }
}
