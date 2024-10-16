package br.com.fullstackedu.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;
import java.util.Objects;

public record AlunoUpdateRequest(
        String nome,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data_nascimento,

        Long id_usuario,

        Long id_turma
){
        @AssertTrue(message = "Ao menos uma das seguintes propriedades deve ser informada: [nome, data_nascimento, id_usuario, id_turma]")
        public boolean isValidRequest() {
                return Objects.nonNull(nome)
                        || Objects.nonNull(data_nascimento)
                        || Objects.nonNull(id_usuario)
                        || Objects.nonNull(id_turma);
        }
}
