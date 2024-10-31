package br.com.fullstackedu.labpcp.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;
import java.util.Objects;
@Schema(description = "Objeto de edição da turma")
public record TurmaUpdateRequest(
    String nome,
    Long id_curso,
    Long id_professor,
    String hora,
    LocalDate dataFim,
    LocalDate dataInicio) {

    @AssertTrue(message = "Ao menos uma das seguintes propriedades não deve ser null: [nome, id_curso, id_professor]")
    public boolean isValidRequest() {
        return Objects.nonNull(nome)
                || Objects.nonNull(id_curso)
                || Objects.nonNull(id_professor)
                || Objects.nonNull(hora)
                || Objects.nonNull(dataFim)
                || Objects.nonNull(dataInicio);
    }
}
