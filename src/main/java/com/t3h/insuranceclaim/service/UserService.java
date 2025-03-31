package com.t3h.insuranceclaim.service;

import com.t3h.insuranceclaim.dto.UserDto;
import com.t3h.insuranceclaim.entity.UserEntity;
import com.t3h.insuranceclaim.mapper.UserMapper;
import com.t3h.insuranceclaim.repository.UserRepository;
import com.t3h.insuranceclaim.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private MinioService minioService;

    public Page<UserDto> searchUsers(String code, String phone, String createdDate, Pageable pageable) {
        // TODO: Implement search logic
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    public UserDto createUser(UserDto userDto, String base64Image) {
        // Convert base64 to file and upload to MinIO
        String imagePath = minioService.uploadBase64Image(base64Image, "user-avatars");
        
        // Set image path to userDto
        userDto.setPathAvatar(imagePath);
        
        // Convert to entity
        UserEntity userEntity = userMapper.toEntity(userDto);
        
        // Encode password
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        // Set default role as ADMIN
        userEntity.getRoles().add(roleRepository.findByName("ROLE_ADMIN"));
        
        // Save to database
        UserEntity savedEntity = userRepository.save(userEntity);
        
        return userMapper.toDto(savedEntity);
    }
}
