package com.pos.pos_backend.auth.dto;

public record CurrentUserResponse(

        Long id,

        Long businessId,

        String email,

        String fullName,

        String role
) {
}