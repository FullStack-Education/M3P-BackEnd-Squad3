package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.request.DocenteCreateRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.DocenteUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.NovoDocenteResponse;
import br.com.fullstackedu.labpcp.service.DocenteService;
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
public class DocenteController {
    private final DocenteService docenteService;

    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @PostMapping()
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

    @GetMapping("/{docenteId}")
    public ResponseEntity<NovoDocenteResponse> getDocenteById(
            @RequestHeader(name = "Authorization") String authToken,
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

    @PutMapping("/{docenteId}")
    public ResponseEntity<NovoDocenteResponse> updateDocente(
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

    @DeleteMapping("/{docenteId}")
    public ResponseEntity<NovoDocenteResponse> deleteDocente(
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

    @DeleteMapping()
    public ResponseEntity<String> deleteAllDocentes(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rota DELETE disponível apenas para registos individuais. Ex: /docentes/ID");
    }
}
