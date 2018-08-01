package com.cassiomolin.listela.checklist.controller;

import com.cassiomolin.listela.checklist.controller.mapper.ChecklistItemMapper;
import com.cassiomolin.listela.checklist.controller.model.CreateChecklistItemDetails;
import com.cassiomolin.listela.checklist.controller.model.QueryChecklistItemDetails;
import com.cassiomolin.listela.checklist.domain.ChecklistItem;
import com.cassiomolin.listela.checklist.service.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class ChecklistItemController {

    @Autowired
    private ChecklistItemMapper checklistItemMapper;

    @Autowired
    private ChecklistItemService checklistItemService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTask(@Valid @NotNull @RequestBody CreateChecklistItemDetails createChecklistItemDetails) {

        ChecklistItem checklistItem = checklistItemMapper.toChecklistItem(createChecklistItemDetails);
        checklistItem = checklistItemService.createTask(checklistItem);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(checklistItem.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QueryChecklistItemDetails>> findTasks() {


        List<ChecklistItem> checklistItems = checklistItemService.findTasks();
        return ResponseEntity.ok(checklistItemMapper.toQueryTaskResults(checklistItems));
    }
}