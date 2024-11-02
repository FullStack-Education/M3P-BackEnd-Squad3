package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.NotaRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.NotaUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.AlunoScoreDTO;
import br.com.fullstackedu.labpcp.controller.dto.response.AlunoScoreResponse;
import br.com.fullstackedu.labpcp.controller.dto.response.NotaResponse;
import br.com.fullstackedu.labpcp.database.entity.*;
import br.com.fullstackedu.labpcp.database.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotaService {
    private final NotaRepository notaRepository;
    private final DocenteRepository docenteRepository;
    private final AlunoRepository alunoRepository;
    private final MateriaRepository materiaRepository;
    private final LoginService loginService;
    private final TurmaRepository turmaRepository;

    private static final List<String> commonPermissions = List.of("ADM", "PROFESSOR");
    private static final List<String> ownerPermissions = List.of("ALUNO");
    private static final List<String> admPermission = List.of("ADM");

    private boolean _isAuthorized(String actualToken, List<String> authorizedPerfis) {
        String papelName = loginService.getFieldInToken(actualToken, "scope");
        return authorizedPerfis.contains(papelName);
    }
    private boolean _isAuthorized(String actualToken) {
        return _isAuthorized(actualToken, commonPermissions);
    }

    public NotaResponse insert(NotaRequest notaRequest, String actualToken) {
        try {
            if (!_isAuthorized(actualToken)){
                String errMessage = "O Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new NotaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            return _insertNota(notaRequest);
        } catch (Exception e) {
            log.error("Falha ao adicionar Nota. Erro: {}", e.getMessage());
            return new NotaResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    private NotaResponse _insertNota(NotaRequest notaRequest) {
        AlunoEntity targetAluno = alunoRepository.findById(notaRequest.id_aluno()).orElse(null);
        if (Objects.isNull(targetAluno)){
            String errMessage = "Erro ao cadastrar nota: Nenhum aluno com id ["+ notaRequest.id_aluno() +"] encontrado";
            log.error(errMessage);
            return new NotaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.NOT_FOUND);
        }

        DocenteEntity targetProfessor = docenteRepository.findById(notaRequest.id_professor()).orElse(null);
        if (Objects.isNull(targetProfessor)){
            String errMessage = "Erro ao cadastrar nota: Nenhum professor com id ["+ notaRequest.id_professor() +"] encontrado";
            log.error(errMessage);
            return new NotaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.NOT_FOUND);
        } else if (!targetProfessor.getUsuario().getPapel().getNome().toUpperCase().contains("PROFESSOR")) {
            String errMessage = "Erro ao cadastrar nota: Docente Id ["+ notaRequest.id_professor() + "] não possui o papel de Professor";
            log.error(errMessage);
            return new NotaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.NOT_FOUND);
        }

        MateriaEntity targetMateria = materiaRepository.findById(notaRequest.id_materia()).orElse(null);
        if (Objects.isNull(targetMateria)){
            String errMessage = "Erro ao cadastrar nota: Nenhuma materia com id ["+ notaRequest.id_materia() +"] encontrada";
            log.error(errMessage);
            return new NotaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.NOT_FOUND);
        }

        TurmaEntity targetTurma = turmaRepository.findById(notaRequest.id_turma()).orElse(null);
        if (Objects.isNull(targetTurma)){
            String errMessage = "Erro ao cadastrar nota: Nenhuma turma com id ["+ notaRequest.id_turma() +"] encontrada";
            log.error(errMessage);
            return new NotaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.NOT_FOUND);
        }

        NotaEntity newNota = new NotaEntity();


        newNota.setAluno(targetAluno);
        newNota.setProfessor(targetProfessor);
        newNota.setMateria(targetMateria);
        newNota.setTurma(targetTurma);
        newNota.setNome(notaRequest.nome());
        newNota.setData(notaRequest.data());
        newNota.setValor(notaRequest.valor());
        NotaEntity insertedNota = notaRepository.save(newNota);
        log.info("Nota adicionada com sucesso: {}", newNota.getId());
        return new NotaResponse(true, LocalDateTime.now(),"Nota adicionada com sucesso.", Collections.singletonList(insertedNota), HttpStatus.CREATED);
    }

    public NotaResponse getById(Long notaId, String actualToken) {
        try {
            if (_isAuthorized(actualToken, commonPermissions)) {
                return _getNotaById(notaId);
            }

            if (_isAuthorized(actualToken, ownerPermissions)) {
                Long usuarioId = Long.valueOf(loginService.getFieldInToken(actualToken, "id_usuario"));
                return _getNotaByIdAndAlunoId(notaId, usuarioId);
            }

            return NotaResponse.createErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "O Usuário logado não tem acesso a essa funcionalidade");

        } catch (Exception e) {
            log.error("Falha ao buscar Nota ID {}. Erro: {}", notaId, e.getMessage());
            return NotaResponse.createErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    private NotaResponse _getNotaById(Long notaId) {
        NotaEntity targetNota = notaRepository.findById(notaId).orElse(null);
        if (targetNota == null) {
            return NotaResponse.createErrorResponse(
                    HttpStatus.NOT_FOUND,
                    "Nota ID " + notaId + " não encontrada."
            );
        }
        return NotaResponse.createSuccessResponse(
                HttpStatus.OK,
                "Nota encontrada",
                Collections.singletonList(targetNota)
        );
    }

    private NotaResponse _getNotaByIdAndAlunoId(Long notaId, Long usuarioId) {
        AlunoEntity targetAluno = alunoRepository.findByUsuarioId(usuarioId).orElse(null);
        if (targetAluno == null) {
            return NotaResponse.createErrorResponse(
                    HttpStatus.NOT_FOUND,
                    "Nenhum Aluno foi encontrado para o usuário logado com id [" + usuarioId + "]"
            );
        }

        NotaEntity targetNota = notaRepository.findByIdAndAlunoId(notaId, targetAluno.getId()).orElse(null);
        if (targetNota == null) {
            return NotaResponse.createErrorResponse(
                    HttpStatus.NOT_FOUND,
                    "Nota ID " + notaId + " não encontrada para este aluno."
            );
        }

        return NotaResponse.createSuccessResponse(
                HttpStatus.OK,
                "Nota encontrada",
                Collections.singletonList(targetNota)
        );
    }

    public NotaResponse getByAlunoId(Long alunoId, String actualToken) {
        try {
            if (_isAuthorized(actualToken, commonPermissions)) {
                return _getByAlunoId(alunoId);
            }

            if (_isAuthorized(actualToken, ownerPermissions)){
                Long usuarioId = Long.valueOf(loginService.getFieldInToken(actualToken, "id_usuario"));
                AlunoEntity loggedAluno = alunoRepository.findByUsuarioId(usuarioId).orElse(null);
                if (loggedAluno != null && Objects.equals(loggedAluno.getId(), alunoId)) {
                    return _getByAlunoId(alunoId);
                } else return NotaResponse.createErrorResponse(
                        HttpStatus.UNAUTHORIZED,
                        "Alunos logados tem acesso someone a suas próprias notas.");
            }
            return NotaResponse.createErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "O Usuário logado não tem acesso a essa funcionalidade");

        } catch (Exception e) {
            log.error("Falha ao buscar Notas do Aluno ID {}. Erro: {}", alunoId, e.getMessage());
            return NotaResponse.createErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    private NotaResponse _getByAlunoId(Long alunoId) {
        List<NotaEntity> listNotasByAluno = notaRepository.findByAlunoId(alunoId);
        if (listNotasByAluno.isEmpty()){
            return NotaResponse.createErrorResponse (
                    HttpStatus.NOT_FOUND,
                    "Nenhuma Nota encontrada para o Aluno ID "+alunoId);
        } else
            return NotaResponse.createSuccessResponse (
                    HttpStatus.OK,
                    "Notas encontradas: " + listNotasByAluno.size(),
                    listNotasByAluno);
    }

    public NotaResponse updateNota(Long notaId, NotaUpdateRequest notaUpdateRequest, String actualToken) {
        try {
            if (_isAuthorized(actualToken, commonPermissions)) {
                return _updateNota(notaUpdateRequest,notaId);
            }
            String errMessage = "O Usuário logado não tem acesso a essa funcionalidade";
            log.error(errMessage);
            return NotaResponse.createErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    errMessage);
        } catch (Exception e) {
            log.error("Falha ao atualizar a Nota {}. Erro: {}", notaId, e.getMessage());
            return NotaResponse.createErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    private NotaResponse _updateNota(NotaUpdateRequest notaUpdateRequest, Long notaId) {
        NotaEntity targetNotaEntity = notaRepository.findById(notaId).orElse(null);
        if (Objects.isNull(targetNotaEntity))
            return NotaResponse.createErrorResponse(
                    HttpStatus.NOT_FOUND,
                    "Nota id [" + notaId + "] não encontrado"
            );
        if (Objects.nonNull(notaUpdateRequest.id_aluno())) {
            AlunoEntity targetAluno = alunoRepository.findById(notaUpdateRequest.id_aluno()).orElse(null);
            if (Objects.isNull(targetAluno))
                return NotaResponse.createErrorResponse(
                        HttpStatus.NOT_FOUND,
                        "Aluno id [" + notaUpdateRequest.id_aluno() + "] não encontrado"
                );
            targetNotaEntity.setAluno(targetAluno);
        }
        if (Objects.nonNull(notaUpdateRequest.id_materia())) {
            MateriaEntity targetMateriaEntity = materiaRepository.findById(notaUpdateRequest.id_materia()).orElse(null);
            if (Objects.isNull(targetMateriaEntity))
                return NotaResponse.createErrorResponse(
                        HttpStatus.NOT_FOUND,
                        "Materia id [" + notaUpdateRequest.id_materia() + "] não encontrado"
                );
            targetNotaEntity.setMateria(targetMateriaEntity);
        }

        if (Objects.nonNull(notaUpdateRequest.id_professor())) {
            DocenteEntity targetDocente = docenteRepository.findById(notaUpdateRequest.id_professor()).orElse(null);
            if (Objects.isNull(targetDocente))
                return NotaResponse.createErrorResponse(
                        HttpStatus.NOT_FOUND,
                        "Docente id [" + notaUpdateRequest.id_professor() + "] não encontrado"
                );
            targetNotaEntity.setProfessor(targetDocente);
        }

        if (Objects.nonNull(notaUpdateRequest.id_turma())) {
            TurmaEntity targetTurma = turmaRepository.findById(notaUpdateRequest.id_professor()).orElse(null);
            if (Objects.isNull(targetTurma))
                return NotaResponse.createErrorResponse(
                        HttpStatus.NOT_FOUND,
                        "Turma id [" + notaUpdateRequest.id_turma() + "] não encontrado"
                );
            targetNotaEntity.setTurma(targetTurma);
        }
        if (Objects.nonNull(notaUpdateRequest.valor())) targetNotaEntity.setValor(notaUpdateRequest.valor());
        if (Objects.nonNull(notaUpdateRequest.data())) targetNotaEntity.setData(notaUpdateRequest.data());

        NotaEntity savedNotaEntity = notaRepository.save(targetNotaEntity);
        return NotaResponse.createSuccessResponse(
                HttpStatus.OK,
                "Nota atualizada",
                Collections.singletonList(savedNotaEntity)
        );

    }
    public NotaResponse deleteNota(Long notaId, String actualToken) {
        try {
            if(_isAuthorized(actualToken, admPermission)) {
                return _deleteNota(notaId);
            }
            String errMessage = "O Usuário logado não tem acesso a essa funcionalidade";
            log.error(errMessage);
            return NotaResponse.createErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    errMessage
            );
        } catch (Exception e) {
            log.error("Falha ao excluir a nota {}. Erro: {}", notaId, e.getMessage());
            return NotaResponse.createErrorResponse (
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    private NotaResponse _deleteNota(Long notaId) {
        NotaEntity targetNotaEntity = notaRepository.findById(notaId).orElse(null);
        if (Objects.isNull(targetNotaEntity))
            return NotaResponse.createErrorResponse (
                    HttpStatus.NOT_FOUND,
                    "Nota id [" + notaId + "] não encontrada"
            );
        else {
            notaRepository.delete(targetNotaEntity);
            return NotaResponse.createErrorResponse(
                    HttpStatus.NO_CONTENT,
                    "Nota id [" + notaId + "] excluido"
            );
        }
    }

    public AlunoScoreResponse getScoreByAlunoId(Long alunoId, String actualToken) {
        try {
            if (_isAuthorized(actualToken, admPermission)) {
                return _getScoreByAlunoId(alunoId);
            }

            if (_isAuthorized(actualToken, ownerPermissions)){
                Long usuarioId = Long.valueOf(loginService.getFieldInToken(actualToken, "id_usuario"));
                AlunoEntity loggedAluno =  alunoRepository.findByUsuarioId(usuarioId).orElse(null);
                if (loggedAluno != null && Objects.equals(loggedAluno.getId(), alunoId)) {
                    return _getScoreByAlunoId(alunoId);
                } else return AlunoScoreResponse.createErrorResponse(
                        HttpStatus.UNAUTHORIZED,
                        "Alunos logados tem acesso someone a suas próprias notas e pontuações.");
            }
            return AlunoScoreResponse.createErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "O Usuário logado não tem acesso a essa funcionalidade");

        } catch (Exception e) {
            log.error("Falha ao buscar Notas do Aluno ID {}. Erro: {}", alunoId, e.getMessage());
            return AlunoScoreResponse.createErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    private AlunoScoreResponse _getScoreByAlunoId(Long alunoId) {
        AlunoEntity targetAlunoEntity = alunoRepository.findById(alunoId).orElseThrow(() -> new RuntimeException("Erro ao buscar aluno ID ["+alunoId+"]"));
        NotaResponse listOfNotas = _getByAlunoId(alunoId);
        if(!listOfNotas.success()){
            return AlunoScoreResponse.createErrorResponse(
                    listOfNotas.httpStatus(),
                    listOfNotas.message()
            );
        }
        double averageGradesAluno = listOfNotas.notaData()
                .stream()
                .map(NotaEntity::getValor)
                .collect(
                        Collectors.averagingDouble(Double::doubleValue)
                );
        double scoreAluno = (averageGradesAluno * 10);
        double roundedScoreAluno = Math.round(scoreAluno * 100.00)/100.00;

        AlunoScoreDTO alunoScoreDTO = new AlunoScoreDTO(targetAlunoEntity, roundedScoreAluno);
        return AlunoScoreResponse.createSuccessResponse(
                HttpStatus.OK,
                "Pontuação calculada com sucesso",
                Collections.singletonList(alunoScoreDTO)
        );
    }
}
