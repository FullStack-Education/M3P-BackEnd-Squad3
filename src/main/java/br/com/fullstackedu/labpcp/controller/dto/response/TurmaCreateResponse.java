package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.TurmaEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TurmaCreateResponse(Boolean success, LocalDateTime timestamp, String message, List<TurmaEntity> turmaData, HttpStatus httpStatus) {

}