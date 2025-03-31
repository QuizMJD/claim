package com.t3h.insuranceclaim.service.impl;

import com.t3h.insuranceclaim.dto.UserDto;
import com.t3h.insuranceclaim.entity.UserEntity;
import com.t3h.insuranceclaim.mapper.UserMapper;
import com.t3h.insuranceclaim.repository.UserRepository;
import com.t3h.insuranceclaim.repository.RoleRepository;
import com.t3h.insuranceclaim.service.UserService;
import com.t3h.insuranceclaim.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private MinioService minioService;

    @Override
    public UserDto getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        return userMapper.toDto(userEntity);
    }

    @Override
    public UserDto getProfileUser() {
        // TODO: Implement get current user profile
        return null;
    }

    @Override
    public Page<UserDto> searchUsers(String code, String phone, String createdDate, Pageable pageable) {
        // TODO: Implement search logic
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    public UserDto createUser(UserDto userDto, String base64Image) {
        // Convert base64 to file and upload to MinIO
        String imagePath = minioService.uploadBase64Image(base64Image, "user-avatars");
        
        // Set image path to userDto
        userDto.setPathAvatar(imagePath);
        
        // Convert to entity
        UserEntity userEntity = userMapper.toEntity(userDto);
        
        // Set default role as ADMIN
        userEntity.getRoles().add(roleRepository.findByName("ROLE_ADMIN"));
        
        // Save to database
        UserEntity savedEntity = userRepository.save(userEntity);
        
        return userMapper.toDto(savedEntity);
    }
}
