package br.com.fullstackedu.labpcp.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record CursoRequest(
        @NotNull(message = "Atributo nome é obrigatório")
        String nome
){
}
