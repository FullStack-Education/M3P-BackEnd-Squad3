package br.com.fullstackedu.labpcp.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import br.com.fullstackedu.labpcp.controller.dto.request.DocenteCreateRequest;
import br.com.fullstackedu.labpcp.controller.dto.request.DocenteUpdateRequest;
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

import java.time.LocalDate;
import java.util.*;

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
    void testAddMateriaDocente_DocenteNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(docenteRepository.findById(1L)).thenReturn(Optional.empty());

        NovoDocenteResponse response = docenteService.addMateriaDocente(1L, 1L, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Erro ao cadastrar docente: Nenhum docente com id [1] encontrado", response.message());
    }

    @Test
    void testAddMateriaDocente_DocenteJaMinistraMateria() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        DocenteEntity docente = new DocenteEntity();
        MateriaEntity materia = new MateriaEntity();
        docente.setMaterias(new HashSet<>(Collections.singletonList(materia)));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(materiaRepository.findById(1L)).thenReturn(Optional.of(materia));

        NovoDocenteResponse response = docenteService.addMateriaDocente(1L, 1L, token);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.httpStatus());
        assertEquals("Docente ja ministra essa materia", response.message());
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
    void testDeleteMateriaDocente_Success() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        DocenteEntity docente = new DocenteEntity();
        MateriaEntity materia = new MateriaEntity();
        docente.setMaterias(new HashSet<>(Collections.singletonList(materia)));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(materiaRepository.findById(1L)).thenReturn(Optional.of(materia));
        when(docenteRepository.save(any(DocenteEntity.class))).thenReturn(docente);

        NovoDocenteResponse response = docenteService.deleteMateriaDocente(1L, 1L, token);

        assertEquals(HttpStatus.NO_CONTENT, response.httpStatus());
        assertEquals("Docente atualizado com sucesso", response.message());
    }

    @Test
    void testDeleteMateriaDocente_DocenteNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(docenteRepository.findById(1L)).thenReturn(Optional.empty());

        NovoDocenteResponse response = docenteService.deleteMateriaDocente(1L, 1L, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Erro ao atualizar docente: Nenhum docente com id [1] encontrado", response.message());
    }

    @Test
    void testDeleteMateriaDocente_MateriaNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        DocenteEntity docente = new DocenteEntity();
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(materiaRepository.findById(1L)).thenReturn(Optional.empty());

        NovoDocenteResponse response = docenteService.deleteMateriaDocente(1L, 1L, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Erro ao atualizar docente: Nenhuma materia com id [1] encontrada", response.message());
    }

    @Test
    void testDeleteMateriaDocente_FailureByException() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(docenteRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            NovoDocenteResponse response = docenteService.deleteMateriaDocente(1L, 1L, token);
        });
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testGetAllDocentes_Success() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        DocenteEntity docente1 = new DocenteEntity();
        DocenteEntity docente2 = new DocenteEntity();
        when(docenteRepository.findAll()).thenReturn(Arrays.asList(docente1, docente2));

        NovoDocenteResponse response = docenteService.getAllDocentes(token);

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals("Docentes encontrados: 2", response.message());
        assertNotNull(response.docenteData());
        assertEquals(2, response.docenteData().size());
    }

    @Test
    void testGetAllDocentes_EmptyResults() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(docenteRepository.findAll()).thenReturn(Collections.emptyList());

        NovoDocenteResponse response = docenteService.getAllDocentes(token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Não há docentes cadastrados.", response.message());
    }

    @Test
    void testGetAllDocentes_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");

        NovoDocenteResponse response = docenteService.getAllDocentes(token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("Usuários com papel [ALUNO] não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testGetAllDocentes_FailureByException() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(docenteRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        NovoDocenteResponse response = docenteService.getAllDocentes(token);

        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals("Database error", response.message());
    }

    @Test
    void testNovoDocente_Success() throws Exception {
        String token = "validToken";
        DocenteCreateRequest request = docenteCreateRequest;
        UsuarioEntity usuario = new UsuarioEntity();
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(usuarioRepository.findById(request.id_usuario())).thenReturn(Optional.of(usuario));
        DocenteEntity docente = new DocenteEntity(request.nome(),  usuario);
        when(docenteRepository.save(any(DocenteEntity.class))).thenReturn(docente);

        NovoDocenteResponse response = docenteService.novoDocente(request, token);

        assertEquals(HttpStatus.CREATED, response.httpStatus());
        assertEquals("Docente cadastrado com sucesso.", response.message());
    }

    @Test
    void testNovoDocente_UnauthorizedAccess() throws Exception {
        String token = "invalidToken";
        DocenteCreateRequest request = docenteCreateRequest;
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");

        NovoDocenteResponse response = docenteService.novoDocente(request, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("Usuários com papel [ALUNO] não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testNovoDocente_UsuarioNotFound() throws Exception {
        String token = "validToken";
        DocenteCreateRequest request = docenteCreateRequest;
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(usuarioRepository.findById(request.id_usuario())).thenReturn(Optional.empty());

        NovoDocenteResponse response = docenteService.novoDocente(request, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Erro ao cadastrar docente: Nenhum usuário com id [" + request.id_usuario() + "] encontrado", response.message());
    }

    @Test
    void testNovoDocente_FailureByException() throws Exception {
        String token = "validToken";
        DocenteCreateRequest request = docenteCreateRequest;
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(usuarioRepository.findById(request.id_usuario())).thenThrow(new RuntimeException("Database error"));

        NovoDocenteResponse response = docenteService.novoDocente(request, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals("Database error", response.message());
    }

    @Test
    void testUpdateDocente_Success() {
        String token = "validToken";
        String someString = "some string";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        List<Long> idMaterias = new ArrayList<>();
        DocenteUpdateRequest request = new DocenteUpdateRequest("Updated Name", LocalDate.now(), "51999999999", someString, someString, LocalDate.now(), someString, someString, someString, someString, someString, someString, someString, someString, someString, someString, 1L, idMaterias);
        DocenteEntity docente = new DocenteEntity();
        UsuarioEntity usuario = new UsuarioEntity();
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(docenteRepository.save(any(DocenteEntity.class))).thenReturn(docente);

        NovoDocenteResponse response = docenteService.updateDocente(1L, request, token);

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals("Docente atualizado", response.message());
    }

    @Test
    void testUpdateDocente_DocenteNotFound() {
        String token = "validToken";
        String someString = "some string";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        List<Long> idMaterias = new ArrayList<>();
        DocenteUpdateRequest request = new DocenteUpdateRequest("Updated Name", LocalDate.now(), "51999999999", someString, someString, LocalDate.now(), someString, someString, someString, someString, someString, someString, someString, someString, someString, someString, 1L, idMaterias );
        when(docenteRepository.findById(1L)).thenReturn(Optional.empty());

        NovoDocenteResponse response = docenteService.updateDocente(1L, request, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Docente id [1] não encontrado", response.message());
    }

    @Test
    void testUpdateDocente_UnauthorizedAccess() {
        String token = "invalidToken";
        String someString = "some string";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");
        List<Long> idMaterias = new ArrayList<>();
        DocenteUpdateRequest request = new DocenteUpdateRequest("Updated Name", LocalDate.now(), "51999999999", someString, someString, LocalDate.now(), someString, someString, someString, someString, someString, someString, someString, someString, someString, someString, 1L, idMaterias);

        NovoDocenteResponse response = docenteService.updateDocente(1L, request, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("Usuários com papel [ALUNO] não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testUpdateDocente_FalhaAoAssociarUsuario() {
        String token = "validToken";
        String someString = "some string";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        List<Long> idMaterias = new ArrayList<>();
        DocenteUpdateRequest request = new DocenteUpdateRequest("Updated Name", LocalDate.now(), "51999999999", someString, someString, LocalDate.now(), someString, someString, someString, someString, someString, someString, someString, someString, someString, someString, 1L, idMaterias);
        DocenteEntity docente = new DocenteEntity();
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        NovoDocenteResponse response = docenteService.updateDocente(1L, request, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Falha ao associar Usuário ID 1 ao Docente ID [1]: Usuário não existe", response.message());
    }

    @Test
    void testUpdateDocente_FailureByException() {
        String token = "validToken";
        String someString = "some string";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        List<Long> idMaterias = new ArrayList<>();
        DocenteUpdateRequest request = new DocenteUpdateRequest("Updated Name", LocalDate.now(), "51999999999", someString, someString, LocalDate.now(), someString, someString, someString, someString, someString, someString, someString, someString, someString, someString, 1L, idMaterias);
        when(docenteRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        NovoDocenteResponse response = docenteService.updateDocente(1L, request, token);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals("Database error", response.message());
    }

    @Test
    void testGetDocenteById_Success() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        DocenteEntity docente = new DocenteEntity();
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));

        NovoDocenteResponse response = docenteService.getDocenteById(1L, token);

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals("Docente encontrado", response.message());
        assertNotNull(response.docenteData());
        assertEquals(1, response.docenteData().size());
        assertEquals(docente, response.docenteData().get(0));
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
    void testGetDocenteById_FailureByException() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(docenteRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        NovoDocenteResponse response = docenteService.getDocenteById(1L, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals("Database error", response.message());
    }

    @Test
    void testGetDocenteById_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");

        NovoDocenteResponse response = docenteService.getDocenteById(1L, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("Usuários com papel [ALUNO] não tem acesso a essa funcionalidade", response.message());
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

    @Test
    void testDeleteDocente_FailureByException() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(docenteRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        
        NovoDocenteResponse respose =  docenteService.deleteDocente(1L, token);
        assertEquals("Database error", respose.message());
    }

    @Test
    void testDeleteDocente_UnauthorizedAccess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");

        NovoDocenteResponse response = docenteService.deleteDocente(1L, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("Usuários com papel [ALUNO] não tem acesso a essa funcionalidade", response.message());
    }

    @Test
    void testDeleteDocente_DocenteNotFound() {
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(docenteRepository.findById(1L)).thenReturn(Optional.empty());

        NovoDocenteResponse response = docenteService.deleteDocente(1L, token);

        assertEquals(HttpStatus.NOT_FOUND, response.httpStatus());
        assertEquals("Docente id [1] não encontrado", response.message());
    }

    @Test
    void testCount_Success() {
        when(docenteRepository.count()).thenReturn(5L);

        long count = docenteService.count();

        assertEquals(5L, count);
    }

    @Test
    void testCount_Failure() {
        when(docenteRepository.count()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            docenteService.count();
        });

        assertEquals("Database error", exception.getMessage());
    }
}
