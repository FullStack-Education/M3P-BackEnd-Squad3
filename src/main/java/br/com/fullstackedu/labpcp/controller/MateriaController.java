package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.request.MateriaRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.MateriaUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.MateriaResponse;
import br.com.fullstackedu.labpcp.service.MateriaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/materias")
public class MateriaController {
    private final MateriaService materiaService;

    @PostMapping()
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

    @GetMapping("/{materiaId}")
    public ResponseEntity<MateriaResponse> getDocenteById(
            @RequestHeader(name = "Authorization") String authToken,
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

    @DeleteMapping("/{materiaId}")
    public ResponseEntity<MateriaResponse> deleteMateria(
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

    @PutMapping("/{materiaId}")
    public ResponseEntity<MateriaResponse> updateMateria(
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
