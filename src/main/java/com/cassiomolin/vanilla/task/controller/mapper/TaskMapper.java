package com.cassiomolin.vanilla.task.controller.mapper;

import com.cassiomolin.vanilla.task.controller.model.CreateTaskDetails;
import com.cassiomolin.vanilla.task.controller.model.QueryTaskResult;
import com.cassiomolin.vanilla.task.controller.model.UpdateTaskDetails;
import com.cassiomolin.vanilla.task.domain.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Component that maps a {@link Task} domain model to API models and vice versa.
 *
 * @author cassiomolin
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task  toTask(CreateTaskDetails createTaskDetails);

    QueryTaskResult toQueryTaskResult(Task task);

    List<QueryTaskResult> toQueryTaskResults(List<Task> tasks);

    void updateTask(UpdateTaskDetails updateTaskDetails, @MappingTarget Task task);
}