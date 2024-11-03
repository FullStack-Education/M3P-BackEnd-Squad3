package br.com.fullstackedu.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
@Schema(description = "Objeto de edição do docente")
public record DocenteUpdateRequest(
        String nome,
        LocalDate data_entrada,
        String telefone,
        String genero,
        String estadoCivil,
        LocalDate dataNascimento,
        String email,
        String CPF,
        String RG,
        String naturalidade,
        String cep,
        String logradouro,
        String numero,
        String cidade,
        String estado,
        String complemento,
        Long id_usuario,
        List<Long> id_materias
) {
    @AssertTrue(message = "Ao menos uma das propriedades não deve ser null")
    public boolean isValidRequest() {
        return Objects.nonNull(nome) || Objects.nonNull(data_entrada) || Objects.nonNull(telefone) ||
                Objects.nonNull(genero) || Objects.nonNull(estadoCivil) || Objects.nonNull(dataNascimento) ||
                Objects.nonNull(email) || Objects.nonNull(CPF) || Objects.nonNull(RG) ||
                Objects.nonNull(naturalidade) || Objects.nonNull(cep) || Objects.nonNull(logradouro) ||
                Objects.nonNull(numero) || Objects.nonNull(cidade) || Objects.nonNull(estado) ||
                Objects.nonNull(complemento) || Objects.nonNull(id_usuario);
    }
}
