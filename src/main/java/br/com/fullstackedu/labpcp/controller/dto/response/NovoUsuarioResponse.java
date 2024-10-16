package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record NovoUsuarioResponse(Boolean success, LocalDateTime timestamp, String message, UsuarioEntity usuarioEntity, HttpStatus httpStatus) {
}
