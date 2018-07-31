package com.cassiomolin.listify.checklist.controller.mapper;

import com.cassiomolin.listify.checklist.controller.model.CreateChecklistDetails;
import com.cassiomolin.listify.checklist.controller.model.QueryChecklistDetails;
import com.cassiomolin.listify.checklist.domain.Checklist;
import com.cassiomolin.listify.task.controller.model.CreateTaskDetails;
import com.cassiomolin.listify.task.controller.model.QueryTaskDetails;
import com.cassiomolin.listify.task.controller.model.UpdateTaskDetails;
import com.cassiomolin.listify.task.domain.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Component that maps a {@link Checklist} domain model to API models and vice versa.
 *
 * @author cassiomolin
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChecklistMapper {

    Checklist toChecklist(CreateChecklistDetails createChecklistDetails);

    QueryChecklistDetails toQueryChecklistDetails(Checklist checklist);
}