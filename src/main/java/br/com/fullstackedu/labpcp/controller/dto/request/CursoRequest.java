package br.com.fullstackedu.labpcp.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Objeto de criação do curso")
public record CursoRequest(
        @NotNull(message = "Atributo nome é obrigatório")
        String nome
){
}
