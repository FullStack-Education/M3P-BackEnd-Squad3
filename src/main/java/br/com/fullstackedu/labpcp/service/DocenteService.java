package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.DocenteCreateRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.DocenteUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.NovoDocenteResponse;
import br.com.fullstackedu.labpcp.database.entity.DocenteEntity;
import br.com.fullstackedu.labpcp.database.entity.MateriaEntity;
import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.database.repository.DocenteRepository;
import br.com.fullstackedu.labpcp.database.repository.MateriaRepository;
import br.com.fullstackedu.labpcp.database.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class DocenteService {
    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final LoginService loginService;
    private final MateriaRepository materiaRepository;

    public DocenteService(DocenteRepository docenteRepository, UsuarioRepository usuarioRepository, LoginService loginService, MateriaRepository materiaRepository) {
        this.docenteRepository = docenteRepository;
        this.usuarioRepository = usuarioRepository;
        this.loginService = loginService;
        this.materiaRepository = materiaRepository;
    }

    public NovoDocenteResponse addMateriaDocente(Long materiaId, Long docenteId, String authToken) {
        String papelName =  loginService.getFieldInToken(authToken, "scope");
        List<String> authorizedPapeis =  Arrays.asList("ADM", "PEDAGOGICO", "RECRUITER");
        if (!authorizedPapeis.contains(papelName)){
            String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
            log.error(errMessage);
            return new NovoDocenteResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
        }

        Optional<DocenteEntity> docenteOpt = docenteRepository.findById(docenteId);
        Optional<MateriaEntity> materiaOpt = materiaRepository.findById(materiaId);

        if (docenteOpt.isEmpty()) {
            String errMessage = "Erro ao cadastrar docente: Nenhum docente com id [" + docenteId + "] encontrado";
            return new NovoDocenteResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.NOT_FOUND);
        } else if (materiaOpt.isEmpty()) {
            String errMessage = "Erro ao cadastrar docente: Nenhum docente com id [" + materiaId + "] encontrado";
            return new NovoDocenteResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.NOT_FOUND);
        } else {
            //log.info("4");
            DocenteEntity docente = docenteOpt.get();
            //log.info("5");
            MateriaEntity materia = materiaOpt.get();
            //log.info("6, {}",materia);
            docente.addMateria(materia);
            //log.info(docente.toString());
            //log.info("7");
            DocenteEntity newDocenteEntity = docenteRepository.save(docente);
            //log.info("8");
            //log.info("Docente atualizado com sucesso: {}", newDocenteEntity);
            //log.info("9");
            return new NovoDocenteResponse(true, LocalDateTime.now(), "Docente atualizado com sucesso.", Collections.singletonList(newDocenteEntity), HttpStatus.OK);
        }
    }

    public NovoDocenteResponse deleteMateriaDocente(Long materiaId, Long docenteId, String authToken) {
        String papelName =  loginService.getFieldInToken(authToken, "scope");
        List<String> authorizedPapeis =  Arrays.asList("ADM", "PEDAGOGICO", "RECRUITER");
        if (!authorizedPapeis.contains(papelName)){
            String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
            log.error(errMessage);
            return new NovoDocenteResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
        }
        Optional<DocenteEntity> docenteOpt = docenteRepository.findById(docenteId);
        Optional<MateriaEntity> materiaOpt = materiaRepository.findById(materiaId);

        if (docenteOpt.isEmpty()) {
            String errMessage = "Erro ao atualizar docente: Nenhum docente com id [" + docenteId + "] encontrado";
            log.error(errMessage);
            return new NovoDocenteResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.NOT_FOUND);
        } else if (materiaOpt.isEmpty()) {
            String errMessage = "Erro ao atualizar docente: Nenhuma materia com id [" + materiaId + "] encontrada";
            log.error(errMessage);
            return new NovoDocenteResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.NOT_FOUND);
        } else {
            DocenteEntity docente = docenteOpt.get();
            MateriaEntity materia = materiaOpt.get();
            docente.removeMateria(materia);
            DocenteEntity docenteEntity = docenteRepository.save(docente);
            return new NovoDocenteResponse(false, LocalDateTime.now(), "Docente atualizado com sucesso", Collections.singletonList(docenteEntity), HttpStatus.NO_CONTENT);
        }
    }

    public NovoDocenteResponse novoDocente(DocenteCreateRequest docenteCreateRequest, String authToken) throws Exception{
        try {
            String papelName =  loginService.getFieldInToken(authToken, "scope");
            List<String> authorizedPapeis =  Arrays.asList("ADM", "PEDAGOGICO", "RECRUITER");
            if (!authorizedPapeis.contains(papelName)){
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new NovoDocenteResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            UsuarioEntity targetUsuario = usuarioRepository.findById(docenteCreateRequest.id_usuario()).orElse(null);   //getReferenceById();
            if (Objects.isNull(targetUsuario)){
                String errMessage = "Erro ao cadastrar docente: Nenhum usuário com id ["+ docenteCreateRequest.id_usuario() +"] encontrado";
                log.error(errMessage);
                return new NovoDocenteResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.NOT_FOUND);
            }
            DocenteEntity newDocenteEntity = docenteRepository.save(
                    new DocenteEntity(
                            docenteCreateRequest.nome(),
                            docenteCreateRequest.data_entrada(),
                            targetUsuario)
                    );
            log.info("Docente adicionado com sucesso: {}", newDocenteEntity);
            return new NovoDocenteResponse(true, LocalDateTime.now(),"Docente cadastrado com sucesso.", Collections.singletonList(newDocenteEntity), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Falha ao adicionar Docente. Erro: {}", e.getMessage());
            return new NovoDocenteResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }


    public NovoDocenteResponse getDocenteById(Long docenteId, String authToken) {
        try {
            String papelName =  loginService.getFieldInToken(authToken, "scope");
            List<String> authorizedPapeis =  Arrays.asList("ADM", "PEDAGOGICO", "RECRUITER");
            if (!authorizedPapeis.contains(papelName)){
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new NovoDocenteResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            DocenteEntity targetDocente = docenteRepository.findById(docenteId).orElse(null);
            if (Objects.isNull(targetDocente)){
                return new NovoDocenteResponse(false, LocalDateTime.now() , "Docente ID "+docenteId+" não encontrado." , null, HttpStatus.NOT_FOUND);
            } else
                return new NovoDocenteResponse(true, LocalDateTime.now() , "Docente encontrado" , Collections.singletonList(targetDocente), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Falha ao buscar Docente ID {}. Erro: {}", docenteId, e.getMessage());
            return new NovoDocenteResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    public NovoDocenteResponse getAllDocentes(String authToken) {
        try {
            String papelName =  loginService.getFieldInToken(authToken, "scope");
            List<String> authorizedPapeis =  Arrays.asList("ADM", "PEDAGOGICO", "RECRUITER");
            if (!authorizedPapeis.contains(papelName)){
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new NovoDocenteResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            List<DocenteEntity> listDocentes = docenteRepository.findAll();
            if (listDocentes.isEmpty()){
                return new NovoDocenteResponse(false, LocalDateTime.now() , "Não há docentes cadastrados." , null, HttpStatus.NOT_FOUND);
            } else
                return new NovoDocenteResponse(true, LocalDateTime.now(), "Docentes encontrados: " + listDocentes.size() , listDocentes, HttpStatus.OK);
        } catch(Exception e) {
            log.error("Falha ao buscar Docentes cadastrados. Erro: {}", e.getMessage());
            return new NovoDocenteResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }

    public NovoDocenteResponse updateDocente(Long docenteId, DocenteUpdateRequest docenteUpdateRequest, String authToken) {
        try {
            String papelName = loginService.getFieldInToken(authToken, "scope");
            List<String> authorizedPapeis = Arrays.asList("ADM", "PEDAGOGICO", "RECRUITER");
            if (!authorizedPapeis.contains(papelName)) {
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new NovoDocenteResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.UNAUTHORIZED);
            }
            return _updateDocente(docenteUpdateRequest,docenteId);

        } catch (Exception e) {
            log.error("Falha ao atualizar o docente {}. Erro: {}", docenteId, e.getMessage());
            return new NovoDocenteResponse(false, LocalDateTime.now(), e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    private NovoDocenteResponse _updateDocente(DocenteUpdateRequest docenteUpdateRequest, Long docenteId) {
        DocenteEntity targetDocenteEntity = docenteRepository.findById(docenteId).orElse(null);
        if (Objects.isNull(targetDocenteEntity))
            return new NovoDocenteResponse(false, LocalDateTime.now() , "Docente id [" + docenteId + "] não encontrado" , null, HttpStatus.NOT_FOUND);

        if (docenteUpdateRequest.id_usuario() != null) {
            UsuarioEntity usuario = usuarioRepository.findById(docenteUpdateRequest.id_usuario()).orElse(null);
            if (usuario != null) targetDocenteEntity.setUsuario(usuario);
            else return new NovoDocenteResponse(false, LocalDateTime.now() , "Falha ao associar Usuário ID "+ docenteUpdateRequest.id_usuario() +" ao Docente ID ["+ docenteId+"]: Usuário não existe" , null, HttpStatus.NOT_FOUND);
        }

        if (docenteUpdateRequest.nome() != null) targetDocenteEntity.setNome(docenteUpdateRequest.nome());

        if (docenteUpdateRequest.data_entrada() != null) targetDocenteEntity.setDataEntrada(docenteUpdateRequest.data_entrada());

        DocenteEntity savedDocenteEntity = docenteRepository.save(targetDocenteEntity);
        return new NovoDocenteResponse(true, LocalDateTime.now(), "Docente atualizado", Collections.singletonList(savedDocenteEntity) , HttpStatus.OK);

    }

    public NovoDocenteResponse deleteDocente(Long docenteId, String authToken) {
        try {
            String papelName = loginService.getFieldInToken(authToken, "scope");
            List<String> authorizedPapeis = List.of("ADM");
            if (!authorizedPapeis.contains(papelName)) {
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new NovoDocenteResponse(false, LocalDateTime.now(), errMessage, null, HttpStatus.UNAUTHORIZED);
            }
            return _deleteDocente(docenteId);

        } catch (Exception e) {
            log.error("Falha ao excluir o docente {}. Erro: {}", docenteId, e.getMessage());
            return new NovoDocenteResponse(false, LocalDateTime.now(), e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    private NovoDocenteResponse _deleteDocente(Long docenteId) {
        DocenteEntity targetDocenteEntity = docenteRepository.findById(docenteId).orElse(null);
        if (Objects.isNull(targetDocenteEntity))
            return new NovoDocenteResponse(false, LocalDateTime.now() , "Docente id [" + docenteId + "] não encontrado" , null, HttpStatus.NOT_FOUND);
        else {
            docenteRepository.delete(targetDocenteEntity);
            return new NovoDocenteResponse(true, LocalDateTime.now() , "Docente id [" + docenteId + "] excluido" , null, HttpStatus.NO_CONTENT);
        }
    }

    public long count() {
        return docenteRepository.count();
    }
}
