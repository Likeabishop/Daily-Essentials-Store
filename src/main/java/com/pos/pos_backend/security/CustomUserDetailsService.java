package com.pos.pos_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.pos.pos_backend.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) {

        return userRepository
                .findByEmail(username)
                .map(UserPrincipal::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                username
                        )
                );
    }
}