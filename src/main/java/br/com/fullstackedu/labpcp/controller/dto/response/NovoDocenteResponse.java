package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.DocenteEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record NovoDocenteResponse(Boolean success, LocalDateTime timestamp, String message, List<DocenteEntity> docenteData, HttpStatus httpStatus) {

}