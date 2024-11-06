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
//        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data_nascimento,

//        @NotNull(message = "Atributo id_usuario é obrigatório")
        Long id_usuario,

        @NotNull(message = "Atributo id_turma é obrigatório")
        Long id_turma,

        @NotNull(message = "Atributo telefone é obrigatório")
        String telefone,
        @NotNull(message = "Atributo genero é obrigatório")
        String genero,
        @NotNull(message = "Atributo estadoCivil é obrigatório")
        String estadoCivil,
        @NotNull(message = "Atributo email é obrigatório")
        String email,
        @NotNull(message = "Atributo cpf é obrigatório")
        String cpf,
        @NotNull(message = "Atributo rg é obrigatório")
        String rg,
        @NotNull(message = "Atributo naturalidade é obrigatório")
        String naturalidade,

        @NotNull(message = "Atributo senha é obrigatório")
        String senha,

        @NotNull(message = "Atributo id_papel é obrigatório")
        Long id_papel,


        String cep,
        String logadouro,
        String numero,
        String cidade,
        String complemento
){


}
