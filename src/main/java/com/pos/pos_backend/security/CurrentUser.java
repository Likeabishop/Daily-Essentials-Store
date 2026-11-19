package com.pos.pos_backend.security;

import lombok.Builder;

@Builder
public record CurrentUser(
        Long userId,
        Long businessId,
        String email,
        String role
) {
}