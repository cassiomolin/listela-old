package com.cassiomolin.listify.task.repository;

import com.cassiomolin.listify.task.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

}
