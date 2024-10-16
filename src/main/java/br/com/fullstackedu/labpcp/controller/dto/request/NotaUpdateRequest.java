package br.com.fullstackedu.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public record NotaUpdateRequest(
        Long id_aluno,
        Long id_professor,
        Long id_materia,
        Double valor,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data
){
        @AssertTrue(message = "Ao menos uma das seguintes propriedades deve ser informada: [id_aluno, id_professor, id_materia, valor, data]")
        public boolean isValidRequest() {
                return Objects.nonNull(id_aluno)
                        || Objects.nonNull(id_professor)
                        || Objects.nonNull(id_materia)
                        || Objects.nonNull(valor)
                        || Objects.nonNull(data);
        }
}