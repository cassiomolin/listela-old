package com.cassiomolin.listela.checklist.service;

import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.user.domain.User;
import com.cassiomolin.listela.user.repository.UserRepository;
import com.cassiomolin.listela.checklist.repository.ChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChecklistService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

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
}
