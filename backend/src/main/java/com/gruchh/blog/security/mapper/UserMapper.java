package com.gruchh.blog.security.mapper;

import com.gruchh.blog.security.dto.UserSummaryDto;
import com.gruchh.blog.security.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserSummaryDto toSummaryDto(User user);
}