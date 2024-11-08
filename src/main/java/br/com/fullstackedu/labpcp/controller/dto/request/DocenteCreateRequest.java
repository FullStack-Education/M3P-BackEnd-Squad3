package br.com.fullstackedu.labpcp.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Objeto de criação do docente")
public record DocenteCreateRequest(
        @NotBlank(message = "O nome do Docente é obrigatório")
        String nome,
        @NotNull(message = "O telefone do Docente é obrigatório")
        String telefone,

        @NotNull(message = "O gênero do Docente é obrigatório")
        String genero,

        @NotNull(message = "O estado civil do Docente é obrigatório")
        String estadoCivil,

        @NotNull(message = "A data de nascimento é obrigatória no formato dd/MM/yyyy")
        LocalDate dataNascimento,

        @NotNull(message = "O email é obrigatório")
        String email,

        @NotNull(message = "O CPF é obrigatório")
        String CPF,

        @NotNull(message = "O RG é obrigatório")
        String RG,

        @NotNull(message = "A naturalidade é obrigatória")
        String naturalidade,

        @NotNull(message = "O CEP é obrigatório")
        String cep,

        @NotNull(message = "O logradouro é obrigatório")
        String logradouro,

        @NotNull(message = "O número é obrigatório")
        String numero,

        @NotNull(message = "A cidade é obrigatória")
        String cidade,

        @NotNull(message = "O estado é obrigatório")
        String estado,

        @NotNull(message = "O complemento é obrigatória")
        String complemento,

        Long id_usuario,

        @NotNull(message = "Atributo senha é obrigatório")
        String senha,

        @NotNull(message = "Atributo id_papel é obrigatório")
        Long id_papel,

        @NotNull(message = "Atributo id_materias é obrigatório")
        List<Long> id_materias
) {}
