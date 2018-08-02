package com.cassiomolin.listela.checklist.repository;

import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistRepository extends MongoRepository<Checklist, String> {

    List<Checklist> findAllByOwner(User owner);
}
