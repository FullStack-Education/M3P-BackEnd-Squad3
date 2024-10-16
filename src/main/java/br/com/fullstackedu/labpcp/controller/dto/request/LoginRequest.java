package br.com.fullstackedu.labpcp.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @NotBlank(message = "O login do usuário é obrigatório")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        String senha
){
}
