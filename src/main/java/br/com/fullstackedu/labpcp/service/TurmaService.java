package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.TurmaCreateRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.TurmaUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.TurmaCreateResponse;
import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import br.com.fullstackedu.labpcp.database.entity.DocenteEntity;
import br.com.fullstackedu.labpcp.database.entity.TurmaEntity;
import br.com.fullstackedu.labpcp.database.repository.CursoRepository;
import br.com.fullstackedu.labpcp.database.repository.DocenteRepository;
import br.com.fullstackedu.labpcp.database.repository.TurmaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TurmaService {
    private final LoginService loginService;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;
    private final TurmaRepository turmaRepository;

    public TurmaService(LoginService loginService, CursoRepository cursoRepository, DocenteRepository docenteRepository, TurmaRepository turmaRepository) {
        this.loginService = loginService;
        this.cursoRepository = cursoRepository;
        this.docenteRepository = docenteRepository;
        this.turmaRepository = turmaRepository;
    }

    public TurmaCreateResponse novaTurma(TurmaCreateRequest turmaCreateRequest, String authToken) throws Exception{
        try {
            String papelName =  loginService.getFieldInToken(authToken, "scope");
            List<String> authorizedPapeis =  Arrays.asList("ADM", "PEDAGOGICO");
            if (!authorizedPapeis.contains(papelName)){
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new TurmaCreateResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            return _insertTurma(turmaCreateRequest);
        } catch (Exception e) {
            log.error("Falha ao adicionar Turma. Erro: {}", e.getMessage());
            return new TurmaCreateResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    private TurmaCreateResponse _insertTurma(TurmaCreateRequest turmaCreateRequest) {
        //curso
        CursoEntity targetCurso = cursoRepository.findById(turmaCreateRequest.id_curso()).orElse(null);
        if (Objects.isNull(targetCurso)){
            String errMessage = "Erro ao cadastrar turma: Nenhum curso com id ["+ turmaCreateRequest.id_curso() +"] encontrado";
            log.error(errMessage);
            return new TurmaCreateResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.NOT_FOUND);
        }
        TurmaEntity newTurma = new TurmaEntity();

        //professor (not required)
        if (Objects.isNull(turmaCreateRequest.id_professor())) {
            newTurma.setProfessor(null);
        } else {
            DocenteEntity targetProfessor = docenteRepository.findById(turmaCreateRequest.id_professor()).orElse(null);
            if (Objects.isNull(targetProfessor)){
                String errMessage = "Erro ao cadastrar turma: Nenhum professor com id ["+ turmaCreateRequest.id_professor() +"] encontrado";
                log.error(errMessage);
                return new TurmaCreateResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.NOT_FOUND);
            } else
                newTurma.setProfessor(targetProfessor);
        }
        newTurma.setCurso(targetCurso);
        newTurma.setNome(turmaCreateRequest.nome());
        newTurma.setDataFim(turmaCreateRequest.dataFim());
        newTurma.setDataInicio(turmaCreateRequest.dataInicio());
        newTurma.setHora(turmaCreateRequest.hora());
        TurmaEntity insertedTurma = turmaRepository.save(newTurma);
        log.info("Turma adicionada com sucesso: {}", newTurma.getId());
        return new TurmaCreateResponse(true, LocalDateTime.now(),"Turma adicionada com sucesso.", Collections.singletonList(newTurma), HttpStatus.CREATED);
    }

    public TurmaCreateResponse getTurmaById(Long turmaId, String actualToken) {
        try {
            String papelName =  loginService.getFieldInToken(actualToken, "scope");
            List<String> authorizedPapeis =  Arrays.asList("ADM", "PEDAGOGICO");
            if (!authorizedPapeis.contains(papelName)){
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new TurmaCreateResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            TurmaEntity targetTurma = turmaRepository.findById(turmaId).orElse(null);
            if (Objects.isNull(targetTurma)){
                return new TurmaCreateResponse(false, LocalDateTime.now() , "Turma ID "+turmaId+" não encontrado." , null, HttpStatus.NOT_FOUND);
            } else
                return new TurmaCreateResponse(true, LocalDateTime.now() , "Turma encontrada" , Collections.singletonList(targetTurma), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Falha ao buscar Turma ID {}. Erro: {}", turmaId, e.getMessage());
            return new TurmaCreateResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    public TurmaCreateResponse updateTurma(Long turmaId, TurmaUpdateRequest turmaUpdateRequest, String actualToken) {
        try {
            String papelName =  loginService.getFieldInToken(actualToken, "scope");
            List<String> authorizedPapeis =  Arrays.asList("ADM", "PEDAGOGICO");
            if (!authorizedPapeis.contains(papelName)){
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new TurmaCreateResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            return _updateTurma(turmaUpdateRequest,turmaId);

        } catch (Exception e) {
            log.error("Falha ao atualizar a Turma {}. Erro: {}", turmaId, e.getMessage());
            return new TurmaCreateResponse(false, LocalDateTime.now(), e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    private TurmaCreateResponse _updateTurma(TurmaUpdateRequest turmaUpdateRequest, Long turmaId) {
        TurmaEntity targetTurmaEntity = turmaRepository.findById(turmaId).orElse(null);
        if (Objects.isNull(targetTurmaEntity))
            return new TurmaCreateResponse(false, LocalDateTime.now() , "Turma id [" + turmaId + "] não encontrada" , null, HttpStatus.NOT_FOUND);

        if (turmaUpdateRequest.id_curso() != null) {
            CursoEntity targetCurso = cursoRepository.findById(turmaUpdateRequest.id_curso()).orElse(null);
            if (targetCurso != null) targetTurmaEntity.setCurso(targetCurso);
            else return new TurmaCreateResponse(false, LocalDateTime.now() , "Falha ao associar Curso ID "+ turmaUpdateRequest.id_curso() +" à Turma ID ["+ turmaId +"]: O Curso não existe." , null, HttpStatus.NOT_FOUND);
        }

        if (!Objects.isNull(turmaUpdateRequest.id_professor())) {
            DocenteEntity targetProfessor = docenteRepository.findById(turmaUpdateRequest.id_professor()).orElse(null);
            if (Objects.isNull(targetProfessor)){
                String errMessage = "Erro ao associar professor à turma: Nenhum Docente com id ["+ turmaUpdateRequest.id_professor() +"] encontrado";
                log.error(errMessage);
                return new TurmaCreateResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.NOT_FOUND);
            } else {
                targetTurmaEntity.setProfessor(targetProfessor);
            }
        }
        if (turmaUpdateRequest.nome() != null) targetTurmaEntity.setNome(turmaUpdateRequest.nome());
        if (turmaUpdateRequest.hora() != null) targetTurmaEntity.setHora(turmaUpdateRequest.hora());
        if (turmaUpdateRequest.dataFim() != null) targetTurmaEntity.setDataFim(turmaUpdateRequest.dataFim());
        if (turmaUpdateRequest.dataInicio() != null) targetTurmaEntity.setDataInicio(turmaUpdateRequest.dataInicio());

        TurmaEntity savedTurmaEntity = turmaRepository.save(targetTurmaEntity);
        return new TurmaCreateResponse(true, LocalDateTime.now(), "Turma atualizada", Collections.singletonList(savedTurmaEntity) , HttpStatus.OK);

    }

    public TurmaCreateResponse deleteTurma(Long turmaId, String actualToken) {
        try {
            String papelName = loginService.getFieldInToken(actualToken, "scope");
            List<String> authorizedPapeis = List.of("ADM");
            if (!authorizedPapeis.contains(papelName)) {
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new TurmaCreateResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.UNAUTHORIZED);
            }
            return _deleteTurma(turmaId);

        } catch (Exception e) {
            log.error("Falha ao excluir a turma {}. Erro: {}", turmaId, e.getMessage());
            return new TurmaCreateResponse(false, LocalDateTime.now(), e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }

    }

    private TurmaCreateResponse _deleteTurma(Long turmaId) {
        TurmaEntity targetTurmaEntity = turmaRepository.findById(turmaId).orElse(null);
        if (Objects.isNull(targetTurmaEntity))
            return new TurmaCreateResponse(false, LocalDateTime.now() , "Turma id [" + turmaId + "] não encontrada" , null, HttpStatus.NOT_FOUND);
        else {
            turmaRepository.delete(targetTurmaEntity);
            return new TurmaCreateResponse(true, LocalDateTime.now() , "Docente id [" + turmaId + "] excluido" , null, HttpStatus.NO_CONTENT);
        }
    }

    public TurmaCreateResponse getAllTurmas(String actualToken) {
        try {
            String papelName = loginService.getFieldInToken(actualToken, "scope");
            List<String> authorizedPapeis = Arrays.asList("ADM", "PEDAGOGICO");
            if (!authorizedPapeis.contains(papelName)) {
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new TurmaCreateResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.UNAUTHORIZED);
            }
            List<TurmaEntity> listTurmas = turmaRepository.findAll();
            if (listTurmas.isEmpty()){
                return new TurmaCreateResponse(false, LocalDateTime.now() , "Não há turmas cadastradas." , null, HttpStatus.NOT_FOUND);
            } else
                return new TurmaCreateResponse(true, LocalDateTime.now(), "Turmas encontradas: " + listTurmas.size() , listTurmas, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Falha ao buscar Turmas cadastradas. Erro: {}", e.getMessage());
            return new TurmaCreateResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }

    }

    public long count() {
        return turmaRepository.count();
    }
}
