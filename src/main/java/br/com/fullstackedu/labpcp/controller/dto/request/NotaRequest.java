package br.com.fullstackedu.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
@Schema(description = "Objeto de criação da nota")
public record NotaRequest (
        @NotNull(message = "Atributo id_aluno é obrigatório")
        Long id_aluno,

        @NotNull(message = "Atributo id_professor é obrigatório")
        Long id_professor,

        @NotNull(message = "Atributo id_materia é obrigatório")
        Long id_materia,

        @NotNull(message = "Atributo id_materia é obrigatório")
        Double valor,

        @NotNull(message = "Atributo data é obrigatório no formato dd/MM/yyyy")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data
){
}
