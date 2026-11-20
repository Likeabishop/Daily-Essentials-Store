package com.pos.pos_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pos.pos_backend.business.entity.Business;
import com.pos.pos_backend.business.repository.BusinessRepository;
import com.pos.pos_backend.user.entity.Role;
import com.pos.pos_backend.user.entity.User;
import com.pos.pos_backend.user.repository.UserRepository;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DataInitializer
        implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) {
            return;
        }

        Business business =
                businessRepository.save(
                        Business.builder()
                                .name(
                                        "Daily Essentials Store"
                                )
                                .active(true)
                                .build()
                );

        User owner =
                User.builder()
                        .business(business)
                        .email(
                             "owner@dailyessentials.co.za"
                        )
                        .fullName(
                             "Store Owner"
                        )
                        .passwordHash(
                             passwordEncoder.encode(
                                 "ChangeMe123!"
                             )
                        )
                        .role(Role.OWNER)
                        .active(true)
                        .build();

        userRepository.save(owner);
    }
}