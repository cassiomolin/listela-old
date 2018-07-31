package com.cassiomolin.listify.task.service;

import com.cassiomolin.listify.task.repository.TaskRepository;
import com.cassiomolin.listify.task.domain.Task;
import com.cassiomolin.listify.task.domain.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Create a task.
     *
     * @param task
     * @return
     */
    public Task createTask(@NotNull Task task) {
        task.setCompleted(false);
        return taskRepository.save(task);
    }

    /**
     * Find all tasks.
     *
     * @return
     */
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Find tasks using a filter.
     *
     * @param filter
     * @return
     */
    public List<Task> findTasks(@NotNull TaskFilter filter) {

        Task task = new Task();
        task.setDescription(filter.getDescription());
        task.setCompleted(filter.getCompleted());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("description", match -> match.contains().ignoreCase());
        Example<Task> example = Example.of(task, matcher);

        Sort sort = new Sort(Sort.Direction.ASC, "createdDate");

        return taskRepository.findAll(example, sort);
    }

    /**
     * Delete tasks.
     *
     * @param completed
     * @return
     */
    public void deleteTasks(Boolean completed) {

        if (completed == null) {
            taskRepository.deleteAll();
        } else {
            Task task = new Task();
            task.setCompleted(completed);
            Example<Task> example = Example.of(task);
            taskRepository.deleteAll(taskRepository.findAll(example));
        }
    }

    /**
     * Find a task by id.
     *
     * @param taskId
     * @return
     */
    public Optional<Task> findTask(@NotNull String taskId) {
        return taskRepository.findById(taskId);
    }

    /**
     * Update a task.
     *
     * @param task
     */
    public void updateTask(@NotNull Task task) {
        taskRepository.save(task);
    }

    /**
     * Delete a task.
     *
     * @param taskId
     */
    public void deleteTask(@NotNull String taskId) {
        taskRepository.deleteById(taskId);
    }
}
