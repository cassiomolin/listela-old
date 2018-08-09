package com.cassiomolin.listela.checklist.repository;

import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.checklist.domain.ChecklistItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChecklistItemRepository extends MongoRepository<ChecklistItem, String> {

    Optional<ChecklistItem> findByIdAndChecklistId(String checklistId, String itemId);
}
