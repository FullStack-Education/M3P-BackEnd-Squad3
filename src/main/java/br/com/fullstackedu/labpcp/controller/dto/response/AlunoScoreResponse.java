package br.com.fullstackedu.labpcp.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
@Schema(description = "Objeto de resposta da média do aluno")
public record AlunoScoreResponse(
        Boolean success,
        LocalDateTime timestamp,
        String message,
        List<AlunoScoreDTO> alunoScoreData,
        HttpStatus httpStatus
)
{
    public static AlunoScoreResponse createErrorResponse(HttpStatus status, String message) {
        return new AlunoScoreResponse(
                false,
                LocalDateTime.now(),
                message,
                null,
                status
        );
    }

    public static AlunoScoreResponse createSuccessResponse(HttpStatus status, String message, List<AlunoScoreDTO> data) {
        return new AlunoScoreResponse(
                true,
                LocalDateTime.now(),
                message,
                data,
                status
        );
    }
}