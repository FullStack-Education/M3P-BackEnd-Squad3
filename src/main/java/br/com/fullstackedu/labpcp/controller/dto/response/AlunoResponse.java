package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.AlunoEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record AlunoResponse(Boolean success, LocalDateTime timestamp, String message, List<AlunoEntity> alunoData, HttpStatus httpStatus) {

}