package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.request.MateriaRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.MateriaUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.MateriaResponse;
import br.com.fullstackedu.labpcp.service.MateriaService;
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

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/materias")
@Tag(name = "Materia - CRUD", description = "CRUD de materias")
public class MateriaController {
    private final MateriaService materiaService;

    @Operation(summary = "Cadastrar nova materia")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Materia cadastrada",
                    useReturnTypeSchema = true
            ),
    })

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MateriaResponse> insertMateria(
            @RequestHeader(name = "Authorization") String authToken,
            @Valid @RequestBody MateriaRequest materiaRequest
    ) throws Exception {
        log.info("POST /materias ");

        String actualToken = authToken.substring(7);
        MateriaResponse response = materiaService.insert(materiaRequest, actualToken);
        if (response.success()){
            log.info("POST /materias -> Materia cadastrado com sucesso.");
        } else {
            log.info("POST /materias -> Erro ao cadastrar novo materia: [{}].", response.message());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Buscar materia por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Materia encontrada",
                    useReturnTypeSchema = true
            ),
    })

    @GetMapping("/{materiaId}")
    public ResponseEntity<MateriaResponse> getDocenteById(
            @RequestHeader(name = "Authorization") String authToken,
            @Parameter(description = "ID da materia", example = "1")
            @Valid @PathVariable Long materiaId) {
        log.info("GET /materias/{} ", materiaId);
        String actualToken = authToken.substring(7);
        MateriaResponse response = materiaService.getById(materiaId, actualToken);
        if (response.success()){
            log.info("GET /materias/{} -> OK ", materiaId);
        } else {
            log.error("GET /materias/{} -> {} ", materiaId, response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Excluir materia por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Materia excluída"
            ),
    })

    @DeleteMapping("/{materiaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<MateriaResponse> deleteMateria(
            @Parameter(description = "ID da materia", example = "1")
            @PathVariable @NotNull(message = "ID da materia é requerido para exclusão") Long materiaId,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("DELETE /materias");
        String actualToken = authToken.substring(7);
        MateriaResponse response = materiaService.deleteMateria(materiaId, actualToken);
        if (response.success()) {
            log.info("DELETE /materias -> OK ");
        } else {
            log.error("DELETE /materias -> {}", response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Editar docente por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Materia alterada",
                    useReturnTypeSchema = true
            ),
    })

    @PutMapping("/{materiaId}")
    public ResponseEntity<MateriaResponse> updateMateria(
            @Parameter(description = "ID da materia", example = "1")
            @PathVariable Long materiaId,
            @Valid @RequestBody MateriaUpdateRequest materiaUpdateRequest,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("PUT /materias");
        String actualToken = authToken.substring(7);
        MateriaResponse response = materiaService.update(materiaId , materiaUpdateRequest, actualToken);
        if (response.success()) {
            log.info("PUT /materias -> OK ");
        } else {
            log.error("PUT /materias -> {}", response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

}
