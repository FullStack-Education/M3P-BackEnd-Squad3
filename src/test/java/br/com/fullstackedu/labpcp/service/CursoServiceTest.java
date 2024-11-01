package br.com.fullstackedu.labpcp.service;
import static org.junit.jupiter.api.Assertions.*;

import br.com.fullstackedu.labpcp.database.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import br.com.fullstackedu.labpcp.controller.dto.request.CursoRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.CursoResponse;
import br.com.fullstackedu.labpcp.database.entity.CursoEntity;

import org.springframework.http.HttpStatus;


public class CursoServiceTest {

  @InjectMocks
  private CursoService cursoService;

  @Mock
  private LoginService loginService;

  @Mock
  private CursoRepository cursoRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testInsertCursoSuccess() {
    CursoRequest cursoRequest = new CursoRequest("Curso Teste");
    CursoEntity cursoEntity = new CursoEntity();
    cursoEntity.setNome("Curso Teste");
    cursoEntity.setId(1L);

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    when(cursoRepository.save(any(CursoEntity.class))).thenReturn(cursoEntity);

    CursoResponse response = cursoService.insertCurso(cursoRequest, "validToken");

    assertTrue(response.success());
    assertEquals(HttpStatus.CREATED, response.httpStatus());
    assertEquals("Curso adicionada com sucesso.", response.message());
    assertNotNull(response.cursoData());
  }

