package com.pos.pos_backend.common.response;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record ErrorResponse(
        String code,
        String message,
        List<String> details,
        OffsetDateTime timestamp
) {
}