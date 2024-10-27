package br.com.fullstackedu.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;
@Schema(description = "Objeto de edição da nota")
public record NotaUpdateRequest(
        Long id_aluno,
        Long id_professor,
        Long id_materia,
        Long id_turma,
        String nome,
        Double valor,
        LocalDate data
){
        @AssertTrue(message = "Ao menos uma das seguintes propriedades deve ser informada: [id_aluno, id_professor, id_materia, valor, id_turma, nome, data]")
        public boolean isValidRequest() {
                return Objects.nonNull(id_aluno)
                        || Objects.nonNull(id_professor)
                        || Objects.nonNull(id_materia)
                        || Objects.nonNull(valor)
                        || Objects.nonNull(id_turma)
                        || Objects.nonNull(nome)
                        || Objects.nonNull(data);
        }
}