package com.cassiomolin.listela.checklist.repository;

import com.cassiomolin.listela.checklist.domain.Checklist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistRepository extends MongoRepository<Checklist, String> {

    Optional<Checklist> findByIdAndOwnerId(String checklistId, String userId);

    List<Checklist> findAllByOwnerId(String userId);

    Optional<Checklist> deleteByIdAndOwnerId(String checklistId, String userId);
}
