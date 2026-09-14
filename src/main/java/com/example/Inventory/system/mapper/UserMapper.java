package com.example.Inventory.system.mapper;

import com.example.Inventory.system.dto.response.UserResponse;
import com.example.Inventory.system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}
