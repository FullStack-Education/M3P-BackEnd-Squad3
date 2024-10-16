package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.MateriaEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record MateriaResponse(Boolean success, LocalDateTime timestamp, String message, List<MateriaEntity> materiaData, HttpStatus httpStatus) {

}