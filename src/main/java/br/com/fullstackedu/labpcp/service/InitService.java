package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.database.entity.*;
import br.com.fullstackedu.labpcp.database.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.Random;

@Service
@Slf4j
public class InitService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final BCryptPasswordEncoder bCryptEncoder;
    private final CursoRepository cursoRepository;
    private final MateriaRepository materiaRepository;
    private final DocenteRepository docenteRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final NotaRepository notaRepository;

    public InitService(UsuarioRepository usuarioRepository, PapelRepository papelRepository, BCryptPasswordEncoder bCryptEncoder, CursoRepository cursoRepository, MateriaRepository materiaRepository, DocenteRepository docenteRepository, AlunoRepository alunoRepository, TurmaRepository turmaRepository, NotaRepository notaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.bCryptEncoder = bCryptEncoder;
        this.cursoRepository = cursoRepository;
        this.materiaRepository = materiaRepository;
        this.docenteRepository = docenteRepository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.notaRepository = notaRepository;
    }


    private void insertIfNotExistsPapelEntity(Long id, String nome) {
        if (papelRepository.findByNome(nome).isEmpty()) {
            PapelEntity newPapel = new PapelEntity();
            newPapel.setId(id);
            newPapel.setNome(nome);
            papelRepository.save(newPapel);
        }
    }
    private void insertIfNotExistsUsuarioEntity(Long id, String login, String senha, String papel) throws Exception {
        if (usuarioRepository.findByLogin(login).isEmpty()) {
            log.info("InitService -> Inserindo o docente [{}] ", login);
            UsuarioEntity newUser = new UsuarioEntity();
            newUser.setId(id);
            newUser.setLogin(login);
            newUser.setSenha(bCryptEncoder.encode(senha));
            newUser.setNome(login);
            newUser.setPapel(papelRepository.findByNome(papel)
                    .orElseThrow(
                            () -> new RuntimeException("Erro ao inserir usuario inicial. O Papel [" + login + "] não foi encontrado")
                    )
            );
            usuarioRepository.save(newUser);
        }
    }

    private void insertIfNotExistsCursoEntity(Long id, String nome) throws Exception {
        if (cursoRepository.findByNome(nome).isEmpty()) {
            log.info("InitService -> Inserindo o curso [{}] ", nome);
            var newCurso = new CursoEntity();
            newCurso.setId(id);
            newCurso.setNome(nome);
            cursoRepository.save(newCurso);
        }
    }

    private void insertIfNotExistsMateriaEntity(Long id, String nome, String nomeCurso) throws Exception {
        if (materiaRepository.findByNome(nome).isEmpty()) {
            log.info("InitService -> Inserindo a materia [{}] ", nome);
            var entity = new MateriaEntity();
            entity.setId(id);
            entity.setNome(nome);
            var curso = cursoRepository.findByNome(nomeCurso).orElseThrow(() -> new NotFoundException("Curso não encontrado"));
            entity.setCurso(curso);
            materiaRepository.save(entity);
        }
    }

    private void insertIfNotExistsProfessorEntity(Long id, String nome, String loginUsuario) throws Exception {
        if (docenteRepository.findByNome(nome).isEmpty()) {
            log.info("InitService -> Inserindo a professor [{}] ", nome);
            var entity = new DocenteEntity();
            entity.setId(id);
            entity.setNome(nome);
            entity.setDataEntrada(LocalDate.now());
            var papel = papelRepository.findByNome("PROFESSOR").orElseThrow(() -> new RuntimeException("Papel não encotrado"));
            var curso = usuarioRepository.findByLoginAndPapelId(loginUsuario, papel.getId()).orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
            entity.setUsuario(curso);
            docenteRepository.save(entity);
        }
    }

    private void insertIfNotExistsTurmaEntity(Long id, String nome, String nomeProfessor, String nomeCurso) throws Exception {
        if (turmaRepository.findByNome(nome).isEmpty()) {
            log.info("InitService -> Inserindo a turma [{}] ", nome);
            var entity = new TurmaEntity();
            entity.setId(id);
            entity.setNome(nome);
            entity.setDataFim(LocalDate.now());
            entity.setDataInicio(LocalDate.now());
            entity.setHora("10:10");
            var curso = cursoRepository.findByNome(nomeCurso).orElseThrow(() -> new NotFoundException("Curso não encontrado"));
            var docente = docenteRepository.findByNome(nomeProfessor).orElseThrow(() -> new NotFoundException("Professor não encontrado"));
            entity.setProfessor(docente);
            entity.setCurso(curso);
            turmaRepository.save(entity);
        }
    }


    private void insertIfNotExistsAlunoEntity(Long id, String nome, String loginUsuario, String nomeTurma) throws Exception {
        if (alunoRepository.findByNome(nome).isEmpty()) {
            log.info("InitService -> Inserindo a aluno [{}] ", nome);
            var entity = new AlunoEntity();
            entity.setId(id);
            entity.setNome(nome);
            entity.setDataNascimento(LocalDate.now());
            entity.setTelefone("4899999999");
            entity.setGenero("outro");
            entity.setEstadoCivil("solteiro");
            entity.setEmail("mail@mail.com");
            entity.setEmail("mail@mail.com");
            entity.setCpf("mail@mail.com");
            entity.setCpf("00000000000");
            entity.setRg("4444444");
            entity.setNaturalidade("brasil");
            entity.setCep("88000000");
            entity.setLogadouro("rua sem fim");
            entity.setNumero("7070");
            entity.setCidade("pinhacinho");
            entity.setComplemento("casa");
            var papel = papelRepository.findByNome("ALUNO").orElseThrow(() -> new RuntimeException("Papel não encotrado"));
            var curso = usuarioRepository.findByLoginAndPapelId(loginUsuario, papel.getId()).orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
            entity.setUsuario(curso);
            var turma = turmaRepository.findByNome(nomeTurma).orElseThrow(() -> new RuntimeException("Turma não encotrado"));
            entity.setTurma(turma);
            alunoRepository.save(entity);
        }
    }

    private void insertIfNotExistsTwentyNotaEntityByAlunoEntity(Long id, String nomeAluno, String nomeProfessor, String nomeMateria,String nomeTurma, double nota) throws Exception {
        var aluno = alunoRepository.findByNome(nomeAluno).orElseThrow(() -> new RuntimeException("Aluno não encotrado"));
        if (notaRepository.countByAlunoId(aluno.getId()) < 20) {
            log.info("InitService -> Inserindo a nota [{}] ", id);
            var entity = new NotaEntity();
            entity.setId(id);
            entity.setAluno(aluno);
            entity.setData(LocalDate.now());
            var professor = docenteRepository.findByNome(nomeProfessor).orElseThrow(() -> new NotFoundException("Professor não encontrado"));
            entity.setProfessor(professor);
            var materia = materiaRepository.findByNome(nomeMateria).orElseThrow(() -> new RuntimeException("Materia não encotrado"));
            entity.setMateria(materia);
            var turma = turmaRepository.findByNome(nomeTurma).orElseThrow(() -> new RuntimeException("Materia não encotrado"));
            entity.setTurma(turma);
            entity.setValor(nota);
            notaRepository.save(entity);
        }
    }

    @PostConstruct
    public void initUsuarios() throws Exception {

        log.info("InitService -> Verificando necessidade de inserir papeis iniciais ");

        insertIfNotExistsPapelEntity(1L, "ADM");
        insertIfNotExistsPapelEntity(2L, "PEDAGOGICO");
        insertIfNotExistsPapelEntity(3L, "RECRUITER");
        insertIfNotExistsPapelEntity(4L, "PROFESSOR");
        insertIfNotExistsPapelEntity(5L, "ALUNO");


        log.info("InitService -> Verificando necessidade de inserir usuários iniciais ");
        Long id = 1L;
        insertIfNotExistsUsuarioEntity(id++, "ADM", "ADM", "ADM");
        insertIfNotExistsUsuarioEntity(id++, "admin@mail.com", "admin@mail.com", "ADM");
        //TODO encapsule the users bellow to env "test"
        insertIfNotExistsUsuarioEntity(id++, "pedagogico@mail.com", "pedagogico@mail.com", "PEDAGOGICO");
        insertIfNotExistsUsuarioEntity(id++, "recruiter@mail.com", "recruiter@mail.com", "RECRUITER");
        insertIfNotExistsUsuarioEntity(id++, "professor@mail.com", "professor@mail.com", "PROFESSOR");
        insertIfNotExistsUsuarioEntity(id++, "aluno@mail.com", "aluno@mail.com", "ALUNO");

        //professores
        log.info("InitService -> Cria profesores");
        int index = 0;
        String[] emailProfessores = new String[]{"eduardo@mail.com",
                "gabriel@mail.com",
                "ray@mail.com",
                "joaoPaulo@mail.com",
                "duda@mail.com",
                "hugo@mail.com"};
        for (String emailProfessore : emailProfessores) {
            insertIfNotExistsUsuarioEntity(id++, emailProfessore, "professor", "PROFESSOR");
        }


        //alunos
        log.info("InitService -> Cria Alunos");
        String[] emailAlunos = new String[]{"joao@mail.com",
                "maria@mail.com",
                "jose@mail.com",
                "fabio@mail.com",
                "oto@mail.com",
                "alice@mail.com"};
        index = 0;
        for (String emailAluno : emailAlunos) {
            insertIfNotExistsUsuarioEntity(id++, emailAluno, "aluno", "ALUNO");
        }


        String[] nomeCursos = new String[]{"FrontEnd",
                "BackEnd",
                "FullStack",
                "Dados",
                "Inteligencia Ariticial",
                "Experiencia do Usuario"};
        for (String nomeCurso : nomeCursos) {
            insertIfNotExistsCursoEntity(id++, nomeCurso);
        }

        String[] nomeMaterias = new String[]{"Introdução ao Design Responsivo",
                "JavaScript Avançado",
                "Desenvolvimento com React/Vue/Angular",
                "Criação de APIs RESTful",
                "Desenvolvimento com Java",
                "Testes e Debugging de Back-End",
                "Integração de Front-End e Back-End",
                "Escalabilidade e Otimização",
                "Testes em Ambiente Full-Stack",
                "Manipulação e Limpeza de Dados",
                "Análise de Dados Exploratória",
                "Estatística para Ciência de Dados",
                "Redes Neurais e Deep Learning",
                "Processamento de Linguagem Natural (NLP)",
                "Aprendizado por Reforço",
                "Pesquisa de Usuário",
                "Teste de Usabilidade",
                "Acessibilidade Digital"};
        index = 0;
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[0]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[0]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[0]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[1]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[1]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[1]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[2]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[2]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[2]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[3]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[3]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[3]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[4]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[4]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[4]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[5]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[5]);
        insertIfNotExistsMateriaEntity(id++, nomeMaterias[index++], nomeCursos[5]);


        for (String emailProfessore : emailProfessores) {
            insertIfNotExistsProfessorEntity(id++, emailProfessore.split("@")[0], emailProfessore);
        }
        String[] nomeTurmas = new String[]{"Turma FrontEnd",
                "Turma BackEnd",
                "Turma FullStack",
                "Turma Dados",
                "Turma Inteligencia Ariticial",
                "Turma Experiencia do Usuario"};

        for (int i = 0; i < nomeTurmas.length; i++) {
            var indexProfessor = i % emailProfessores.length;
            var indexCurso = i % nomeCursos.length;
            insertIfNotExistsTurmaEntity(id++, nomeTurmas[i],
                    emailProfessores[indexProfessor].split("@")[0]
                    , nomeCursos[indexCurso]);
        }

        for (String emailAluno : emailAlunos) {
            Random random = new Random();
            int indexTurma = random.nextInt(nomeTurmas.length);
            insertIfNotExistsAlunoEntity(id++, emailAluno.split("@")[0], emailAluno, nomeTurmas[indexTurma]);
        }

        for (String emailAluno : emailAlunos)
            for (int i = 0; i < 10; i++) {
                Random random = new Random();
                int indexMateria = random.nextInt(nomeMaterias.length);
                int indexProfessor = random.nextInt(emailProfessores.length);
                int indexTurma = random.nextInt(nomeTurmas.length);
                insertIfNotExistsTwentyNotaEntityByAlunoEntity(id++, emailAluno.split("@")[0],
                        emailProfessores[indexProfessor].split("@")[0],
                        nomeMaterias[indexMateria],
                        nomeTurmas[indexTurma],
                        random.nextInt(10));
            }


    }
}
