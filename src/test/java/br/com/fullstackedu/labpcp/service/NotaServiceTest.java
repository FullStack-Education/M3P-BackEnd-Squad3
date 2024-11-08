package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.NotaRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.NotaUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.AlunoScoreResponse;
import br.com.fullstackedu.labpcp.controller.dto.response.NotaResponse;
import br.com.fullstackedu.labpcp.database.entity.*;
import br.com.fullstackedu.labpcp.database.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotaServiceTest {
    @Mock
    private NotaRepository notaRepository;

    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private AlunoEntity alunoEntity;

    @Mock
    private DocenteRepository docenteRepository;
    @Mock
    private DocenteEntity docenteEntity;

    @Mock
    private PapelEntity papelEntity;

    @Mock
    private UsuarioEntity usuarioEntity;

    @Mock
    private LoginService loginService;

    @Mock
    private NotaRequest notaRequest;
    @Mock
    private NotaUpdateRequest notaUpdateRequest;
    @Mock
    private NotaEntity notaEntity;
    @Mock
    private List<NotaEntity> notaList;

    @Mock
    private MateriaRepository materiaRepository;
    @Mock
    private MateriaEntity materiaEntity;

    @Mock
    private TurmaRepository turmaRepository;
    @Mock
    private TurmaEntity turmaEntity;

    @InjectMocks
    private NotaService notaService;

    @Test
    void testInsertNota_Success() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        when(alunoRepository.findById(any(Long.class))).thenReturn(Optional.of(alunoEntity));
        when(docenteRepository.findById(any(Long.class))).thenReturn(Optional.of(docenteEntity));

        when(docenteEntity.getUsuario()).thenReturn(usuarioEntity);
        when(usuarioEntity.getPapel()).thenReturn(papelEntity);
        when(papelEntity.getNome()).thenReturn("PROFESSOR");

        when(materiaRepository.findById(any(Long.class))).thenReturn(Optional.of(materiaEntity));
        when(turmaRepository.findById(any(Long.class))).thenReturn(Optional.of(turmaEntity));
        when(notaRepository.save(any(NotaEntity.class))).thenReturn(notaEntity);

        NotaResponse response = notaService.insert(notaRequest, token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.CREATED, response.httpStatus());
        assertEquals(true, response.success());
        assertEquals("Nota adicionada com sucesso.", response.message());
    }

    @Test
    void testInsertNota_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ANY");

        NotaResponse response = notaService.insert(notaRequest, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("O Usuário logado não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testInsertNota_BadRequest() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        RuntimeException exception = new RuntimeException("Simulated Bad Request");

        when(alunoRepository.findById(any(Long.class))).thenThrow(exception);

        NotaResponse response = notaService.insert(notaRequest, token);
        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals(exception.getMessage(), response.message());
    }

    @Test
    void testInsertNota_AlunoNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        NotaResponse response = notaService.insert(notaRequest, token);
        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Erro ao cadastrar nota: Nenhum aluno com id [" + notaRequest.id_aluno() + "] encontrado",
                response.message());
    }

    @Test
    void testInsertNota_ProfessorNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        when(alunoRepository.findById(any(Long.class))).thenReturn(Optional.of(alunoEntity));

        NotaResponse response = notaService.insert(notaRequest, token);
        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Erro ao cadastrar nota: Nenhum professor com id [" + notaRequest.id_aluno() + "] encontrado",
                response.message());
    }

    @Test
    void testInsertNota_ProfessorPapelNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        when(alunoRepository.findById(any(Long.class))).thenReturn(Optional.of(alunoEntity));
        when(docenteRepository.findById(any(Long.class))).thenReturn(Optional.of(docenteEntity));

        when(docenteEntity.getUsuario()).thenReturn(usuarioEntity);
        when(usuarioEntity.getPapel()).thenReturn(papelEntity);
        when(papelEntity.getNome()).thenReturn("");

        NotaResponse response = notaService.insert(notaRequest, token);
        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Erro ao cadastrar nota: Docente Id [" + notaRequest.id_aluno() + "] não possui o papel de Professor",
                response.message());
    }

    @Test
    void testInsertNota_MateriaNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        when(alunoRepository.findById(any(Long.class))).thenReturn(Optional.of(alunoEntity));
        when(docenteRepository.findById(any(Long.class))).thenReturn(Optional.of(docenteEntity));

        when(docenteEntity.getUsuario()).thenReturn(usuarioEntity);
        when(usuarioEntity.getPapel()).thenReturn(papelEntity);
        when(papelEntity.getNome()).thenReturn("PROFESSOR");

        NotaResponse response = notaService.insert(notaRequest, token);
        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Erro ao cadastrar nota: Nenhuma materia com id [" + notaRequest.id_materia() + "] encontrada",
                response.message());
    }

    @Test
    void testInsertNota_TurmaNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        when(alunoRepository.findById(any(Long.class))).thenReturn(Optional.of(alunoEntity));
        when(docenteRepository.findById(any(Long.class))).thenReturn(Optional.of(docenteEntity));

        when(docenteEntity.getUsuario()).thenReturn(usuarioEntity);
        when(usuarioEntity.getPapel()).thenReturn(papelEntity);
        when(papelEntity.getNome()).thenReturn("PROFESSOR");

        when(materiaRepository.findById(any(Long.class))).thenReturn(Optional.of(materiaEntity));

        NotaResponse response = notaService.insert(notaRequest, token);
        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Erro ao cadastrar nota: Nenhuma turma com id [" + notaRequest.id_materia() + "] encontrada",
                response.message());
    }

    @Test
    void testGetNota_CommonSuccess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        when(notaRepository.findById(any(Long.class))).thenReturn(Optional.of(notaEntity));

        NotaResponse response = notaService.getById(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals(true, response.success());
        assertEquals("Nota encontrada", response.message());
    }

    @Test
    void testGetNota_OwnerSuccess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");
        when(loginService.getFieldInToken(token, "id_usuario")).thenReturn("0");
        when(alunoRepository.findByUsuarioId(0L)).thenReturn(Optional.of(alunoEntity));

        when(notaRepository.findByIdAndAlunoId(notaEntity.getId(), alunoEntity.getId())).thenReturn(Optional.of(notaEntity));

        NotaResponse response = notaService.getById(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals(true, response.success());
        assertEquals("Nota encontrada", response.message());
    }

    @Test
    void testGetNota_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ANY");

        NotaResponse response = notaService.getById(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("O Usuário logado não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testGetNota_BadRequest() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        RuntimeException exception = new RuntimeException("Simulated Bad Request");

        when(notaRepository.findById(any(Long.class))).thenThrow(exception);

        NotaResponse response = notaService.getById(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals(exception.getMessage(), response.message());
    }

    @Test
    void testGetNota_CommonNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        when(notaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotaResponse response = notaService.getById(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Nota ID "+ notaEntity.getId() +" não encontrada.", response.message());
    }

    @Test
    void testGetNota_OwnerAlunoNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");
        when(loginService.getFieldInToken(token, "id_usuario")).thenReturn("0");
        when(alunoRepository.findByUsuarioId(0L)).thenReturn(Optional.empty());

        NotaResponse response = notaService.getById(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Nenhum Aluno foi encontrado para o usuário logado com id [" + notaEntity.getId() + "]", response.message());
    }

    @Test
    void testGetNota_OwnerNotaNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");
        when(loginService.getFieldInToken(token, "id_usuario")).thenReturn("0");
        when(alunoRepository.findByUsuarioId(0L)).thenReturn(Optional.of(alunoEntity));

        when(notaRepository.findByIdAndAlunoId(notaEntity.getId(), alunoEntity.getId())).thenReturn(Optional.empty());

        NotaResponse response = notaService.getById(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Nota ID " + notaEntity.getId() + " não encontrada para este aluno.", response.message());
    }

    @Test
    void testGetNotaByAlunoId_CommonSuccess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        when(notaRepository.findByAlunoId(alunoEntity.getId())).thenReturn(notaList);

        NotaResponse response = notaService.getByAlunoId(alunoEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals(true, response.success());
        assertEquals("Notas encontradas: "+ notaList.size(), response.message());
    }

    @Test
    void testGetNotaByAlunoId_OwnerSuccess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");
        when(loginService.getFieldInToken(token, "id_usuario")).thenReturn("0");
        when(alunoRepository.findByUsuarioId(0L)).thenReturn(Optional.of(alunoEntity));

        when(notaRepository.findByAlunoId(alunoEntity.getId())).thenReturn(notaList);

        NotaResponse response = notaService.getByAlunoId(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals(true, response.success());
        assertEquals("Notas encontradas: "+ notaList.size(), response.message());
    }

    @Test
    void testGetNotaByAlunoId_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ANY");

        NotaResponse response = notaService.getByAlunoId(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("O Usuário logado não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testGetNotaByAlunoId_UnauthorizedAccessFromOtherStudents() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");
        when(loginService.getFieldInToken(token, "id_usuario")).thenReturn("0");
        when(alunoRepository.findByUsuarioId(0L)).thenReturn(Optional.of(alunoEntity));

        when(alunoRepository.findByUsuarioId(0L)).thenReturn(Optional.empty());

        NotaResponse response = notaService.getByAlunoId(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Alunos logados tem acesso someone a suas próprias notas.", response.message());
    }

    @Test
    void testGetNotaByAlunoId_BadRequest() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        RuntimeException exception = new RuntimeException("Simulated Bad Request");

        when(notaRepository.findByAlunoId(alunoEntity.getId())).thenThrow(exception);

        NotaResponse response = notaService.getByAlunoId(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals(exception.getMessage(), response.message());
    }

    @Test
    void testGetNotaByAlunoId_NotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        List<NotaEntity> emptyNotaList = new ArrayList<>();

        when(notaRepository.findByAlunoId(alunoEntity.getId())).thenReturn(emptyNotaList);

        NotaResponse response = notaService.getByAlunoId(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Nenhuma Nota encontrada para o Aluno ID "+ notaEntity.getId(), response.message());
    }

    @Test
    void testUpdateNota_Success() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        when(notaRepository.findById(any(Long.class))).thenReturn(Optional.of(notaEntity));
        when(alunoRepository.findById(any(Long.class))).thenReturn(Optional.of(alunoEntity));
        when(materiaRepository.findById(any(Long.class))).thenReturn(Optional.of(materiaEntity));
        when(docenteRepository.findById(any(Long.class))).thenReturn(Optional.of(docenteEntity));
        when(turmaRepository.findById(any(Long.class))).thenReturn(Optional.of(turmaEntity));
        when(notaRepository.save(any(NotaEntity.class))).thenReturn(notaEntity);

        NotaResponse response = notaService.updateNota(notaEntity.getId(), notaUpdateRequest, token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals(true, response.success());
        assertEquals("Nota atualizada", response.message());
    }

    @Test
    void testUpdateNota_NotaNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(notaRepository.findById(1L)).thenReturn(Optional.empty());

        NotaResponse response = notaService.updateNota(1L, notaUpdateRequest, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Nota id [1] não encontrado", response.message());
    }

    @Test
    void testUpdateNota_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");

        NotaResponse response = notaService.updateNota(1L, notaUpdateRequest, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("O Usuário logado não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testUpdateNota_AlunoNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        NotaEntity nota = new NotaEntity();
        when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));

        when(alunoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotaResponse response = notaService.updateNota(1L, notaUpdateRequest, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Aluno id ["+ notaUpdateRequest.id_materia() +"] não encontrado", response.message());
    }

    @Test
    void testUpdateNota_MateriaNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        NotaEntity nota = new NotaEntity();
        when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));

        when(alunoRepository.findById(any(Long.class))).thenReturn(Optional.of(alunoEntity));
        when(materiaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotaResponse response = notaService.updateNota(1L, notaUpdateRequest, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Materia id ["+ notaUpdateRequest.id_materia() +"] não encontrado", response.message());
    }

    @Test
    void testUpdateNota_DocenteNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        NotaEntity nota = new NotaEntity();
        when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));

        when(alunoRepository.findById(any(Long.class))).thenReturn(Optional.of(alunoEntity));
        when(materiaRepository.findById(any(Long.class))).thenReturn(Optional.of(materiaEntity));
        when(docenteRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotaResponse response = notaService.updateNota(1L, notaUpdateRequest, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Docente id ["+ notaUpdateRequest.id_professor() +"] não encontrado", response.message());
    }

    @Test
    void testUpdateNota_TurmaNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        NotaEntity nota = new NotaEntity();
        when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));

        when(alunoRepository.findById(any(Long.class))).thenReturn(Optional.of(alunoEntity));
        when(materiaRepository.findById(any(Long.class))).thenReturn(Optional.of(materiaEntity));
        when(docenteRepository.findById(any(Long.class))).thenReturn(Optional.of(docenteEntity));
        when(turmaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotaResponse response = notaService.updateNota(1L, notaUpdateRequest, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Turma id ["+ notaUpdateRequest.id_materia() +"] não encontrado", response.message());
    }

    @Test
    void testUpdateNota_FailureByException() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(notaRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        NotaResponse response = notaService.updateNota(1L, notaUpdateRequest, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Database error", response.message());
    }

    @Test
    void testDeleteNota_Success() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        NotaEntity nota = new NotaEntity();
        when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));

        NotaResponse response = notaService.deleteNota(1L, token);

        assertEquals(HttpStatus.NO_CONTENT, response.httpStatus());
        assertEquals("Nota id [1] excluido", response.message());
    }

    @Test
    void testDeleteNota_FailureByException() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(notaRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));


        NotaResponse respose =  notaService.deleteNota(1L, token);
        assertEquals("Database error", respose.message());
    }

    @Test
    void testDeleteNota_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");

        NotaResponse response = notaService.deleteNota(1L, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("O Usuário logado não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testDeleteNota_NotaNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(notaRepository.findById(1L)).thenReturn(Optional.empty());

        NotaResponse response = notaService.deleteNota(1L, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Nota id [1] não encontrada", response.message());
    }

    @Test
    void testGetScoreByAlunoId_CommonSuccess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        when(alunoRepository.findById(alunoEntity.getId())).thenReturn(Optional.of(alunoEntity));
        when(notaRepository.findByAlunoId(alunoEntity.getId())).thenReturn(notaList);

        AlunoScoreResponse response = notaService.getScoreByAlunoId(alunoEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals(true, response.success());
        assertEquals("Pontuação calculada com sucesso", response.message());
    }

    @Test
    void testGetScoreByAlunoId_OwnerSuccess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");
        when(loginService.getFieldInToken(token, "id_usuario")).thenReturn("0");
        when(alunoRepository.findByUsuarioId(0L)).thenReturn(Optional.of(alunoEntity));

        when(alunoRepository.findById(alunoEntity.getId())).thenReturn(Optional.of(alunoEntity));
        when(notaRepository.findByAlunoId(alunoEntity.getId())).thenReturn(notaList);

        AlunoScoreResponse response = notaService.getScoreByAlunoId(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals(true, response.success());
        assertEquals("Pontuação calculada com sucesso", response.message());
    }

    @Test
    void testGetScoreByAlunoId_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ANY");

        AlunoScoreResponse response = notaService.getScoreByAlunoId(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("O Usuário logado não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testGetScoreByAlunoId_UnauthorizedAccessFromOtherStudents() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");
        when(loginService.getFieldInToken(token, "id_usuario")).thenReturn("0");
        when(alunoRepository.findByUsuarioId(0L)).thenReturn(Optional.empty());

        AlunoScoreResponse response = notaService.getScoreByAlunoId(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Alunos logados tem acesso someone a suas próprias notas e pontuações.", response.message());
    }

    @Test
    void testGetScoreByAlunoId_BadRequest() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        RuntimeException exception = new RuntimeException("Simulated Bad Request");

        when(alunoRepository.findById(alunoEntity.getId())).thenThrow(exception);

        AlunoScoreResponse response = notaService.getScoreByAlunoId(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals(exception.getMessage(), response.message());
    }

    @Test
    void testGetScoreByAlunoId_NotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");

        List<NotaEntity> emptyNotaList = new ArrayList<>();

        when(alunoRepository.findById(alunoEntity.getId())).thenReturn(Optional.of(alunoEntity));
        when(notaRepository.findByAlunoId(alunoEntity.getId())).thenReturn(emptyNotaList);

        AlunoScoreResponse response = notaService.getScoreByAlunoId(notaEntity.getId(), token);

        System.out.println("Mensagem da resposta: "+ response.message());

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals(false, response.success());
        assertEquals("Nenhuma Nota encontrada para o Aluno ID "+ notaEntity.getId(), response.message());
    }
}
