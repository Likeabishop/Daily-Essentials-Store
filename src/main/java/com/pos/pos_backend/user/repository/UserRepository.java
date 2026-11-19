package com.pos.pos_backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.pos_backend.user.entity.User;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}