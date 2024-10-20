package br.com.fullstackedu.labpcp.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
@Schema(description = "Objeto de erro padrão")
public class CustomErrorResponse {
    @Schema(description = "Código do erro", example = "404")
    private int status;
    @Schema(description = "Data e hora do evento", example = "2024-10-19T15:00:00")
    private LocalDateTime timestamp;
    @Schema(description = "Mensagem de erro", example = "Não encontrado")
    private String message;
    @Schema(description = "Classe do erro apresentado", example = "NotFoundException")
    private String trace;
}