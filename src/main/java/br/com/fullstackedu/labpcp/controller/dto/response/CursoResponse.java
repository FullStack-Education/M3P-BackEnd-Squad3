package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CursoResponse(Boolean success, LocalDateTime timestamp, String message, List<CursoEntity> cursoData, HttpStatus httpStatus) {

}