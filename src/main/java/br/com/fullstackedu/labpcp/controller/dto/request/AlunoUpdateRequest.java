package br.com.fullstackedu.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;
import java.util.Objects;

@Schema(description = "Objeto de edição do aluno")
public record AlunoUpdateRequest(
        String nome,

//        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data_nascimento,

        Long id_usuario,

        Long id_turma,
        String telefone,
        String genero,
        String estadoCivil,
        String email,
        String cpf,
        String rg,
        String naturalidade,
        String cep,
        String logadouro,
        String numero,
        String cidade,
        String complemento
){
        @AssertTrue(message = "Ao menos uma das seguintes propriedades deve ser informada: [nome, data_nascimento, id_usuario, id_turma]")
        public boolean isValidRequest() {
                return Objects.nonNull(nome)
                        || Objects.nonNull(data_nascimento)
                        || Objects.nonNull(id_usuario)
                        || Objects.nonNull(telefone)
                        || Objects.nonNull(genero)
                        || Objects.nonNull(estadoCivil)
                        || Objects.nonNull(email)
                        || Objects.nonNull(cpf)
                        || Objects.nonNull(rg)
                        || Objects.nonNull(naturalidade)
                        || Objects.nonNull(cep)
                        || Objects.nonNull(logadouro)
                        || Objects.nonNull(numero)
                        || Objects.nonNull(cidade)
                        || Objects.nonNull(complemento);
        }
}
