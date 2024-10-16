package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.CursoRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.CursoResponse;
import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import br.com.fullstackedu.labpcp.database.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CursoService {
    private final CursoRepository cursoRepository;
    private final LoginService loginService;

    private static final List<String> commonPermissions = List.of("ADM", "PEDAGOGICO");
    private static final List<String> deletePermission = List.of("ADM");

    private boolean _isAuthorized(String actualToken, List<String> authorizedPerfis) {
        String papelName =  loginService.getFieldInToken(actualToken, "scope");
        return authorizedPerfis.contains(papelName);
    }
    private boolean _isAuthorized(String actualToken) {
        return _isAuthorized(actualToken, commonPermissions);
    }

    public CursoResponse insertCurso(CursoRequest cursoRequest, String actualToken) {
        try {
            if (!_isAuthorized(actualToken)){
                String errMessage = "Usuários logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new CursoResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            return _insertCurso(cursoRequest);
        } catch (Exception e) {
            log.error("Falha ao adicionar Curso. Erro: {}", e.getMessage());
            return new CursoResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    private CursoResponse _insertCurso(CursoRequest cursoRequest) {
        CursoEntity newCurso = new CursoEntity();
        newCurso.setNome(cursoRequest.nome());
        CursoEntity insertedCurso = cursoRepository.save(newCurso);
        log.info("Curso adicionada com sucesso: {}", newCurso.getId());
        return new CursoResponse(true, LocalDateTime.now(),"Curso adicionada com sucesso.", Collections.singletonList(insertedCurso), HttpStatus.CREATED);
    }

    public CursoResponse getById(Long cursoId, String actualToken) {
        try {
            if (!_isAuthorized(actualToken)){
                String errMessage = "Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new CursoResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            CursoEntity targetCurso = cursoRepository.findById(cursoId).orElse(null);
            if (Objects.isNull(targetCurso)){
                return new CursoResponse(false, LocalDateTime.now() , "Curso ID "+cursoId+" não encontrado." , null, HttpStatus.NOT_FOUND);
            } else
                return new CursoResponse(true, LocalDateTime.now() , "Curso encontrado" , Collections.singletonList(targetCurso), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Falha ao buscar Curso ID {}. Erro: {}", cursoId, e.getMessage());
            return new CursoResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    public CursoResponse getAll(String actualToken) {
        try {
            if(!_isAuthorized(actualToken)){
                String errMessage = "Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new CursoResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.UNAUTHORIZED);
            }
            List<CursoEntity> listCursos = cursoRepository.findAll();
            if (listCursos.isEmpty()){
                return new CursoResponse(false, LocalDateTime.now() , "Não há cursos cadastrados." , null, HttpStatus.NOT_FOUND);
            } else
                return new CursoResponse(true, LocalDateTime.now(), "Cursos encontrados: " + listCursos.size() , listCursos, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Falha ao buscar Cursos cadastrados. Erro: {}", e.getMessage());
            return new CursoResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    public CursoResponse update(Long cursoId, CursoRequest cursoUpdateRequest, String actualToken) {
        try {
            if (!_isAuthorized(actualToken)){
                String errMessage = "Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new CursoResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            return _updateCurso(cursoUpdateRequest,cursoId);

        } catch (Exception e) {
            log.error("Falha ao atualizar a Curso {}. Erro: {}", cursoId, e.getMessage());
            return new CursoResponse(false, LocalDateTime.now(), e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    private CursoResponse _updateCurso(CursoRequest cursoUpdateRequest, Long cursoId) {
        CursoEntity targetCursoEntity = cursoRepository.findById(cursoId).orElse(null);
        if (Objects.isNull(targetCursoEntity))
            return new CursoResponse(false, LocalDateTime.now() , "Curso id [" + cursoId + "] não encontrado" , null, HttpStatus.NOT_FOUND);

        targetCursoEntity.setNome(cursoUpdateRequest.nome());
        CursoEntity savedCursoEntity = cursoRepository.save(targetCursoEntity);
        return new CursoResponse(true, LocalDateTime.now(), "Curso atualizado", Collections.singletonList(savedCursoEntity) , HttpStatus.OK);

    }

    public CursoResponse deleteCurso(Long cursoId, String actualToken) {
        try {
            if(!_isAuthorized(actualToken, deletePermission)) {
                String errMessage = "Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new CursoResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.UNAUTHORIZED);
            }
            return _deleteCurso(cursoId);

        } catch (Exception e) {
            log.error("Falha ao excluir a curso {}. Erro: {}", cursoId, e.getMessage());
            return new CursoResponse(false, LocalDateTime.now(), e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }

    }

    private CursoResponse _deleteCurso(Long cursoId) {
        CursoEntity targetCursoEntity = cursoRepository.findById(cursoId).orElse(null);
        if (Objects.isNull(targetCursoEntity))
            return new CursoResponse(false, LocalDateTime.now() , "Curso id [" + cursoId + "] não encontrada" , null, HttpStatus.NOT_FOUND);
        else {
            cursoRepository.delete(targetCursoEntity);
            return new CursoResponse(true, LocalDateTime.now() , "Curso id [" + cursoId + "] excluido" , null, HttpStatus.NO_CONTENT);
        }
    }

}
