package com.cassiomolin.listela.checklist.controller;

import com.cassiomolin.listela.auth.AuthenticatedUserDetails;
import com.cassiomolin.listela.checklist.controller.mapper.ChecklistMapper;
import com.cassiomolin.listela.checklist.controller.model.CreateChecklistDetails;
import com.cassiomolin.listela.checklist.controller.model.QueryChecklistDetails;
import com.cassiomolin.listela.checklist.controller.model.UpdateChecklistDetails;
import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.checklist.service.ChecklistService;
import com.cassiomolin.listela.user.domain.User;
import com.cassiomolin.listela.user.service.UserService;
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

@RestController
@RequestMapping("/checklists")
public class ChecklistController {

    @Autowired
    private ChecklistMapper checklistMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ChecklistService checklistService;

    /**
     * Create a checklist.
     *
     * @param createChecklistDetails
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createChecklist(@Valid @NotNull @RequestBody CreateChecklistDetails createChecklistDetails,
                                                @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        Checklist checklist = checklistMapper.toChecklist(createChecklistDetails);
        checklist.setOwner(new User().setId(authenticatedUserDetails.getId()));
        checklist = checklistService.createChecklist(checklist);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(checklist.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Get a representation of a checklist.
     *
     * @return
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QueryChecklistDetails>> getChecklists(@AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        List<Checklist> checklists = checklistService.findChecklists(authenticatedUserDetails.getId());
        return ResponseEntity.ok(checklistMapper.toQueryChecklistDetails(checklists));
    }

    /**
     * Get a representation of all checklists of a user.
     * @param checklistId
     * @return
     */
    @GetMapping(path = "/{checklistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryChecklistDetails> getChecklist(@PathVariable String checklistId,
                                                              @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        Checklist checklist = findChecklist(checklistId, authenticatedUserDetails.getId());
        return ResponseEntity.ok(checklistMapper.toQueryChecklistDetails(checklist));
    }

    /**
     * Delete a checklist.
     *
     * @param checklistId
     * @return
     */
    @DeleteMapping(path = "/{checklistId}")
    public ResponseEntity<Void> deleteChecklist(@PathVariable String checklistId,
                                                @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        Checklist checklist = findChecklist(checklistId, authenticatedUserDetails.getId());
        checklistService.deleteChecklist(checklist.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Update details of a checklist.
     *
     * @param checklistId
     * @param updateChecklistDetails
     * @return
     */
    @PutMapping(path = "/{checklistId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateChecklist(@PathVariable String checklistId,
                                                @Valid @NotNull @RequestBody UpdateChecklistDetails updateChecklistDetails,
                                                @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        Checklist checklist = findChecklist(checklistId, authenticatedUserDetails.getId());
        checklistMapper.updateChecklist(updateChecklistDetails, checklist);
        checklistService.updateChecklist(checklist);
        return ResponseEntity.noContent().build();
    }


    private Checklist findChecklist(String checklistId, String userId) {

        Checklist checklist = checklistService.findChecklist(checklistId, userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!checklist.getOwner().getEmail().equals(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return checklist;
    }
}
