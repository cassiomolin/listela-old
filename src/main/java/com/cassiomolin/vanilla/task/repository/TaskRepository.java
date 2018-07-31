package com.cassiomolin.vanilla.task.repository;

import com.cassiomolin.vanilla.task.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

}
