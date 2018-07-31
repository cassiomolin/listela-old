package com.cassiomolin.listify.checklist.service;

import com.cassiomolin.listify.checklist.domain.Checklist;
import com.cassiomolin.listify.checklist.repository.ChecklistRepository;
import com.cassiomolin.listify.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistRepository checklistRepository;

    public Checklist createChecklist(Checklist checklist) {
        return checklistRepository.insert(checklist);
    }

    public Optional<Checklist> findById(String id) {
        return checklistRepository.findById(id);
    }
}
