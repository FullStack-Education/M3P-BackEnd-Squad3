package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.MateriaRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.MateriaUpdateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.MateriaResponse;
import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import br.com.fullstackedu.labpcp.database.entity.MateriaEntity;
import br.com.fullstackedu.labpcp.database.repository.CursoRepository;
import br.com.fullstackedu.labpcp.database.repository.MateriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MateriaServiceTest {

  @Mock
  private MateriaRepository materiaRepository;

  @InjectMocks
  private MateriaService materiaService;

  @Mock
  private LoginService loginService;

  @Mock
  private CursoRepository cursoRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  //insert
  @Test
  void testInsertSuccess() {
    MateriaRequest materiaRequest = new MateriaRequest("Matematica", 1L);
    MateriaEntity materiaEntity = new MateriaEntity();
    materiaEntity.setId(1L);
    materiaEntity.setNome("Matematica");

    when(materiaRepository.save(any(MateriaEntity.class))).thenReturn(materiaEntity);
    when(cursoRepository.findById(1L)).thenReturn(Optional.of(new CursoEntity()));
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.insert(materiaRequest, "validToken");

    assertEquals(HttpStatus.CREATED, response.httpStatus());
    assertEquals(true, response.success());
    verify(materiaRepository, times(1)).save(any(MateriaEntity.class));
  }

  @Test
  void testInsertFailureByAuthentication() {
    MateriaRequest materiaRequest = new MateriaRequest("Matematica", 1L);

    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("USER");

    MateriaResponse response = materiaService.insert(materiaRequest, "invalidToken");

    assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).save(any(MateriaEntity.class));
  }

  @Test
  void testInsertFailureByException() {
    MateriaRequest materiaRequest = new MateriaRequest("Matematica", 1L);

    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");
    when(cursoRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

    MateriaResponse response = materiaService.insert(materiaRequest, "validToken");

    assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).save(any(MateriaEntity.class));
  }

  @Test
  void testInsertFailureByCursoNotFound() {
    MateriaRequest materiaRequest = new MateriaRequest("Matematica", 1L);

    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");
    when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

    MateriaResponse response = materiaService.insert(materiaRequest, "validToken");

    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).save(any(MateriaEntity.class));
  }

  //getById
  @Test
  void testGetByIdSuccess() {
    MateriaEntity materiaEntity = new MateriaEntity();
    materiaEntity.setId(1L);
    materiaEntity.setNome("Matematica");

    when(materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.getById(1L, "validToken");

    assertEquals(HttpStatus.OK, response.httpStatus());
    assertEquals(true, response.success());
    verify(materiaRepository, times(1)).findById(1L);
  }

  @Test
  void testGetByIdFailureByAuthentication() {
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("USER");

    MateriaResponse response = materiaService.getById(1L, "invalidToken");

    assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).findById(anyLong());
  }

  @Test
  void testGetByIdFailureByException() {
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");
    when(materiaRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

    MateriaResponse response = materiaService.getById(1L, "validToken");

    assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(1)).findById(1L);
  }

  @Test
  void testGetByIdFailureByMateriaNotFound() {
    when(materiaRepository.findById(1L)).thenReturn(Optional.empty());
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.getById(1L, "validToken");

    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(1)).findById(1L);
  }

  //getByCursoId
  @Test
  void testGetByCursoIdSuccess() {
    CursoEntity cursoEntity = new CursoEntity();
    cursoEntity.setId(1L);
    MateriaEntity materiaEntity = new MateriaEntity();
    materiaEntity.setId(1L);
    materiaEntity.setNome("Matematica");
    materiaEntity.setCurso(cursoEntity);

    when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoEntity));
    when(materiaRepository.findByCursoId(1L)).thenReturn(List.of(materiaEntity));
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.getByCursoId(1L, "validToken");

    assertEquals(HttpStatus.OK, response.httpStatus());
    assertEquals(true, response.success());
    verify(materiaRepository, times(1)).findByCursoId(1L);
  }

  @Test
  void testGetByCursoIdFailureByAuthentication() {
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("USER");

    MateriaResponse response = materiaService.getByCursoId(1L, "invalidToken");

    assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).findByCursoId(anyLong());
  }

  @Test
  void testGetByCursoIdFailureByException() {
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");
    when(cursoRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

    MateriaResponse response = materiaService.getByCursoId(1L, "validToken");

    assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).findByCursoId(anyLong());
  }

  @Test
  void testGetByCursoIdFailureByCursoNotFound() {
    when(cursoRepository.findById(1L)).thenReturn(Optional.empty());
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.getByCursoId(1L, "validToken");

    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).findByCursoId(anyLong());
  }

  @Test
  void testGetByCursoIdFailureByNoMateriaFound() {
    CursoEntity cursoEntity = new CursoEntity();
    cursoEntity.setId(1L);

    when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoEntity));
    when(materiaRepository.findByCursoId(1L)).thenReturn(Collections.emptyList());
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.getByCursoId(1L, "validToken");

    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(1)).findByCursoId(1L);
  }
  
  //deleteMateria
  @Test
  void testDeleteMateriaSuccess() {
    MateriaEntity materiaEntity = new MateriaEntity();
    materiaEntity.setId(1L);
    materiaEntity.setNome("Matematica");

    when(materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.deleteMateria(1L, "validToken");

    assertEquals(HttpStatus.NO_CONTENT, response.httpStatus());
    assertEquals(true, response.success());
    verify(materiaRepository, times(1)).delete(materiaEntity);
  }

  @Test
  void testDeleteMateriaFailureByAuthentication() {
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("USER");

    MateriaResponse response = materiaService.deleteMateria(1L, "invalidToken");

    assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).delete(any(MateriaEntity.class));
  }

  @Test
  void testDeleteMateriaFailureByException() {
    MateriaEntity materiaEntity = new MateriaEntity();
    materiaEntity.setId(1L);
    materiaEntity.setNome("Matematica");

    when(materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");
    doThrow(new RuntimeException("Database error")).when(materiaRepository).delete(materiaEntity);

    MateriaResponse response = materiaService.deleteMateria(1L, "validToken");

    assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(1)).delete(materiaEntity);
  }

  @Test
  void testDeleteMateriaFailureByMateriaNotFound() {
    when(materiaRepository.findById(1L)).thenReturn(Optional.empty());
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.deleteMateria(1L, "validToken");

    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).delete(any(MateriaEntity.class));
  }

  //update
  @Test
  void testUpdateSuccess() {
    MateriaUpdateRequest materiaUpdateRequest = new MateriaUpdateRequest("Fisica", 1L);
    MateriaEntity materiaEntity = new MateriaEntity();
    materiaEntity.setId(1L);
    materiaEntity.setNome("Matematica");

    CursoEntity cursoEntity = new CursoEntity();
    cursoEntity.setId(1L);

    when(materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
    when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoEntity));
    when(materiaRepository.save(any(MateriaEntity.class))).thenReturn(materiaEntity);
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.update(1L, materiaUpdateRequest, "validToken");

    assertEquals(HttpStatus.OK, response.httpStatus());
    assertEquals(true, response.success());
    verify(materiaRepository, times(1)).save(any(MateriaEntity.class));
  }

  @Test
  void testUpdateFailureByAuthentication() {
    MateriaUpdateRequest materiaUpdateRequest = new MateriaUpdateRequest("Fisica", 1L);

    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("USER");

    MateriaResponse response = materiaService.update(1L, materiaUpdateRequest, "invalidToken");

    assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).save(any(MateriaEntity.class));
  }

  @Test
  void testUpdateFailureByException() {
    MateriaUpdateRequest materiaUpdateRequest = new MateriaUpdateRequest("Fisica", 1L);

    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");
    when(materiaRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

    MateriaResponse response = materiaService.update(1L, materiaUpdateRequest, "validToken");

    assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).save(any(MateriaEntity.class));
  }

  @Test
  void testUpdateFailureByMateriaNotFound() {
    MateriaUpdateRequest materiaUpdateRequest = new MateriaUpdateRequest("Fisica", 1L);

    when(materiaRepository.findById(1L)).thenReturn(Optional.empty());
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.update(1L, materiaUpdateRequest, "validToken");

    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).save(any(MateriaEntity.class));
  }

  @Test
  void testUpdateFailureByCursoNotFound() {
    MateriaUpdateRequest materiaUpdateRequest = new MateriaUpdateRequest("Fisica", 1L);
    MateriaEntity materiaEntity = new MateriaEntity();
    materiaEntity.setId(1L);
    materiaEntity.setNome("Matematica");

    when(materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
    when(cursoRepository.findById(1L)).thenReturn(Optional.empty());
    when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");

    MateriaResponse response = materiaService.update(1L, materiaUpdateRequest, "validToken");

    assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
    assertEquals(false, response.success());
    verify(materiaRepository, times(0)).save(any(MateriaEntity.class));
  }
}