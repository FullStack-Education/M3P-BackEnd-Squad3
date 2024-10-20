package br.com.fullstackedu.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;
import java.util.Objects;
@Schema(description = "Objeto de edição do docente")
public record DocenteUpdateRequest(
    String nome,

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate data_entrada,

    Long id_usuario

){
    @AssertTrue(message = "Ao menos uma das seguintes propriedades não deve ser null: [nome, data_entrada,id_usuario]")
    public boolean isValidRequest() {
        return Objects.nonNull(nome) || Objects.nonNull(data_entrada) || Objects.nonNull(id_usuario);
    }
}
