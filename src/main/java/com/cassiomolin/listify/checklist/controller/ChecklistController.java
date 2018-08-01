package com.cassiomolin.listify.checklist.controller;

import com.cassiomolin.listify.checklist.controller.mapper.ChecklistMapper;
import com.cassiomolin.listify.checklist.controller.model.CreateChecklistDetails;
import com.cassiomolin.listify.checklist.controller.model.QueryChecklistDetails;
import com.cassiomolin.listify.checklist.domain.Checklist;
import com.cassiomolin.listify.checklist.service.ChecklistService;
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

@RestController
@RequestMapping("/checklists")
public class ChecklistController {

    @Autowired
    private ChecklistMapper checklistMapper;

    @Autowired
    private ChecklistService checklistService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createChecklist(@Valid @NotNull @RequestBody CreateChecklistDetails createChecklistDetails) {

        Checklist checklist = checklistMapper.toChecklist(createChecklistDetails);
        checklist = checklistService.createChecklist(checklist);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(checklist.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryChecklistDetails> findUser(@PathVariable String id) {
        Checklist checklist = checklistService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(checklistMapper.toQueryChecklistDetails(checklist));
    }
}
