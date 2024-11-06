package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Schema(description = "Objeto de resposta do novo usuario")
public record NovoUsuarioResponse(Boolean success, LocalDateTime timestamp, String message, UsuarioEntity usuarioEntity, HttpStatus httpStatus) {
}
