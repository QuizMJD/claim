package com.t3h.insuranceclaim.mapper;

import com.t3h.insuranceclaim.dto.RoleDto;
import com.t3h.insuranceclaim.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto toDto(RoleEntity entity);
    RoleEntity toEntity(RoleDto dto);

    default Set<RoleDto> toDtoSet(Set<RoleEntity> roleEntities) {
        return roleEntities.stream().map(this::toDto).collect(Collectors.toSet());
    }
    default Set<RoleEntity> toEntitySet(Set<RoleDto> roleDTOS) {
        return roleDTOS.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
