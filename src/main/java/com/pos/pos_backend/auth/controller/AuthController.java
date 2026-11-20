package com.pos.pos_backend.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos.pos_backend.auth.dto.CurrentUserResponse;
import com.pos.pos_backend.auth.dto.LoginRequest;
import com.pos.pos_backend.auth.dto.LoginResponse;
import com.pos.pos_backend.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(

            @Valid
            @RequestBody
            LoginRequest request

    ) {

        return authService.login(request);
    }

    @GetMapping("/me")
    public CurrentUserResponse me() {

        return authService.currentUser();
    }

    //@PreAuthorize("hasRole('OWNER')")
}