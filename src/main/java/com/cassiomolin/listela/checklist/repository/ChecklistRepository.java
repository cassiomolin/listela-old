package com.cassiomolin.listela.checklist.repository;

import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistRepository extends MongoRepository<Checklist, String> {

    Optional<Checklist> findByIdAndOwnerId(String id, String ownerId);

    List<Checklist> findAllByOwner(User owner);

    List<Checklist> findAllByOwnerId(String ownerId);
}
