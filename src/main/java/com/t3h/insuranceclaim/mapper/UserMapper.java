package com.t3h.insuranceclaim.mapper;

import com.t3h.insuranceclaim.dto.UserDto;
import com.t3h.insuranceclaim.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "createdDate", expression = "java(entity.getCreatedDate().toString())")
    UserDto toDto(UserEntity entity);

    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserDto dto);
}
