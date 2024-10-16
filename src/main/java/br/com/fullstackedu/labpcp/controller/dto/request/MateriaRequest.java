package br.com.fullstackedu.labpcp.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record MateriaRequest (
        @NotNull(message = "Atributo nome é obrigatório")
        String nome,

        @NotNull(message = "Atributo id_curso é obrigatório")
        Long id_curso
){
}
