package com.t3h.insuranceclaim.service;

import com.t3h.insuranceclaim.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto getUserByUsername(String username);
    UserDto getProfileUser();
    Page<UserDto> searchUsers(String code, String phone, String createdDate, Pageable pageable);
    UserDto createUser(UserDto userDto, String base64Image);
} 