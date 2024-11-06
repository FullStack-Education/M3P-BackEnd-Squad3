package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.TurmaEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
@Schema(description = "Objeto de resposta da turma")
public record TurmaCreateResponse(Boolean success, LocalDateTime timestamp, String message, List<TurmaEntity> turmaData, HttpStatus httpStatus) {

}