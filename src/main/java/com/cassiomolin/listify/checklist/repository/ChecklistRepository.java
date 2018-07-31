package com.cassiomolin.listify.checklist.repository;

import com.cassiomolin.listify.checklist.domain.Checklist;
import com.cassiomolin.listify.task.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends MongoRepository<Checklist, String> {

}
