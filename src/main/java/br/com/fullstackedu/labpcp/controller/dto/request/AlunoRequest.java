package br.com.fullstackedu.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Objeto de criação do aluno")
public record AlunoRequest (
        @NotNull(message = "Atributo nome é obrigatório")
        String nome,

        @NotNull(message = "Atributo data_nascimento é obrigatório no formato dd/MM/yyyy")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data_nascimento,

        @NotNull(message = "Atributo id_usuario é obrigatório")
        Long id_usuario,

        Long id_turma
){
}
