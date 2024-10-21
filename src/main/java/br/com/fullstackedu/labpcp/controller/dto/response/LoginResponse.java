package br.com.fullstackedu.labpcp.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(description = "Objeto de resposta do login")
public record LoginResponse(
        boolean success,
        LocalDateTime timestamp,
        String message,
        String authToken,
        long expirationTime ) {
}
