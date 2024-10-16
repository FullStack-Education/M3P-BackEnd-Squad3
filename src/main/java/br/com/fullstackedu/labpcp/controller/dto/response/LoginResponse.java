package br.com.fullstackedu.labpcp.controller.dto.response;

import java.time.LocalDateTime;

public record LoginResponse(
        boolean success,
        LocalDateTime timestamp,
        String message,
        String authToken,
        long expirationTime ) {
}
