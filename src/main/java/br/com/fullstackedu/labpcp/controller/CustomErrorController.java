package br.com.fullstackedu.labpcp.controller;


import br.com.fullstackedu.labpcp.controller.dto.response.CustomErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
@Tag(name = "Exceções", description = "Tratamento de exceções")
public class CustomErrorController {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida. A requisição não pode estar vazia.",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))
            ),
    })

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        CustomErrorResponse response = new CustomErrorResponse();
        response.setStatus(ex.getStatusCode().value());
        String message = Arrays.stream(Objects.requireNonNull(ex.getDetailMessageArguments()))
                .filter(arg -> arg != null && !arg.toString().isBlank())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        log.info("ex.getDetailMessageArguments()): {}", message);
        response.setMessage(message);
        response.setTrace(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(response);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida. O formato do corpo da requisição está incorreto ou não pôde ser lido.",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))
            ),
    })

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorResponse> handleInvalidValueException(HttpMessageNotReadableException ex) {
        CustomErrorResponse response = new CustomErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        response.setTrace(Arrays.toString(ex.getStackTrace()));
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(response);
    }
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida. O argumento passado é inválido.",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))
            ),
    })

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        CustomErrorResponse response = new CustomErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        response.setTrace(Arrays.toString(ex.getStackTrace()));
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(response);
    }


}
