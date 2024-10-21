package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
@Schema(description = "Objeto de resposta do curso")
public record CursoResponse(Boolean success, LocalDateTime timestamp, String message, List<CursoEntity> cursoData, HttpStatus httpStatus) {

}