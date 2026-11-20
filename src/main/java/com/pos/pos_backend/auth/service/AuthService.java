package com.pos.pos_backend.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pos.pos_backend.auth.dto.CurrentUserResponse;
import com.pos.pos_backend.auth.dto.LoginRequest;
import com.pos.pos_backend.auth.dto.LoginResponse;
import com.pos.pos_backend.security.JwtService;
import com.pos.pos_backend.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(
            LoginRequest request
    ) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        UserPrincipal principal =
                (UserPrincipal)
                        authentication.getPrincipal();

        String jwt =
                jwtService.generateToken(
                        principal.getUserId(),
                        principal.getUsername(),
                        principal.getRole(),
                        principal.getBusinessId()
                );

        return new LoginResponse(jwt);
    }

    public CurrentUserResponse currentUser() {

        UserPrincipal principal =
            (UserPrincipal)
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal();

        return new CurrentUserResponse(
            principal.getUserId(),
            principal.getBusinessId(),
            principal.getUsername(),
            principal.getUser().getFullName(),
            principal.getRole()
        );
    }
}