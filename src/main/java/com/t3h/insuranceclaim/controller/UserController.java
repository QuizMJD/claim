package com.t3h.insuranceclaim.controller;

import com.t3h.insuranceclaim.dto.UserDto;
import com.t3h.insuranceclaim.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String createdDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<UserDto> users = userService.searchUsers(code, phone, createdDate, pageRequest);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(
            @RequestBody UserDto userDto,
            @RequestParam(required = false) String base64Image) {
        
        UserDto createdUser = userService.createUser(userDto, base64Image);
        return ResponseEntity.ok(createdUser);
    }
} 