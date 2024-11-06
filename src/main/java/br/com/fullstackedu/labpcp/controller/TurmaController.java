package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.request.TurmaCreateRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.TurmaUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.TurmaCreateResponse;
import br.com.fullstackedu.labpcp.service.TurmaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/turmas")
@Slf4j
@Validated
@Tag(name = "Turma - CRUD", description = "CRUD de turmas")
public class TurmaController {
    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @Operation(summary = "Cadastrar nova turma")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Turma cadastrada",
                    useReturnTypeSchema = true
            ),
    })

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TurmaCreateResponse> newTurma(
            @RequestHeader(name = "Authorization") String authToken,
            @Valid @RequestBody TurmaCreateRequest turmaCreateRequest
    ) throws Exception {
        log.info("POST /turmas ");
        String actualToken = authToken.substring(7);
        TurmaCreateResponse response = turmaService.novaTurma(turmaCreateRequest, actualToken);
        if (response.success()){
            log.info("POST /turmas -> Turma cadastrada com sucesso.");
        } else {
            log.error("POST /turmas -> Erro ao cadastrar nova Turma: [{}].", response.message());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @Operation(summary = "Buscar turma por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Turma encontrada",
                    useReturnTypeSchema = true
            ),
    })

    @GetMapping("/{turmaId}")
    public ResponseEntity<TurmaCreateResponse> getTurmaById(
            @RequestHeader(name = "Authorization") String authToken,
            @Parameter(description = "ID da turma", example = "1")
            @Valid @PathVariable Long turmaId) {
        log.info("GET /turmas/{} ", turmaId);
        String actualToken = authToken.substring(7);
        TurmaCreateResponse response = turmaService.getTurmaById(turmaId, actualToken);
        if (response.success()){
            log.info("GET /turmas/{} -> OK ", turmaId);
        } else {
            log.error("GET /turmas/{} -> 500", turmaId);
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @Operation(summary = "Buscar todas as turmas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Turmas encontradas",
                    useReturnTypeSchema = true
            ),
    })
    @GetMapping()
    public ResponseEntity<TurmaCreateResponse> getAllTurmas(
            @RequestHeader(name = "Authorization") String authToken) throws Exception {
        log.info("GET /turmas ");
        String actualToken = authToken.substring(7);
        TurmaCreateResponse response = turmaService.getAllTurmas(actualToken);
        if (response.success()){
            log.info("GET /turmas -> OK ");
        } else {
            log.error("GET /turmas -> 500");
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @Operation(summary = "Editar turma por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Turma alterada",
                    useReturnTypeSchema = true
            ),
    })

    @PutMapping("/{turmaId}")
    public ResponseEntity<TurmaCreateResponse> updateTurma(
            @Parameter(description = "ID da turma", example = "1")
            @PathVariable Long turmaId,
            @Valid @RequestBody TurmaUpdateRequest turmaUpdateRequest,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("PUT /turmas");
        String actualToken = authToken.substring(7);
        TurmaCreateResponse response = turmaService.updateTurma(turmaId , turmaUpdateRequest, actualToken);
        if (response.success()) {
            log.info("PUT /turmas -> OK ");
        } else {
            log.error("PUT /turmas -> 500");
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Excluir turma por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Nota excluída"
            ),
    })

    @DeleteMapping("/{turmaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<TurmaCreateResponse> deleteTurma(
            @Parameter(description = "ID da turma", example = "1")
            @PathVariable @NotNull(message = "ID da turma é requerido para exclusão") Long turmaId,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("DELETE /turmas");
        String actualToken = authToken.substring(7);
        TurmaCreateResponse response = turmaService.deleteTurma(turmaId, actualToken);
        if (response.success()) {
            log.info("DELETE /turmas -> OK ");
        } else {
            log.error("DELETE /turmas -> 500");
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}
