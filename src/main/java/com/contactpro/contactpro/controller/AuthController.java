package com.contactpro.contactpro.controller;

import org.springframework.web.bind.annotation.*;

import com.contactpro.contactpro.service.UserService;
import com.contactpro.contactpro.dto.LoginRequest;
import com.contactpro.contactpro.dto.LoginResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}