package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record DashboardResponse(Boolean success, LocalDateTime timestamp, String message, Long alunosCount, Long turmasCount, Long docentesCount, HttpStatus httpStatus) {
}
