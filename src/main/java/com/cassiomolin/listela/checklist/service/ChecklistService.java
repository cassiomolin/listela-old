package com.cassiomolin.listela.checklist.service;

import com.cassiomolin.listela.auth.AuthenticatedUserDetails;
import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.checklist.domain.ChecklistItem;
import com.cassiomolin.listela.checklist.repository.ChecklistItemRepository;
import com.cassiomolin.listela.checklist.repository.ChecklistRepository;
import com.cassiomolin.listela.user.domain.User;
import com.cassiomolin.listela.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    /**
     * Create a checklist.
     *
     * @param checklist
     * @return
     */
    public Checklist createChecklist(Checklist checklist) {
        User user = userRepository.findById(getAuthenticatedUser().getId()).get();
        checklist.setOwner(user);
        return checklistRepository.insert(checklist);
    }

    /**
     * Find a checklist by id.
     *
     * @param checklistId
     * @return
     */
    public Optional<Checklist> findChecklist(String checklistId) {
        return checklistRepository.findByIdAndOwnerId(checklistId, getAuthenticatedUser().getId());
    }

    /**
     * Find checklists.
     *
     * @return
     */
    public List<Checklist> findChecklists() {
        return checklistRepository.findAllByOwnerId(getAuthenticatedUser().getId());
    }

    /**
     * Delete a checklist by id.
     *
     * @param checklist
     */
    public void deleteChecklist(Checklist checklist) {
        checklistRepository.delete(checklist);
    }

    /**
     * Update a checklist.
     *
     * @param checklist
     * @return
     */
    public Checklist updateChecklist(Checklist checklist) {
        return checklistRepository.save(checklist);
    }

    /**
     * Add item to a checklist.
     *
     * @param checklist
     * @param checklistItem
     * @return
     */
    public ChecklistItem addItem(Checklist checklist, ChecklistItem checklistItem) {

        checklistItem.setCompleted(false);
        checklistItem.setChecklist(checklist);
        checklistItem = checklistItemRepository.save(checklistItem);

        List<ChecklistItem> items = Optional.ofNullable(checklist.getItems()).orElse(new ArrayList<>());
        items.add(checklistItem);
        checklist.setItems(items);

        checklistRepository.save(checklist);
        return checklistItem;
    }

    /**
     * Find item from a checklist.
     *
     * @param checklistId
     * @param itemId
     * @return
     */
    public Optional<ChecklistItem> findItem(String checklistId, String itemId) {
        return checklistItemRepository.findByIdAndChecklistId(itemId, checklistId);
    }

    /**
     * Delete item from a checklist.
     *
     * @param checklist
     * @param checklistItem
     */
    public void deleteItem(Checklist checklist, ChecklistItem checklistItem) {

        checklistItemRepository.delete(checklistItem);

        Optional.ofNullable(checklist.getItems()).ifPresent(items -> items.removeIf(item -> checklistItem.getId().equals(item.getId())));
        checklistRepository.save(checklist);
    }

    /**
     * Update an item from a checklist.
     *
     * @param item
     */
    public void updateItem(ChecklistItem item) {
        checklistItemRepository.save(item);
    }

    private AuthenticatedUserDetails getAuthenticatedUser() {
        return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
