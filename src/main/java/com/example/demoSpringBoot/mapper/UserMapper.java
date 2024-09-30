package com.example.demoSpringBoot.mapper;

import com.example.demoSpringBoot.dto.request.UserCreationRequest;
import com.example.demoSpringBoot.dto.request.UserUpdateRequest;
import com.example.demoSpringBoot.dto.response.UserResponse;
import com.example.demoSpringBoot.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

// MapStruct annotations. Check in target\classes\...\mapper\UserMapperImpl
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    //@Mapping(source = "firstName", target = "lastName") // Example when we want to map source field to target field
    //@Mapping(target = "lastName", ignore = true) // Example when we don't want to map lastName
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    UserResponse toUserResponse(User user);


}
