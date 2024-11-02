package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.request.AlunoRequest;

import br.com.fullstackedu.labpcp.controller.dto.request.AlunoUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.*;

import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.service.AlunoService;
import br.com.fullstackedu.labpcp.service.NotaService;
import br.com.fullstackedu.labpcp.service.PapelService;
import br.com.fullstackedu.labpcp.service.UsuarioService;
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

import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/alunos")
@Slf4j
@Validated
@RequiredArgsConstructor
@Tag(name = "Aluno - CRUD", description = "CRUD de alunos")
public class AlunoController {
    private final AlunoService alunoService;
    private final NotaService notaService;
    private final UsuarioService usuarioService;
    private final PapelService papelService;

    @Operation(summary = "Cadastrar novo aluno")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Aluno cadastrado",
                    useReturnTypeSchema = true
            ),
    })

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AlunoResponse> insertAluno(
            @RequestHeader(name = "Authorization") String authToken,
            @Valid @RequestBody AlunoRequest alunoRequest
    ) throws Exception {
        log.info("POST /alunos ");

        String actualToken = authToken.substring(7);
        UsuarioEntity newUser = new UsuarioEntity();
        newUser.setNome(alunoRequest.nome());
        newUser.setLogin(alunoRequest.email());
        newUser.setSenha(alunoRequest.senha());
        newUser.setPapel(
                papelService.getPapelById(alunoRequest.id_papel())
        );

        NovoUsuarioResponse novo = usuarioService.novoUsuario(newUser, actualToken);
        if(!novo.success()){
            String errMessage = "Erro ao cadastrar aluno: dados ja cadastrados";
            log.error(errMessage);
            return ResponseEntity.status(novo.httpStatus()).body(new AlunoResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.NOT_FOUND));
        }

        AlunoRequest novoAlun0 = new AlunoRequest(
                alunoRequest.nome(),
                alunoRequest.data_nascimento(),
                novo.usuarioEntity().getId(),
                alunoRequest.id_turma(),
                alunoRequest.telefone(),
                alunoRequest.genero(),
                alunoRequest.estadoCivil(),
                alunoRequest.email(),
                alunoRequest.cpf(),
                alunoRequest.rg(),
                alunoRequest.naturalidade(),
                alunoRequest.senha(),
                alunoRequest.id_papel(),
                alunoRequest.cep(),
                alunoRequest.logadouro(),
                alunoRequest.numero(),
                alunoRequest.cidade(),
                alunoRequest.complemento()
                );

        AlunoResponse response = alunoService.insertAluno(novoAlun0, actualToken);
        if (response.success()){
            log.info("POST /alunos -> Aluno cadastrado com sucesso.");
        } else {
            log.info("POST /alunos -> Erro ao cadastrar novo aluno: [{}].", response.message());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Buscar aluno por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Aluno encontrado",
                    useReturnTypeSchema = true
            ),
    })

    @GetMapping("/{alunoId}")
    public ResponseEntity<AlunoResponse> getById(
            @RequestHeader(name = "Authorization") String authToken,
            @Parameter(description = "ID do aluno", example = "1")
            @Valid @PathVariable Long alunoId) {
        log.info("GET /alunos/{} ", alunoId);
        String actualToken = authToken.substring(7);
        AlunoResponse response = alunoService.getById(alunoId, actualToken);
        if (response.success()){
            log.info("GET /alunos/{} -> OK ", alunoId);
        } else {
            log.error("GET /alunos/{} -> {} ", alunoId, response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Editar aluno por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Aluno alterado",
                    useReturnTypeSchema = true
            ),
    })

    @PutMapping("/{alunoId}")
    public ResponseEntity<AlunoResponse> updateAluno(
            @Parameter(description = "ID do aluno", example = "1")
            @PathVariable Long alunoId,
            @Valid @RequestBody AlunoUpdateRequest alunoUpdateRequest,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("PUT /alunos");
        String actualToken = authToken.substring(7);
        AlunoResponse response = alunoService.updateAluno(alunoId , alunoUpdateRequest, actualToken);
        if (response.success()) {
            log.info("PUT /alunos -> OK ");
        } else {
            log.error("PUT /alunos -> {}", response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Buscar todos os alunos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Alunos encontrados",
                    useReturnTypeSchema = true
            ),
    })

    @GetMapping()
    public ResponseEntity<AlunoResponse> getAllAlunos(
            @RequestHeader(name = "Authorization") String authToken) throws Exception {
        log.info("GET /alunos ");
        String actualToken = authToken.substring(7);
        AlunoResponse response = alunoService.getAllAlunos(actualToken);
        if (response.success()){
            log.info("GET /alunos -> OK ");
        } else {
            log.error("GET /alunos -> {}", response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @Operation(summary = "Buscar notas por aluno")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nota encontrada",
                    useReturnTypeSchema = true
            ),
    })

    @GetMapping("/{alunoId}/notas")
    public ResponseEntity<NotaResponse> getNotasByAlunoId(
            @Parameter(description = "ID do aluno", example = "1")
            @RequestHeader(name = "Authorization") String authToken,
            @Valid @PathVariable Long alunoId) {
        log.info("GET /alunos/{}/notas ", alunoId);
        String actualToken = authToken.substring(7);
        NotaResponse response = notaService.getByAlunoId(alunoId, actualToken);
        if (response.success()){
            log.info("GET /alunos/{}/notas -> OK ", alunoId);
        } else {
            log.error("GET /alunos/{}/notas -> {} ", alunoId, response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
    @Operation(summary = "Excluir aluno por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Aluno excluído"
            ),
    })

    @DeleteMapping("/{alunoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<AlunoResponse> deleteAluno(
            @Parameter(description = "ID do aluno", example = "1")
            @PathVariable @NotNull(message = "ID da aluno é requerido para exclusão") Long alunoId,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("DELETE /alunos");
        String actualToken = authToken.substring(7);
        AlunoResponse response = alunoService.deleteAluno(alunoId, actualToken);
        if (response.success()) {
            log.info("DELETE /alunos -> OK ");
        } else {
            log.error("DELETE /alunos -> {}", response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @Operation(summary = "Buscar média por ID aluno")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Média encontrada",
                    useReturnTypeSchema = true
            ),
    })

    @GetMapping("/{alunoId}/pontuacao")
    public ResponseEntity<AlunoScoreResponse> getScoreByAlunoId(
            @Parameter(description = "ID do aluno", example = "1")
            @PathVariable Long alunoId,
            @RequestHeader(name = "Authorization") String authToken) {

        log.info("GET /alunos/{}/pontuacao", alunoId);
        String actualToken = authToken.substring(7);
        AlunoScoreResponse response = notaService.getScoreByAlunoId(alunoId, actualToken);
        if (response.success()) {
            log.info("GET /alunos/{}/pontuacao -> OK", alunoId);
        } else {
            log.error("GET /alunos/{}/pontuacao -> {}", alunoId, response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @GetMapping("/{alunoId}/meu-curso")
    public ResponseEntity<CursoResponse> getCourseByAlunoId(
            @Parameter(description = "ID do aluno", example = "1")
            @PathVariable Long alunoId,
            @RequestHeader(name = "Authorization") String authToken) {

        log.info("GET /alunos/{}/meu-curso", alunoId);
        String actualToken = authToken.substring(7);
        CursoResponse response = alunoService.getCourseByAlunoId(alunoId, actualToken);
        if (response.success()) {
            log.info("GET /alunos/{}/pontuacao -> OK", alunoId);
        } else {
            log.error("GET /alunos/{}/pontuacao -> {}", alunoId, response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @GetMapping("/{alunoId}/cursos-extra")
    public ResponseEntity<CursoResponse> getExtraCoursesByAlunoId(
            @Parameter(description = "ID do aluno", example = "1")
            @PathVariable Long alunoId,
            @RequestHeader(name = "Authorization") String authToken) {

        log.info("GET /alunos/{}/cursos-extra", alunoId);
        String actualToken = authToken.substring(7);
        CursoResponse response = alunoService.getExtraCoursesByAlunoId(alunoId, actualToken);
        if (response.success()) {
            log.info("GET /alunos/{}/pontuacao -> OK", alunoId);
        } else {
            log.error("GET /alunos/{}/pontuacao -> {}", alunoId, response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

}
