package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.DocenteEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
@Schema(description = "Objeto de resposta do novo docente")
public record NovoDocenteResponse(Boolean success, LocalDateTime timestamp, String message, List<DocenteEntity> docenteData, HttpStatus httpStatus) {

}