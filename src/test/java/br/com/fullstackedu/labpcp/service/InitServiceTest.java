package br.com.fullstackedu.labpcp.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.fullstackedu.labpcp.database.entity.AlunoEntity;
import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import br.com.fullstackedu.labpcp.database.entity.DocenteEntity;
import br.com.fullstackedu.labpcp.database.entity.MateriaEntity;
import br.com.fullstackedu.labpcp.database.entity.PapelEntity;
import br.com.fullstackedu.labpcp.database.entity.TurmaEntity;
import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.database.repository.AlunoRepository;
import br.com.fullstackedu.labpcp.database.repository.CursoRepository;
import br.com.fullstackedu.labpcp.database.repository.DocenteRepository;
import br.com.fullstackedu.labpcp.database.repository.MateriaRepository;
import br.com.fullstackedu.labpcp.database.repository.NotaRepository;
import br.com.fullstackedu.labpcp.database.repository.PapelRepository;
import br.com.fullstackedu.labpcp.database.repository.TurmaRepository;
import br.com.fullstackedu.labpcp.database.repository.UsuarioRepository;

public class InitServiceTest {

  @Mock
  private UsuarioRepository usuarioRepository;

  @Mock
  private PapelRepository papelRepository;

  @Mock
  private BCryptPasswordEncoder bCryptEncoder;

  @Mock
  private CursoRepository cursoRepository;

  @Mock
  private MateriaRepository materiaRepository;

  @Mock
  private DocenteRepository docenteRepository;

  @Mock
  private AlunoRepository alunoRepository;

  @Mock
  private TurmaRepository turmaRepository;

  @Mock
  private NotaRepository notaRepository;

