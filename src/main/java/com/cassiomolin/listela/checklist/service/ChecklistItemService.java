package com.cassiomolin.listela.checklist.service;

import com.cassiomolin.listela.checklist.domain.ChecklistItem;
import com.cassiomolin.listela.checklist.repository.ChecklistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class ChecklistItemService {

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    public ChecklistItem createTask(@NotNull ChecklistItem checklistItem) {
        checklistItem.setCompleted(false);
        return checklistItemRepository.save(checklistItem);
    }

    public List<ChecklistItem> findAllTasks() {
        return checklistItemRepository.findAll();
    }

    public List<ChecklistItem> findTasks() {

//        ChecklistItem task = new ChecklistItem();
//        task.setTitle(filter.getTitle());
//        task.setCompleted(filter.getCompleted());
//
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("title", match -> match.contains().ignoreCase());
//        Example<ChecklistItem> example = Example.of(task, matcher);
//
//        Sort sort = new Sort(Sort.Direction.ASC, "createdDate");

        return checklistItemRepository.findAll();
    }

    public void deleteTasks(Boolean completed) {

        if (completed == null) {
            checklistItemRepository.deleteAll();
        } else {
            ChecklistItem checklistItem = new ChecklistItem();
            checklistItem.setCompleted(completed);
            Example<ChecklistItem> example = Example.of(checklistItem);
            checklistItemRepository.deleteAll(checklistItemRepository.findAll(example));
        }
    }

    public Optional<ChecklistItem> findTask(@NotNull String taskId) {
        return checklistItemRepository.findById(taskId);
    }

    public void updateTask(@NotNull ChecklistItem checklistItem) {
        checklistItemRepository.save(checklistItem);
    }

    public void deleteTask(@NotNull String taskId) {
        checklistItemRepository.deleteById(taskId);
    }
}
