package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.request.NotaRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.NotaUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.NotaResponse;
import br.com.fullstackedu.labpcp.service.NotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notas")
@Slf4j
@Validated
@RequiredArgsConstructor
@Tag(name = "Nota - CRUD", description = "CRUD de notas")
public class NotaController {
    private final NotaService notaService;

    @Operation(summary = "Cadastrar nova nota")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Nota cadastrada",
                    useReturnTypeSchema = true
            ),
    })

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NotaResponse> insertNota(
            @RequestHeader(name = "Authorization") String authToken,
            @Valid @RequestBody NotaRequest notaRequest
    ) throws Exception {
        log.info("POST /notas ");

        String actualToken = authToken.substring(7);
        NotaResponse response = notaService.insert(notaRequest, actualToken);
        if (response.success()){
            log.info("POST /notas -> Nota cadastrado com sucesso.");
        } else {
            log.info("POST /notas -> Erro ao cadastrar novo nota: [{}].", response.message());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @Operation(summary = "Buscar nota por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nota encontrada",
                    useReturnTypeSchema = true
            ),
    })

    @GetMapping("/{notaId}")
    public ResponseEntity<NotaResponse> getDocenteById(
            @RequestHeader(name = "Authorization") String authToken,
            @Parameter(description = "ID da nota", example = "1")
            @Valid @PathVariable Long notaId) {
        log.info("GET /notas/{} ", notaId);
        String actualToken = authToken.substring(7);
        NotaResponse response = notaService.getById(notaId, actualToken);
        if (response.success()){
            log.info("GET /notas/{} -> OK ", notaId);
        } else {
            log.error("GET /notas/{} -> {} ", notaId, response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Editar nota por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nota alterada",
                    useReturnTypeSchema = true
            ),
    })

    @PutMapping("/{notaId}")
    public ResponseEntity<NotaResponse> updateNota(
            @Parameter(description = "ID da nota", example = "1")
            @PathVariable Long notaId,
            @Valid @RequestBody NotaUpdateRequest notaUpdateRequest,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("PUT /notas");
        String actualToken = authToken.substring(7);
        NotaResponse response = notaService.updateNota(notaId , notaUpdateRequest, actualToken);
        if (response.success()) {
            log.info("PUT /notas -> OK ");
        } else {
            log.error("PUT /notas -> {}", response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Excluir nota por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Nota excluída"
            ),
    })

    @DeleteMapping("/{notaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<NotaResponse> deleteNota(
            @Parameter(description = "ID da nota", example = "1")
            @PathVariable @NotNull(message = "ID da nota é requerido para exclusão") Long notaId,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("DELETE /notas");
        String actualToken = authToken.substring(7);
        NotaResponse response = notaService.deleteNota(notaId, actualToken);
        if (response.success()) {
            log.info("DELETE /notas -> OK ");
        } else {
            log.error("DELETE /notas -> {}", response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }


}
