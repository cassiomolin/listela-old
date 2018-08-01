package com.cassiomolin.listela.checklist.repository;

import com.cassiomolin.listela.checklist.domain.Checklist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends MongoRepository<Checklist, String> {

}
