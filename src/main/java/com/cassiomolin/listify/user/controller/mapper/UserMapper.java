package com.cassiomolin.listify.user.controller.mapper;

import com.cassiomolin.listify.user.controller.model.CreateUserDetails;
import com.cassiomolin.listify.user.controller.model.QueryUserDetails;
import com.cassiomolin.listify.user.domain.User;
import org.mapstruct.Mapper;
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
}