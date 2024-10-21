package br.com.fullstackedu.labpcp.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Objeto de criação do novo usuario")
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
