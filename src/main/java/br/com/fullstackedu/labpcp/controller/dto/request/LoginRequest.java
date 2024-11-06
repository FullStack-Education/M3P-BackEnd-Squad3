package br.com.fullstackedu.labpcp.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
@Schema(description = "Objeto de submissão do login")
public record LoginRequest (
        @NotBlank(message = "O login do usuário é obrigatório")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        String senha
){
}
