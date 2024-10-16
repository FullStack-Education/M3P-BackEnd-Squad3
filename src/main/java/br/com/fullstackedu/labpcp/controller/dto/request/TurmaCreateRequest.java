package br.com.fullstackedu.labpcp.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TurmaCreateRequest(
        @NotBlank(message = "O nome da turma é obrigatório")
        String nome,

        @NotNull(message = "O ID do Curso é obrigatório")
        Long id_curso,

        Long id_professor
) {
}
