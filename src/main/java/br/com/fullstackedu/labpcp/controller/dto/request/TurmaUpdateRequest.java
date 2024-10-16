package br.com.fullstackedu.labpcp.controller.dto.request;

import jakarta.validation.constraints.AssertTrue;

import java.util.Objects;

public record TurmaUpdateRequest(
    String nome,
    Long id_curso,
    Long id_professor) {

    @AssertTrue(message = "Ao menos uma das seguintes propriedades não deve ser null: [nome, id_curso, id_professor]")
    public boolean isValidRequest() {
        return Objects.nonNull(nome) || Objects.nonNull(id_curso) || Objects.nonNull(id_professor);
    }
}
