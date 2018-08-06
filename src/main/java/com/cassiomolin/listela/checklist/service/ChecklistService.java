package com.cassiomolin.listela.checklist.service;

import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.checklist.domain.ChecklistItem;
import com.cassiomolin.listela.checklist.repository.ChecklistItemRepository;
import com.cassiomolin.listela.checklist.repository.ChecklistRepository;
import com.cassiomolin.listela.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChecklistService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    public Checklist createChecklist(Checklist checklist) {
        return checklistRepository.insert(checklist);
    }

    public Optional<Checklist> findChecklist(String checklistId, String userId) {
        return checklistRepository.findByIdAndOwnerId(checklistId, userId);
    }

    public List<Checklist> findChecklists(String userId) {
        return checklistRepository.findAllByOwnerId(userId);
    }

    public void deleteChecklist(String id) {
        checklistRepository.deleteById(id);
    }

    public Checklist updateChecklist(Checklist checklist) {
        return checklistRepository.save(checklist);
    }

    public ChecklistItem addItemToChecklist(String checklistId, String userId, ChecklistItem checklistItem) {

        Optional<Checklist> optional = findChecklist(checklistId, userId);
        if (optional.isPresent()) {

            checklistItem.setCompleted(false);
            checklistItem = checklistItemRepository.save(checklistItem);

            Checklist checklist = optional.get();
            List<ChecklistItem> items = Optional.ofNullable(checklist.getItems()).orElse(new ArrayList<>());
            items.add(checklistItem);
            checklist.setItems(items);

            checklistRepository.save(checklist);
        }

        return null; // fixme
    }

    public void deleteItemFromChecklist(String checklistId, String userId, String itemId) {

        Optional<Checklist> optional = findChecklist(checklistId, userId);
        if (optional.isPresent()) {

            checklistItemRepository.deleteById(itemId);

            Checklist checklist = optional.get();
            Optional.ofNullable(checklist.getItems())
                    .ifPresent(items -> items.removeIf(item -> item.getId().equals(itemId)));

            checklistRepository.save(checklist);
        }
    }
}
