package br.com.fullstackedu.labpcp.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import br.com.fullstackedu.labpcp.controller.dto.request.DocenteCreateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.NovoDocenteResponse;
import br.com.fullstackedu.labpcp.database.repository.DocenteRepository;
import br.com.fullstackedu.labpcp.database.repository.MateriaRepository;
import br.com.fullstackedu.labpcp.database.repository.UsuarioRepository;
import br.com.fullstackedu.labpcp.database.entity.DocenteEntity;
import br.com.fullstackedu.labpcp.database.entity.MateriaEntity;
import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import java.util.HashSet;
import java.util.Optional;

public class DocenteServiceTest {

    @Mock
    private DocenteRepository docenteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LoginService loginService;

    @Mock
    private NovoDocenteResponse novoDocenteResponse;

    @Mock
    private DocenteCreateRequest docenteCreateRequest;

    @Mock
    private MateriaRepository materiaRepository;

    @InjectMocks
    private DocenteService docenteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMateriaDocente_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");

        NovoDocenteResponse response = docenteService.addMateriaDocente(1L, 1L, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("Usuários com papel [ALUNO] não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testAddMateriaDocente_Success() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        DocenteEntity docente = new DocenteEntity();
        MateriaEntity materia = new MateriaEntity();
        if (docente.getMaterias() == null) {
            docente.setMaterias(new HashSet<>());
        }
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(materiaRepository.findById(1L)).thenReturn(Optional.of(materia));
        when(docenteRepository.save(any(DocenteEntity.class))).thenReturn(docente);

        NovoDocenteResponse response = docenteService.addMateriaDocente(1L, 1L, token);

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals("Docente atualizado com sucesso.", response.message());
    }

    @Test
    void testAddMateriaDocente_MateriaNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        DocenteEntity docente = new DocenteEntity();
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(materiaRepository.findById(1L)).thenReturn(Optional.empty());

        NovoDocenteResponse response = docenteService.addMateriaDocente(1L, 1L, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Erro ao cadastrar docente: Nenhum docente com id [1] encontrado", response.message());
    }

    @Test
    void testDeleteMateriaDocente_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");

        NovoDocenteResponse response = docenteService.deleteMateriaDocente(1L, 1L, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("Usuários com papel [ALUNO] não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testNovoDocente_Success() throws Exception {
        String token = "validToken";
        DocenteCreateRequest request = docenteCreateRequest;
        UsuarioEntity usuario = new UsuarioEntity();
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(usuarioRepository.findById(request.id_usuario())).thenReturn(Optional.of(usuario));
        DocenteEntity docente = new DocenteEntity(request.nome(), request.data_entrada(), usuario);
        when(docenteRepository.save(any(DocenteEntity.class))).thenReturn(docente);

        NovoDocenteResponse response = docenteService.novoDocente(request, token);

        assertEquals(HttpStatus.CREATED, response.httpStatus());
        assertEquals("Docente cadastrado com sucesso.", response.message());
    }

    @Test
    void testGetDocenteById_NotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(docenteRepository.findById(1L)).thenReturn(Optional.empty());

        NovoDocenteResponse response = docenteService.getDocenteById(1L, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Docente ID 1 não encontrado.", response.message());
    }

    @Test
    void testDeleteDocente_Success() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        DocenteEntity docente = new DocenteEntity();
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));

        NovoDocenteResponse response = docenteService.deleteDocente(1L, token);

        assertEquals(HttpStatus.NO_CONTENT, response.httpStatus());
        assertEquals("Docente id [1] excluido", response.message());
    }
}