  @Test
  public void testInsertCursoFailureByAuthentication() {
    CursoRequest cursoRequest = new CursoRequest("Curso Teste");

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("USER");

    CursoResponse response = cursoService.insertCurso(cursoRequest, "invalidToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    assertEquals("Usuários logado não tem acesso a essa funcionalidade", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testInsertCursoFailureByException() {
    CursoRequest cursoRequest = new CursoRequest("Curso Teste");

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    doThrow(new RuntimeException("Database error")).when(cursoRepository).save(any(CursoEntity.class));

    CursoResponse response = cursoService.insertCurso(cursoRequest, "validToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
    assertEquals("Database error", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testGetByIdSuccess() {
    Long cursoId = 1L;
    CursoEntity cursoEntity = new CursoEntity();
    cursoEntity.setId(cursoId);
    cursoEntity.setNome("Curso Teste");

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    when(cursoRepository.findById(cursoId)).thenReturn(java.util.Optional.of(cursoEntity));

    CursoResponse response = cursoService.getById(cursoId, "validToken");

    assertTrue(response.success());
    assertEquals(HttpStatus.OK, response.httpStatus());
    assertEquals("Curso encontrado", response.message());
    assertNotNull(response.cursoData());
  }

  @Test
  public void testGetByIdFailureByAuthentication() {
    Long cursoId = 1L;

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("USER");

    CursoResponse response = cursoService.getById(cursoId, "invalidToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    assertEquals("Usuário logado não tem acesso a essa funcionalidade", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testGetByIdFailureByException() {
    Long cursoId = 1L;

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    doThrow(new RuntimeException("Database error")).when(cursoRepository).findById(cursoId);

    CursoResponse response = cursoService.getById(cursoId, "validToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
    assertEquals("Database error", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testGetByIdFailureByNotFound() {
    Long cursoId = 1L;

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    when(cursoRepository.findById(cursoId)).thenReturn(java.util.Optional.empty());

    CursoResponse response = cursoService.getById(cursoId, "validToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals("Curso ID " + cursoId + " não encontrado.", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testGetAllSuccess() {
    CursoEntity cursoEntity1 = new CursoEntity();
    cursoEntity1.setId(1L);
    cursoEntity1.setNome("Curso Teste 1");

    CursoEntity cursoEntity2 = new CursoEntity();
    cursoEntity2.setId(2L);
    cursoEntity2.setNome("Curso Teste 2");

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    when(cursoRepository.findAll()).thenReturn(List.of(cursoEntity1, cursoEntity2));

    CursoResponse response = cursoService.getAll("validToken");

    assertTrue(response.success());
    assertEquals(HttpStatus.OK, response.httpStatus());
    assertEquals("Cursos encontrados: 2", response.message());
    assertNotNull(response.cursoData());
    assertEquals(2, response.cursoData().size());
  }

  @Test
  public void testGetAllFailureByAuthentication() {
    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("USER");

    CursoResponse response = cursoService.getAll("invalidToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    assertEquals("Usuário logado não tem acesso a essa funcionalidade", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testGetAllFailureByException() {
    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    doThrow(new RuntimeException("Database error")).when(cursoRepository).findAll();

    CursoResponse response = cursoService.getAll("validToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
    assertEquals("Database error", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testGetAllFailureByNotFound() {
    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    when(cursoRepository.findAll()).thenReturn(Collections.emptyList());

    CursoResponse response = cursoService.getAll("validToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals("Não há cursos cadastrados.", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testUpdateSuccess() {
    Long cursoId = 1L;
    CursoRequest cursoRequest = new CursoRequest("Curso Atualizado");
    CursoEntity cursoEntity = new CursoEntity();
    cursoEntity.setId(cursoId);
    cursoEntity.setNome("Curso Antigo");

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    when(cursoRepository.findById(cursoId)).thenReturn(java.util.Optional.of(cursoEntity));
    when(cursoRepository.save(any(CursoEntity.class))).thenReturn(cursoEntity);

    CursoResponse response = cursoService.update(cursoId, cursoRequest, "validToken");

    assertTrue(response.success());
    assertEquals(HttpStatus.OK, response.httpStatus());
    assertEquals("Curso atualizado", response.message());
    assertNotNull(response.cursoData());
  }

  @Test
  public void testUpdateFailureByAuthentication() {
    Long cursoId = 1L;
    CursoRequest cursoRequest = new CursoRequest("Curso Atualizado");

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("USER");

    CursoResponse response = cursoService.update(cursoId, cursoRequest, "invalidToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    assertEquals("Usuário logado não tem acesso a essa funcionalidade", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testUpdateFailureByException() {
    Long cursoId = 1L;
    CursoRequest cursoRequest = new CursoRequest("Curso Atualizado");

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    doThrow(new RuntimeException("Database error")).when(cursoRepository).findById(cursoId);

    CursoResponse response = cursoService.update(cursoId, cursoRequest, "validToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
    assertEquals("Database error", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testUpdateFailureByNotFound() {
    Long cursoId = 1L;
    CursoRequest cursoRequest = new CursoRequest("Curso Atualizado");

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    when(cursoRepository.findById(cursoId)).thenReturn(java.util.Optional.empty());

    CursoResponse response = cursoService.update(cursoId, cursoRequest, "validToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals("Curso id [" + cursoId + "] não encontrado", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testDeleteCursoSuccess() {
    Long cursoId = 1L;
    CursoEntity cursoEntity = new CursoEntity();
    cursoEntity.setId(cursoId);
    cursoEntity.setNome("Curso Teste");

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    when(cursoRepository.findById(cursoId)).thenReturn(java.util.Optional.of(cursoEntity));

    CursoResponse response = cursoService.deleteCurso(cursoId, "validToken");

    assertTrue(response.success());
    assertEquals(HttpStatus.NO_CONTENT, response.httpStatus());
    assertEquals("Curso id [" + cursoId + "] excluido", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testDeleteCursoFailureByAuthentication() {
    Long cursoId = 1L;

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("USER");

    CursoResponse response = cursoService.deleteCurso(cursoId, "invalidToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    assertEquals("Usuário logado não tem acesso a essa funcionalidade", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testDeleteCursoFailureByException() {
    Long cursoId = 1L;

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    doThrow(new RuntimeException("Database error")).when(cursoRepository).findById(cursoId);

    CursoResponse response = cursoService.deleteCurso(cursoId, "validToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
    assertEquals("Database error", response.message());
    assertNull(response.cursoData());
  }

  @Test
  public void testDeleteCursoFailureByNotFound() {
    Long cursoId = 1L;

    when(loginService.getFieldInToken(anyString(), anyString())).thenReturn("ADM");
    when(cursoRepository.findById(cursoId)).thenReturn(java.util.Optional.empty());

    CursoResponse response = cursoService.deleteCurso(cursoId, "validToken");

    assertFalse(response.success());
    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals("Curso id [" + cursoId + "] não encontrada", response.message());
    assertNull(response.cursoData());
  }
}

  

  /* @Test
  public void testCreateCurso() {
    // Add your test logic here
  }

  @Test
  public void testUpdateCurso() {
    // Add your test logic here
  }

  @Test
  public void testDeleteCurso() {
    // Add your test logic here
  }
  */
