package com.cassiomolin.listela.checklist.controller;

import com.cassiomolin.listela.auth.AuthenticatedUserDetails;
import com.cassiomolin.listela.checklist.controller.mapper.ChecklistItemMapper;
import com.cassiomolin.listela.checklist.controller.model.CreateChecklistItemDetails;
import com.cassiomolin.listela.checklist.controller.model.QueryChecklistItemDetails;
import com.cassiomolin.listela.checklist.controller.model.UpdateChecklistItemDetails;
import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.checklist.domain.ChecklistItem;
import com.cassiomolin.listela.checklist.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/checklists/{checklistId}/items")
public class ChecklistItemController {

    @Autowired
    private ChecklistService checklistService;

    @Autowired
    private ChecklistItemMapper checklistItemMapper;

    /**
     * Add item to a checklist.
     *
     * @param checklistId
     * @param createChecklistItemDetails
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addItem(@PathVariable String checklistId,
                                        @RequestBody @Valid @NotNull CreateChecklistItemDetails createChecklistItemDetails) {

        Checklist checklist = findChecklist(checklistId);
        ChecklistItem checklistItem = checklistItemMapper.toChecklistItem(createChecklistItemDetails);
        checklistService.addItem(checklist, checklistItem);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(checklistItem.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Get items from a checklist.
     *
     * @param checklistId
     * @return
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QueryChecklistItemDetails>> getItems(@PathVariable String checklistId) {

        Checklist checklist = findChecklist(checklistId);

        return ResponseEntity.ok(checklistItemMapper.toQueryChecklistItemDetails(checklist.getItems()));
    }

    /**
     * Get item from a checklist.
     *
     * @param checklistId
     * @param itemId
     * @return
     */
    @GetMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryChecklistItemDetails> getItem(@PathVariable String checklistId,
                                                             @PathVariable String itemId) {

        Checklist checklist = findChecklist(checklistId);
        ChecklistItem checklistItem = findChecklistItem(checklist, itemId);

        return ResponseEntity.ok(checklistItemMapper.toQueryChecklistItemDetails(checklistItem));
    }

    /**
     * Delete item from a checklist.
     *
     * @param checklistId
     * @param itemId
     * @return
     */
    @DeleteMapping(path = "/{itemId}")
    public ResponseEntity<QueryChecklistItemDetails> deleteItem(@PathVariable String checklistId,
                                                                @PathVariable String itemId) {

        Checklist checklist = findChecklist(checklistId);
        ChecklistItem checklistItem = findChecklistItem(checklist, itemId);
        checklistService.deleteItem(checklist, checklistItem);

        return ResponseEntity.noContent().build();
    }

    /**
     * Update an item from a checklist.
     *
     * @param checklistId
     * @param itemId
     * @param updateChecklistItemDetails
     *
     * @return
     */
    @PutMapping(path = "/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateItem(@PathVariable String checklistId,
                                           @PathVariable String itemId,
                                           @RequestBody @Valid @NotNull UpdateChecklistItemDetails updateChecklistItemDetails) {

        Checklist checklist = findChecklist(checklistId);
        ChecklistItem checklistItem = findChecklistItem(checklist, itemId);
        checklistItemMapper.updateItem(updateChecklistItemDetails, checklistItem);
        checklistService.updateItem(checklistItem);

        return ResponseEntity.noContent().build();
    }

    private Checklist findChecklist(String checklistId) {
        return checklistService.findChecklist(checklistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private ChecklistItem findChecklistItem(Checklist checklist, String itemId) {
        return checklistService.findItem(checklist.getId(), itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}