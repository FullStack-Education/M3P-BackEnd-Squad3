package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.AlunoRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.AlunoUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.DocenteCreateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.AlunoResponse;
import br.com.fullstackedu.labpcp.controller.dto.response.CursoResponse;
import br.com.fullstackedu.labpcp.controller.dto.response.NovoDocenteResponse;
import br.com.fullstackedu.labpcp.database.entity.AlunoEntity;
import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import br.com.fullstackedu.labpcp.database.entity.TurmaEntity;
import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.database.repository.AlunoRepository;
import br.com.fullstackedu.labpcp.database.repository.CursoRepository;
import br.com.fullstackedu.labpcp.database.repository.TurmaRepository;
import br.com.fullstackedu.labpcp.database.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import br.com.fullstackedu.labpcp.controller.dto.response.DashboardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


class AlunoServiceTest {
    @Mock
    private LoginService loginService;


    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private TurmaRepository turmaRepository;
    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private AlunoResponse alunoResponse;

    @Mock
    private AlunoRequest alunoRequest;

    @Mock
    private AlunoUpdateRequest alunoUpdateRequest;

    @Mock
    private CursoResponse cursoResponse;

    @InjectMocks
    private AlunoService alunoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void insertAlunoSucess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        AlunoRequest request = alunoRequest;
        UsuarioEntity usuario = new UsuarioEntity();
        TurmaEntity turma = new TurmaEntity();
        when(usuarioRepository.findById(request.id_usuario())).thenReturn(Optional.of(usuario));
        when(turmaRepository.findById(request.id_turma())).thenReturn(Optional.of(turma));
        AlunoResponse response = alunoService.insertAluno(request, token);
        assertEquals(HttpStatus.CREATED, response.httpStatus());
    }

    @Test
    void insertAlunoInvalidToken() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("invalid");
        AlunoRequest request = alunoRequest;
        AlunoResponse response = alunoService.insertAluno(request, token);
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    }

    @Test
    void insertAlunoInvalidUser() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        AlunoRequest request = alunoRequest;
        when(usuarioRepository.findById(request.id_usuario())).thenReturn(Optional.empty());
        AlunoResponse response = alunoService.insertAluno(request, token);
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }

    @Test
    void insertAlunoInvalidTurma() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        AlunoRequest request = alunoRequest;
        UsuarioEntity usuario = new UsuarioEntity();
        when(usuarioRepository.findById(request.id_usuario())).thenReturn(Optional.of(usuario));
        when(turmaRepository.findById(request.id_turma())).thenReturn(Optional.empty());
        AlunoResponse response = alunoService.insertAluno(request, token);
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }


    @Test
    void getByIdSucess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(new AlunoEntity()));
        AlunoResponse response = alunoService.getById(1L, token);
        assertEquals(HttpStatus.OK, response.httpStatus());
    }

    @Test
    void getByIdInvalidUser() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("invalid");
        AlunoResponse response = alunoService.getById(1L, token);
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    }

    @Test
    void getByIdNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());
        AlunoResponse response = alunoService.getById(1L, token);
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }

    @Test
    void updateAlunoSucess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        AlunoUpdateRequest request = alunoUpdateRequest;
        AlunoEntity aluno = new AlunoEntity();
        UsuarioEntity usuario = new UsuarioEntity();
        TurmaEntity turma = new TurmaEntity();
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(usuarioRepository.findById(request.id_usuario())).thenReturn(Optional.of(usuario));
        when(turmaRepository.findById(request.id_turma())).thenReturn(Optional.of(turma));
        AlunoResponse response = alunoService.updateAluno(1L, request, token);
        assertEquals(HttpStatus.OK, response.httpStatus());
    }

    @Test
    void updateAlunoInvalidUser() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("invalid");
        AlunoUpdateRequest request = alunoUpdateRequest;
        AlunoResponse response = alunoService.updateAluno(1L, request, token);
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());

    }


    @Test
    void updateAlunoAlunoNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        AlunoUpdateRequest request = alunoUpdateRequest;
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());
        AlunoResponse response = alunoService.updateAluno(1L, request, token);
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }

    @Test
    void updateAlunoTurmaNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        AlunoUpdateRequest request = alunoUpdateRequest;
        AlunoEntity aluno = new AlunoEntity();
        UsuarioEntity usuario = new UsuarioEntity();
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(usuarioRepository.findById(request.id_usuario())).thenReturn(Optional.of(usuario));
        when(turmaRepository.findById(request.id_turma())).thenReturn(Optional.empty());
        AlunoResponse response = alunoService.updateAluno(1L, request, token);
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }

    @Test
    void updateAlunoUserNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        AlunoUpdateRequest request = alunoUpdateRequest;
        AlunoEntity aluno = new AlunoEntity();
        TurmaEntity turma = new TurmaEntity();
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(usuarioRepository.findById(request.id_usuario())).thenReturn(Optional.empty());
        when(turmaRepository.findById(request.id_turma())).thenReturn(Optional.of(turma));
        AlunoResponse response = alunoService.updateAluno(1L, request, token);
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }

    @Test
    void getAllAlunosSucess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(alunoRepository.findAll()).thenReturn(List.of(new AlunoEntity()));
        AlunoResponse response = alunoService.getAllAlunos(token);
        assertEquals(HttpStatus.OK, response.httpStatus());
    }


    @Test
    void getAllAlunosEmpytList() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(alunoRepository.findAll()).thenReturn(Collections.emptyList());
        AlunoResponse response = alunoService.getAllAlunos(token);
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }

    @Test
    void getAllAlunosInvalidUser() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("invalid");
        AlunoResponse response = alunoService.getAllAlunos(token);
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    }

    @Test
    void deleteAlunoSucess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(new AlunoEntity()));
        AlunoResponse response = alunoService.deleteAluno(1L, token);
        assertEquals(HttpStatus.NO_CONTENT, response.httpStatus());
    }


    @Test
    void deleteAlunoAlunoNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());
        AlunoResponse response = alunoService.deleteAluno(1L, token);
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }


    @Test
    void deleteAlunoInvalidUser() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("invalid");
        AlunoResponse response = alunoService.deleteAluno(1L, token);
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    }


    @Test
    void count() {
        var count = alunoService.count();
        assertInstanceOf(Long.class, count, "O tipo do retorno deve ser Long");
    }

    @Test
    void getCourseByAlunoIdSucess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        AlunoEntity aluno = new AlunoEntity();
        TurmaEntity turma = new TurmaEntity();
        CursoEntity curso = new CursoEntity();
        aluno.setTurma(turma);
        turma.setCurso(curso);
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        CursoResponse response = alunoService.getCourseByAlunoId(1L, token);
        assertEquals(HttpStatus.OK, response.httpStatus());
    }

    @Test
    void getCourseByAlunoAlunoNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());
        CursoResponse response = alunoService.getCourseByAlunoId(1L, token);
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }

    @Test
    void getCourseByAlunoInvalidUser() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("invalid");
        CursoResponse response = alunoService.getCourseByAlunoId(1L, token);
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    }


    @Test
    void getExtraCoursesByAlunoIdSucess() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        AlunoEntity aluno = new AlunoEntity();
        TurmaEntity turma = new TurmaEntity();
        CursoEntity curso = new CursoEntity();
        aluno.setTurma(turma);
        turma.setCurso(curso);
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findByIdNot(1L)).thenReturn(List.of(new CursoEntity()));
        CursoResponse response = alunoService.getExtraCoursesByAlunoId(1L, token);
        assertEquals(HttpStatus.OK, response.httpStatus());
    }

    @Test
    void getExtraCoursesByAlunoIdAlunoNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());
        CursoResponse response = alunoService.getExtraCoursesByAlunoId(1L, token);
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }

    @Test
    void getExtraCoursesByAlunoIdIvalidUser() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("invalid");
        CursoResponse response = alunoService.getExtraCoursesByAlunoId(1L, token);
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    }
}