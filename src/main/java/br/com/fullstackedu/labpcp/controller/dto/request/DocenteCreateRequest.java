package br.com.fullstackedu.labpcp.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Schema(description = "Objeto de criação do docente")
public record DocenteCreateRequest(
        @NotBlank(message = "O nome do Docente é obrigatório")
        String nome,

        @NotNull(message = "o parametro data_entrada é obrigatório no formato dd/MM/yyyy")
        LocalDate data_entrada,

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

        String complemento,

        @NotNull(message = "O ID do Usuario é obrigatório")
        Long id_usuario
) {}