  @InjectMocks
  private InitService initService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private void mockRepositoriesForInitUsuarios() {
    PapelEntity papelAdm = new PapelEntity();
    papelAdm.setId(1L);
    when(papelRepository.findByNome("ADM")).thenReturn(Optional.of(papelAdm));
    
    PapelEntity papelPedagogico = new PapelEntity();
    papelPedagogico.setId(2L);
    when(papelRepository.findByNome("PEDAGOGICO")).thenReturn(Optional.of(papelPedagogico));
    
    PapelEntity papelRecruiter = new PapelEntity();
    papelRecruiter.setId(3L);
    when(papelRepository.findByNome("RECRUITER")).thenReturn(Optional.of(papelRecruiter));
    
    PapelEntity papelProfessor = new PapelEntity();
    papelProfessor.setId(4L);
    when(papelRepository.findByNome("PROFESSOR")).thenReturn(Optional.of(papelProfessor));
    
    PapelEntity papelAluno = new PapelEntity();
    papelAluno.setId(5L);
    when(papelRepository.findByNome("ALUNO")).thenReturn(Optional.of(papelAluno));
    when(usuarioRepository.findByLogin("ADM")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("admin@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("pedagogico@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("recruiter@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("professor@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("aluno@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("eduardo@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("gabriel@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("ray@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("joaoPaulo@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("duda@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("hugo@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("joao@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("maria@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("jose@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("fabio@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("oto@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLogin("alice@mail.com")).thenReturn(Optional.of(new UsuarioEntity()));
    when(bCryptEncoder.encode("ADM")).thenReturn("encodedADM");
    when(bCryptEncoder.encode("admin@mail.com")).thenReturn("encodedAdmin");
    when(bCryptEncoder.encode("123456")).thenReturn("encoded123456");
    when(cursoRepository.findByNome("FrontEnd")).thenReturn(Optional.of(new CursoEntity()));
    when(cursoRepository.findByNome("BackEnd")).thenReturn(Optional.of(new CursoEntity()));
    when(cursoRepository.findByNome("FullStack")).thenReturn(Optional.of(new CursoEntity()));
    when(cursoRepository.findByNome("Dados")).thenReturn(Optional.of(new CursoEntity()));
    when(cursoRepository.findByNome("Inteligencia Ariticial")).thenReturn(Optional.of(new CursoEntity()));
    when(cursoRepository.findByNome("Experiencia do Usuario")).thenReturn(Optional.of(new CursoEntity()));
    when(docenteRepository.findByNome("eduardo")).thenReturn(Optional.of(new DocenteEntity()));
    when(docenteRepository.findByNome("gabriel")).thenReturn(Optional.of(new DocenteEntity()));
    when(docenteRepository.findByNome("ray")).thenReturn(Optional.of(new DocenteEntity()));
    when(docenteRepository.findByNome("joaoPaulo")).thenReturn(Optional.of(new DocenteEntity()));
    when(docenteRepository.findByNome("duda")).thenReturn(Optional.of(new DocenteEntity()));
    when(docenteRepository.findByNome("hugo")).thenReturn(Optional.of(new DocenteEntity()));
    when(usuarioRepository.findByLoginAndPapelId("eduardo@mail.com", 4L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("gabriel@mail.com", 4L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("ray@mail.com", 4L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("joaoPaulo@mail.com", 4L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("duda@mail.com", 4L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("hugo@mail.com", 4L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("joao@mail.com", 5L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("maria@mail.com", 5L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("jose@mail.com", 5L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("fabio@mail.com", 5L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("oto@mail.com", 5L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(usuarioRepository.findByLoginAndPapelId("alice@mail.com", 5L)).thenReturn(Optional.of(new UsuarioEntity()));
    when(turmaRepository.findByNome("Turma FrontEnd")).thenReturn(Optional.of(new TurmaEntity()));
    when(turmaRepository.findByNome("Turma BackEnd")).thenReturn(Optional.of(new TurmaEntity()));
    when(turmaRepository.findByNome("Turma FullStack")).thenReturn(Optional.of(new TurmaEntity()));
    when(turmaRepository.findByNome("Turma Dados")).thenReturn(Optional.of(new TurmaEntity()));
    when(turmaRepository.findByNome("Turma Inteligencia Ariticial")).thenReturn(Optional.of(new TurmaEntity()));
    when(turmaRepository.findByNome("Turma Experiencia do Usuario")).thenReturn(Optional.of(new TurmaEntity()));
    when(materiaRepository.findByNome("Introdução ao Design Responsivo")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("JavaScript Avançado")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Desenvolvimento com React/Vue/Angular")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Criação de APIs RESTful")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Desenvolvimento com Java")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Testes e Debugging de Back-End")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Integração de Front-End e Back-End")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Escalabilidade e Otimização")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Testes em Ambiente Full-Stack")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Manipulação e Limpeza de Dados")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Análise de Dados Exploratória")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Estatística para Ciência de Dados")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Redes Neurais e Deep Learning")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Processamento de Linguagem Natural (NLP)")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Aprendizado por Reforço")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Pesquisa de Usuário")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Teste de Usabilidade")).thenReturn(Optional.of(new MateriaEntity()));
    when(materiaRepository.findByNome("Acessibilidade Digital")).thenReturn(Optional.of(new MateriaEntity()));
    when(alunoRepository.findByNome("joao")).thenReturn(Optional.of(new AlunoEntity()));
    when(alunoRepository.findByNome("maria")).thenReturn(Optional.of(new AlunoEntity()));
    when(alunoRepository.findByNome("jose")).thenReturn(Optional.of(new AlunoEntity()));
    when(alunoRepository.findByNome("fabio")).thenReturn(Optional.of(new AlunoEntity()));
    when(alunoRepository.findByNome("oto")).thenReturn(Optional.of(new AlunoEntity()));
    when(alunoRepository.findByNome("alice")).thenReturn(Optional.of(new AlunoEntity()));


  }

  @Test
  public void testInitUsuariosSuccess() throws Exception {
      mockRepositoriesForInitUsuarios();
      initService.initUsuarios();
      assertNotNull(initService);
  }

  @Test
  public void testInitUsuariosFailure() throws Exception {
    when(papelRepository.findByNome("ADM")).thenReturn(Optional.empty());
    try {
      initService.initUsuarios();
    } catch (RuntimeException e) {
      assertNotNull(e);
    }
  }
   
  @Test
  public void testInitUsuariosInsertCursoSuccess() throws Exception {
    mockRepositoriesForInitUsuarios();
    
    when(cursoRepository.findByNome(anyString())).thenReturn(Optional.empty());
    
    CursoEntity newCurso = new CursoEntity();
    newCurso.setId(100L);
    newCurso.setNome("NewCourse");
    when(cursoRepository.save(newCurso)).thenReturn(newCurso);
    
    initService.initUsuarios();
    
    verify(cursoRepository, times(6)).save(any(CursoEntity.class));
  }
}