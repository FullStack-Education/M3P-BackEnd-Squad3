package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.request.DocenteCreateRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.DocenteUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.NovoDocenteResponse;
import br.com.fullstackedu.labpcp.service.DocenteService;
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

@RestController
@RequestMapping("/docentes")
@Slf4j
@Validated
@Tag(name = "Docente - CRUD", description = "CRUD de docentes")
public class DocenteController {
    private final DocenteService docenteService;

    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @Operation(summary = "Cadastrar novo docente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Docente cadastrado",
                    useReturnTypeSchema = true
            ),
    })

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NovoDocenteResponse> newDocente(
            @RequestHeader(name = "Authorization") String authToken,
            @Valid @RequestBody DocenteCreateRequest docenteCreateRequest
    ) throws Exception {
        log.info("POST /docentes -> Novo Docente ");
        String actualToken = authToken.substring(7);
        NovoDocenteResponse response = docenteService.novoDocente(docenteCreateRequest, actualToken);
        if (response.success()){
            log.info("POST /docentes -> Docente cadastrado com sucesso.");
        } else {
            log.error("POST /docentes -> Erro ao cadastrar novo Docente: [{}].", response.message());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @Operation(summary = "Buscar docente por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Docente encontrado",
                    useReturnTypeSchema = true
            ),
    })

    @GetMapping("/{docenteId}")
    public ResponseEntity<NovoDocenteResponse> getDocenteById(
            @RequestHeader(name = "Authorization") String authToken,
            @Parameter(description = "ID do docente", example = "1")
            @Valid @PathVariable Long docenteId) {
        log.info("GET /docente/{} ", docenteId);
        String actualToken = authToken.substring(7);
        NovoDocenteResponse response = docenteService.getDocenteById(docenteId, actualToken);
        if (response.success()){
            log.info("GET /docentes/{} -> OK ", docenteId);
        } else {
            log.error("GET /docentes/{} -> 404", docenteId);
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Buscar todos os docentes")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Docentes encontrados",
                    useReturnTypeSchema = true
            ),
    })

    @GetMapping()
    public ResponseEntity<NovoDocenteResponse> getAllDocentes(
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("GET /docentes ");
        String actualToken = authToken.substring(7);
        NovoDocenteResponse response = docenteService.getAllDocentes(actualToken);
        if (response.success()) {
            log.info("GET /docentes -> OK ");
        } else {
            log.error("GET /docentes -> 404");
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Editar docente por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Docente alterado",
                    useReturnTypeSchema = true
            ),
    })

    @PutMapping("/{docenteId}")
    public ResponseEntity<NovoDocenteResponse> updateDocente(
            @Parameter(description = "ID do docente", example = "1")
            @PathVariable Long docenteId,
            @Valid @RequestBody DocenteUpdateRequest docenteUpdateRequest,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("PUT /docentes");
        String actualToken = authToken.substring(7);
        NovoDocenteResponse response = docenteService.updateDocente(docenteId , docenteUpdateRequest, actualToken);
        if (response.success()) {
            log.info("PUT /docentes -> OK ");
        } else {
            log.error("PUT /docentes -> 400");
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @Operation(summary = "Excluir docente por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Docente excluído"
            ),
    })

    @DeleteMapping("/{docenteId}")
    public ResponseEntity<NovoDocenteResponse> deleteDocente(
            @Parameter(description = "ID do docente", example = "1")
            @PathVariable @NotNull(message = "ID de Docente é requerido para excluão") Long docenteId,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("DELETE /docentes");
        String actualToken = authToken.substring(7);
        NovoDocenteResponse response = docenteService.deleteDocente(docenteId, actualToken);
        if (response.success()) {
            log.info("DELETE /docentes -> OK ");
        } else {
            log.error("DELETE /docentes -> 400");
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Excluir todos os docentes")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Docentes excluídos"
            ),
    })

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteAllDocentes(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rota DELETE disponível apenas para registos individuais. Ex: /docentes/ID");
    }

    @Operation(summary = "Adicionar Materia ao Docente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Docente alterado"
            ),
    })
    @PostMapping("/{docenteId}/materias/{materiaId}")
    public ResponseEntity<NovoDocenteResponse> adicionarMateria(
            @RequestHeader(name = "Authorization") String authToken,
            @Parameter(description = "ID do docente", example = "1")
            @PathVariable Long docenteId,
            @Parameter(description = "ID do da materia", example = "1")
            @PathVariable Long materiaId) {
            log.info("POST /docentes/{}/materias/{} ", docenteId ,materiaId);
            String actualToken = authToken.substring(7);
            NovoDocenteResponse response = docenteService.addMateriaDocente(materiaId, docenteId, actualToken);
            if (response.success()) {
                log.info("PUT /docentes -> OK ");
            } else {
                log.error("PUT /docentes -> 400");
            }
            return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @DeleteMapping("/{docenteId}/materias/{materiaId}")
    public ResponseEntity<NovoDocenteResponse> removerMateria(
            @RequestHeader(name = "Authorization") String authToken,
            @Parameter(description = "ID do docente", example = "1")
            @PathVariable Long docenteId,
            @Parameter(description = "ID da materia", example = "1")
            @PathVariable Long materiaId) {
        log.info("DELETE /{}/materias/{}", docenteId, materiaId);
        String actualToken = authToken.substring(7);
        NovoDocenteResponse response = docenteService.deleteMateriaDocente(materiaId, docenteId, authToken);
        if (response.success()) {
            log.info("DELETE /docentes -> OK ");
        } else {
            log.error("DELETE /docentes -> 400");
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}
