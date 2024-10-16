package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.MateriaRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.MateriaUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.MateriaResponse;
import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import br.com.fullstackedu.labpcp.database.entity.MateriaEntity;
import br.com.fullstackedu.labpcp.database.repository.CursoRepository;
import br.com.fullstackedu.labpcp.database.repository.MateriaRepository;
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
public class MateriaService {
    private final MateriaRepository materiaRepository;
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

    public MateriaResponse insert(MateriaRequest materiaRequest, String actualToken) {
        try {
            if (!_isAuthorized(actualToken)){
                String errMessage = "O Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new MateriaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            return _insertMateria(materiaRequest);
        } catch (Exception e) {
            log.error("Falha ao adicionar Materia. Erro: {}", e.getMessage());
            return new MateriaResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    private MateriaResponse _insertMateria(MateriaRequest materiaRequest) {
        CursoEntity targetCurso = cursoRepository.findById(materiaRequest.id_curso()).orElse(null);
        if (Objects.isNull(targetCurso)){
            String errMessage = "Erro ao cadastrar turma: Nenhum curso com id ["+ materiaRequest.id_curso() +"] encontrado";
            log.error(errMessage);
            return new MateriaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.NOT_FOUND);
        }

        MateriaEntity newMateria = new MateriaEntity();
        newMateria.setNome(materiaRequest.nome());
        newMateria.setCurso(targetCurso);
        MateriaEntity insertedMateria = materiaRepository.save(newMateria);
        log.info("Materia adicionada com sucesso: {}", newMateria.getId());
        return new MateriaResponse(true, LocalDateTime.now(),"Materia adicionada com sucesso.", Collections.singletonList(insertedMateria), HttpStatus.CREATED);
    }

    public MateriaResponse getById(Long materiaId, String actualToken) {
        try {
            if (!_isAuthorized(actualToken)){
                String errMessage = "O Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new MateriaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            MateriaEntity targetMateria = materiaRepository.findById(materiaId).orElse(null);
            if (Objects.isNull(targetMateria)){
                return new MateriaResponse(false, LocalDateTime.now() , "Materia ID "+materiaId+" não encontrado." , null, HttpStatus.NOT_FOUND);
            } else
                return new MateriaResponse(true, LocalDateTime.now() , "Materia encontrada" , Collections.singletonList(targetMateria), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Falha ao buscar Materia ID {}. Erro: {}", materiaId, e.getMessage());
            return new MateriaResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    public MateriaResponse getByCursoId(Long cursoId, String actualToken) {
        try {
            if (!_isAuthorized(actualToken)){
                String errMessage = "Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new MateriaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }

            CursoEntity targetCursoEntity = cursoRepository.findById(cursoId).orElse(null);
            if (Objects.isNull(targetCursoEntity))
                return new MateriaResponse(false, LocalDateTime.now() , "Curso id [" + cursoId + "] não encontrada" , null, HttpStatus.NOT_FOUND);

            List<MateriaEntity> listMaterias =  materiaRepository.findByCursoId(cursoId);
            if (listMaterias.isEmpty()){
                return new MateriaResponse(false, LocalDateTime.now() , "Nenhuma materia encontrada para o Curso ID "+cursoId , null, HttpStatus.NOT_FOUND);
            } else
                return new MateriaResponse(true, LocalDateTime.now() , "Materias encontradas: "+ listMaterias.size() , listMaterias, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Falha ao buscar Materias para o Curso ID {}. Erro: {}", cursoId, e.getMessage());
            return new MateriaResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    public MateriaResponse deleteMateria(Long materiaId, String actualToken) {
        try {
            if(!_isAuthorized(actualToken, deletePermission)) {
                String errMessage = "Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new MateriaResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.UNAUTHORIZED);
            }
            return _deleteMateria(materiaId);

        } catch (Exception e) {
            log.error("Falha ao excluir a materia {}. Erro: {}", materiaId, e.getMessage());
            return new MateriaResponse(false, LocalDateTime.now(), e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }

    }

    private MateriaResponse _deleteMateria(Long materiaId) {
        MateriaEntity targetMateriaEntity = materiaRepository.findById(materiaId).orElse(null);
        if (Objects.isNull(targetMateriaEntity))
            return new MateriaResponse(false, LocalDateTime.now() , "Materia id [" + materiaId + "] não encontrada" , null, HttpStatus.NOT_FOUND);
        else {
            materiaRepository.delete(targetMateriaEntity);
            return new MateriaResponse(true, LocalDateTime.now() , "Materia id [" + materiaId + "] excluido" , null, HttpStatus.NO_CONTENT);
        }
    }

    public MateriaResponse update(Long materiaId, MateriaUpdateRequest materiaUpdateRequest, String actualToken) {
        try {
            if (!_isAuthorized(actualToken)){
                String errMessage = "Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new MateriaResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            return _updateMateria(materiaUpdateRequest,materiaId);

        } catch (Exception e) {
            log.error("Falha ao atualizar a Materia {}. Erro: {}", materiaId, e.getMessage());
            return new MateriaResponse(false, LocalDateTime.now(), e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    private MateriaResponse _updateMateria(MateriaUpdateRequest materiaUpdateRequest, Long materiaId) {
        MateriaEntity targetMateriaEntity = materiaRepository.findById(materiaId).orElse(null);
        if (Objects.isNull(targetMateriaEntity))
            return new MateriaResponse(false, LocalDateTime.now() , "Materia id [" + materiaId + "] não encontrado" , null, HttpStatus.NOT_FOUND);

        if(Objects.nonNull(materiaUpdateRequest.id_curso())) {
            CursoEntity targetCursoEntity = cursoRepository.findById(materiaUpdateRequest.id_curso()).orElse(null);
            if (Objects.isNull(targetCursoEntity))
                return new MateriaResponse(false, LocalDateTime.now() , "Curso id [" + materiaUpdateRequest.id_curso() + "] não encontrada" , null, HttpStatus.NOT_FOUND);
            else targetMateriaEntity.setCurso(targetCursoEntity);
        }
        if(Objects.nonNull(materiaUpdateRequest.nome())) targetMateriaEntity.setNome(materiaUpdateRequest.nome());
        MateriaEntity savedMateriaEntity = materiaRepository.save(targetMateriaEntity);
        return new MateriaResponse(true, LocalDateTime.now(), "Materia atualizada", Collections.singletonList(savedMateriaEntity) , HttpStatus.OK);

    }
}
