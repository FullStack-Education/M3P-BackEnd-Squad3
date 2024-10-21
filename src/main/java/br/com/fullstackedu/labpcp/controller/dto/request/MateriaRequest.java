package br.com.fullstackedu.labpcp.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Objeto de criação da materia")
public record MateriaRequest (
        @NotNull(message = "Atributo nome é obrigatório")
        String nome,

        @NotNull(message = "Atributo id_curso é obrigatório")
        Long id_curso
){
}
