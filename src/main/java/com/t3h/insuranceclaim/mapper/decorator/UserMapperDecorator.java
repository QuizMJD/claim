package com.t3h.insuranceclaim.mapper.decorator;

import com.t3h.insuranceclaim.dto.UserDto;
import com.t3h.insuranceclaim.entity.UserEntity;
import com.t3h.insuranceclaim.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class UserMapperDecorator implements UserMapper {
    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;

    @Override
    public UserDto toDto(UserEntity userEntity) {
        UserDto dto = delegate.toDto(userEntity);
        if (dto != null) {
            // Add any additional mapping logic here
        }
        return dto;
    }

    @Override
    public UserEntity toEntity(UserDto userDto) {
        UserEntity entity = delegate.toEntity(userDto);
        if (entity != null) {
            // Add any additional mapping logic here
        }
        return entity;
    }
}
