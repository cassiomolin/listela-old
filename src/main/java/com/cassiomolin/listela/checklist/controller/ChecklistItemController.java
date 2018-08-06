package com.cassiomolin.listela.checklist.controller;

import com.cassiomolin.listela.auth.AuthenticatedUserDetails;
import com.cassiomolin.listela.checklist.controller.mapper.ChecklistItemMapper;
import com.cassiomolin.listela.checklist.controller.model.CreateChecklistItemDetails;
import com.cassiomolin.listela.checklist.controller.model.QueryChecklistItemDetails;
import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.checklist.domain.ChecklistItem;
import com.cassiomolin.listela.checklist.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/checklists/{checklistId}/items")
public class ChecklistItemController {

    @Autowired
    private ChecklistService checklistService;

    @Autowired
    private ChecklistItemMapper checklistItemMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createItem(@PathVariable String checklistId,
                                           @Valid @NotNull @RequestBody CreateChecklistItemDetails createChecklistItemDetails,
                                           @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        ChecklistItem checklistItem = checklistItemMapper.toChecklistItem(createChecklistItemDetails);
        checklistService.addItemToChecklist(checklistId, authenticatedUserDetails.getId(), checklistItem);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(checklistItem.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QueryChecklistItemDetails>> getItems(@PathVariable String checklistId,
                                                                    @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        Checklist checklist = findChecklist(checklistId, authenticatedUserDetails.getId());
        return ResponseEntity.ok(checklistItemMapper.toQueryChecklistItemDetails(checklist.getItems()));
    }


    @GetMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryChecklistItemDetails> getItem(@PathVariable String checklistId,
                                                             @PathVariable String itemId,
                                                             @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        ChecklistItem checklistItem = findChecklistItem(checklistId, authenticatedUserDetails.getId(), itemId);
        return ResponseEntity.ok(checklistItemMapper.toQueryChecklistItemDetails(checklistItem));
    }

    @DeleteMapping(path = "/{itemId}")
    public ResponseEntity<QueryChecklistItemDetails> deleteItem(@PathVariable String checklistId,
                                                                @PathVariable String itemId,
                                                                @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        checklistService.deleteItemFromChecklist(checklistId, authenticatedUserDetails.getId(), itemId);
        return ResponseEntity.noContent().build();
    }

    private Checklist findChecklist(String checklistId, String userId) {
        return checklistService.findChecklist(checklistId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    private ChecklistItem findChecklistItem(String checklistId, String userId, String itemId) {
        return findChecklist(checklistId, userId)
                .getItems()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}