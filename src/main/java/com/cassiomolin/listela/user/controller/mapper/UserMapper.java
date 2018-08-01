package com.cassiomolin.listela.user.controller.mapper;

import com.cassiomolin.listela.checklist.controller.model.UpdateChecklistItemDetails;
import com.cassiomolin.listela.checklist.domain.ChecklistItem;
import com.cassiomolin.listela.user.controller.model.UpdateUserDetails;
import com.cassiomolin.listela.user.domain.User;
import com.cassiomolin.listela.user.controller.model.CreateUserDetails;
import com.cassiomolin.listela.user.controller.model.QueryUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Component that maps a {@link User} domain model to API models and vice versa.
 *
 * @author cassiomolin
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUser(CreateUserDetails createUserDetails);

    QueryUserDetails toQueryUserDetails(User user);

    void updateUser(UpdateUserDetails updateUserDetails, @MappingTarget User user);
}