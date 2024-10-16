package br.com.fullstackedu.labpcp.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NovoUsuarioRequest (
        String nome,

        @NotBlank(message = "O login do usuário é obrigatório")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        String senha,

        @NotNull(message = "O ID do papel é obrigatório")
        Long idPapel
){
}
