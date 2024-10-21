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
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data_entrada,

        @NotNull(message = "O ID do Usuario é obrigatório")
        Long id_usuario
) {
}
