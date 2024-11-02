package br.com.fullstackedu.labpcp.service;


import br.com.fullstackedu.labpcp.controller.dto.request.TurmaCreateRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.TurmaUpdateRequest;

import br.com.fullstackedu.labpcp.controller.dto.response.TurmaCreateResponse;
import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import br.com.fullstackedu.labpcp.database.entity.TurmaEntity;
import br.com.fullstackedu.labpcp.database.repository.CursoRepository;
import br.com.fullstackedu.labpcp.database.repository.DocenteRepository;
import br.com.fullstackedu.labpcp.database.repository.TurmaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class TurmaServiceTest {
    @InjectMocks
    private TurmaService turmaService;

    @Mock
    private LoginService loginService;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private DocenteRepository docenteRepository;

    @Mock
    private TurmaRepository turmaRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testNovaTurmaSuccess() throws Exception {
        TurmaCreateRequest request = new TurmaCreateRequest("Turma Teste", 1L, "10:00", LocalDate.now().plusDays(30), LocalDate.now(), null);
        CursoEntity cursoEntity = new CursoEntity();
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(cursoEntity));
        when(turmaRepository.save(any(TurmaEntity.class))).thenReturn(new TurmaEntity());

        TurmaCreateResponse response = turmaService.novaTurma(request, "validToken");

        assertTrue(response.success());
        assertEquals(HttpStatus.CREATED, response.httpStatus());
    }

    @Test
    public void testNovaTurmaUnauthorized() throws Exception {
        TurmaCreateRequest request = new TurmaCreateRequest("Turma Teste", 1L, "10:00", LocalDate.now().plusDays(30), LocalDate.now(), null);
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("USER");

        TurmaCreateResponse response = turmaService.novaTurma(request, "invalidToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    }

    @Test
    public void testGetTurmaByIdSuccess() throws Exception {
        TurmaEntity turmaEntity = new TurmaEntity();
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.of(turmaEntity));

        TurmaCreateResponse response = turmaService.getTurmaById(1L, "validToken");

        assertTrue(response.success());
        assertEquals(HttpStatus.OK, response.httpStatus());
    }

    @Test
    public void testGetTurmaByIdNotFound() throws Exception {
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.empty());

        TurmaCreateResponse response = turmaService.getTurmaById(1L, "validToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }

    @Test
    public void testUpdateTurmaSuccess() throws Exception {
        TurmaUpdateRequest updateRequest = new TurmaUpdateRequest("Updated Turma", 1L, null, "10:00", LocalDate.now().plusDays(30), LocalDate.now());
        TurmaEntity turmaEntity = new TurmaEntity();
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.of(turmaEntity));
        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(new CursoEntity()));
        when(turmaRepository.save(any(TurmaEntity.class))).thenReturn(turmaEntity);

        TurmaCreateResponse response = turmaService.updateTurma(1L, updateRequest, "validToken");

        assertTrue(response.success());
        assertEquals(HttpStatus.OK, response.httpStatus());
    }

    @Test
    public void testDeleteTurmaSuccess() throws Exception {
        TurmaEntity turmaEntity = new TurmaEntity();
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.of(turmaEntity));

        TurmaCreateResponse response = turmaService.deleteTurma(1L, "validToken");

        assertTrue(response.success());
        assertEquals(HttpStatus.NO_CONTENT, response.httpStatus());
    }

    @Test
    public void testDeleteTurmaUnauthorized() throws Exception {
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("USER");

        TurmaCreateResponse response = turmaService.deleteTurma(1L, "invalidToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    }

    @Test
    public void testGetAllTurmasSuccess() throws Exception {
        TurmaEntity turmaEntity = new TurmaEntity();
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        when(turmaRepository.findAll()).thenReturn(List.of(turmaEntity));

        TurmaCreateResponse response = turmaService.getAllTurmas("validToken");

        assertTrue(response.success());
        assertEquals(HttpStatus.OK, response.httpStatus());
    }

    @Test
    public void testGetAllTurmasEmpty() throws Exception {
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        when(turmaRepository.findAll()).thenReturn(Collections.emptyList());

        TurmaCreateResponse response = turmaService.getAllTurmas("validToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    }

    @Test
    public void testGetAllFailureByAuthentication() {
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("USER");

        TurmaCreateResponse response = turmaService.getAllTurmas("invalidToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("Usuários com papel [USER] não tem acesso a essa funcionalidade", response.message());
        assertNull(response.turmaData());
    }

    @Test
    public void testGetAllFailureByException() {
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        doThrow(new RuntimeException("Database error")).when(turmaRepository).findAll();

        TurmaCreateResponse response = turmaService.getAllTurmas("validToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals("Database error", response.message());
        assertNull(response.turmaData());
    }

    @Test
    public void testGetAllFailureByNotFound() {
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        when(turmaRepository.findAll()).thenReturn(Collections.emptyList());

        TurmaCreateResponse response = turmaService.getAllTurmas("validToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Não há turmas cadastradas.", response.message());
        assertNull(response.turmaData());
    }

    @Test
    public void testUpdateFailureByAuthentication() {
        TurmaUpdateRequest updateRequest = new TurmaUpdateRequest("Updated Turma", 1L, null, "10:00", LocalDate.now().plusDays(30), LocalDate.now());
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("USER");

        TurmaCreateResponse response = turmaService.updateTurma(1L, updateRequest, "invalidToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("Usuários com papel [USER] não tem acesso a essa funcionalidade", response.message());
        assertNull(response.turmaData());
    }

    @Test
    public void testUpdateFailureByException() {
        TurmaUpdateRequest updateRequest = new TurmaUpdateRequest("Updated Turma", 1L, null, "10:00", LocalDate.now().plusDays(30), LocalDate.now());
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        doThrow(new RuntimeException("Database error")).when(turmaRepository).findById(anyLong());

        TurmaCreateResponse response = turmaService.updateTurma(1L, updateRequest, "validToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals("Database error", response.message());
        assertNull(response.turmaData());
    }

    @Test
    public void testUpdateFailureByNotFound() {
        TurmaUpdateRequest updateRequest = new TurmaUpdateRequest("Updated Turma", 1L, null, "10:00", LocalDate.now().plusDays(30), LocalDate.now());
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.empty());

        TurmaCreateResponse response = turmaService.updateTurma(1L, updateRequest, "validToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Turma id [1] não encontrada", response.message());
        assertNull(response.turmaData());
    }

    @Test
    public void testDeleteTurmaFailureByAuthentication() {
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("USER");

        TurmaCreateResponse response = turmaService.deleteTurma(1L, "invalidToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("Usuários com papel [USER] não tem acesso a essa funcionalidade", response.message());
        assertNull(response.turmaData());
    }

    @Test
    public void testDeleteTurmaFailureByException() {
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        doThrow(new RuntimeException("Database error")).when(turmaRepository).findById(anyLong());

        TurmaCreateResponse response = turmaService.deleteTurma(1L, "validToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals("Database error", response.message());
        assertNull(response.turmaData());
    }

    @Test
    public void testDeleteTurmaFailureByNotFound() {
        when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.empty());

        TurmaCreateResponse response = turmaService.deleteTurma(1L, "validToken");

        assertFalse(response.success());
        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Turma id [1] não encontrada", response.message());
        assertNull(response.turmaData());
    }
}
