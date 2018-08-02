package com.cassiomolin.listela.checklist.controller;

import com.cassiomolin.listela.checklist.controller.mapper.ChecklistMapper;
import com.cassiomolin.listela.checklist.controller.model.CreateChecklistDetails;
import com.cassiomolin.listela.checklist.controller.model.QueryChecklistDetails;
import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.checklist.service.ChecklistService;
import com.cassiomolin.listela.user.domain.User;
import com.cassiomolin.listela.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/checklists")
public class ChecklistController {

    @Autowired
    private ChecklistMapper checklistMapper;

    @Autowired
    private ChecklistService checklistService;

    @Autowired
    private UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createChecklist(@Valid @NotNull @RequestBody CreateChecklistDetails createChecklistDetails,
                                                Principal principal) {

        Checklist checklist = checklistMapper.toChecklist(createChecklistDetails);

        User user = findUser(principal);
        checklist.setOwner(user);

        checklist = checklistService.createChecklist(checklist);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(checklist.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QueryChecklistDetails>> getChecklists(Principal principal) {

        User user = findUser(principal);
        List<Checklist> checklists = checklistService.findAllByUser(user);

        return ResponseEntity.ok(checklistMapper.toQueryChecklistDetails(checklists));
    }

    @GetMapping(path = "/{checklistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryChecklistDetails> findChecklist(@PathVariable String checklistId) {
        Checklist checklist = checklistService.findById(checklistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(checklistMapper.toQueryChecklistDetails(checklist));
    }

    @DeleteMapping(path = "/{checklistId}")
    public ResponseEntity<Void> deleteChecklist(@PathVariable String checklistId) {
        checklistService.delete(checklistId);
        return ResponseEntity.noContent().build();
    }

    private User findUser(Principal principal) {
        return userService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
