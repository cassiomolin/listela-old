package com.cassiomolin.listela.checklist.controller.mapper;

import com.cassiomolin.listela.checklist.controller.model.CreateChecklistItemDetails;
import com.cassiomolin.listela.checklist.controller.model.QueryChecklistItemDetails;
import com.cassiomolin.listela.checklist.controller.model.UpdateChecklistItemDetails;
import com.cassiomolin.listela.checklist.domain.ChecklistItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Component that maps a {@link ChecklistItem} domain model to API models and vice versa.
 *
 * @author cassiomolin
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChecklistItemMapper {

    ChecklistItem toChecklistItem(CreateChecklistItemDetails createChecklistItemDetails);

    QueryChecklistItemDetails toQueryChecklistItemDetails(ChecklistItem checklistItem);

    List<QueryChecklistItemDetails> toQueryChecklistItemDetails(List<ChecklistItem> checklistItems);

    void updateTask(UpdateChecklistItemDetails updateChecklistItemDetails, @MappingTarget ChecklistItem checklistItem);
}