package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import br.com.fullstackedu.labpcp.database.entity.MateriaEntity;
import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.database.repository.CursoRepository;
import br.com.fullstackedu.labpcp.database.repository.MateriaRepository;
import br.com.fullstackedu.labpcp.database.repository.PapelRepository;
import br.com.fullstackedu.labpcp.database.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@Slf4j
public class InitService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final BCryptPasswordEncoder bCryptEncoder;
    private final CursoRepository cursoRepository;
    private final MateriaRepository materiaRepository;

    public InitService(UsuarioRepository usuarioRepository, PapelRepository papelRepository, BCryptPasswordEncoder bCryptEncoder, CursoRepository cursoRepository, MateriaRepository materiaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.bCryptEncoder = bCryptEncoder;
        this.cursoRepository = cursoRepository;
        this.materiaRepository = materiaRepository;
    }

    private void insertIfNotExistsUsuarioEntity(Long id, String login, String senha, String papel) throws Exception {
        if (usuarioRepository.findByLogin(login).isEmpty()) {
            log.info("InitService -> Inserindo o usuário [{}] ", login);
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


    @PostConstruct
    public void initUsuarios() throws Exception {
        log.info("InitService -> Verificando necessidade de inserir usuários iniciais ");
        Long id = 1L;
        insertIfNotExistsUsuarioEntity(id++, "ADM", "ADM", "ADM");
        insertIfNotExistsUsuarioEntity(id++, "admin@mail.com", "admin", "ADM");
        //TODO encapsule the users bellow to env "test"
        insertIfNotExistsUsuarioEntity(id++, "pedagogico@mail.com", "pedagogico", "PEDAGOGICO");
        insertIfNotExistsUsuarioEntity(id++, "recruiter@mail.com", "recruiter", "RECRUITER");
        insertIfNotExistsUsuarioEntity(id++, "professor@mail.com", "professor", "PROFESSOR");
        insertIfNotExistsUsuarioEntity(id++, "aluno@mail.com", "aluno", "ALUNO");

        //professores
        log.info("InitService -> Cria profesores");
        int index = 0;
        String[] nomesProfessores = new String[]{"eduardo@mail.com",
                "gabriel@mail.com",
                "ray@mail.com",
                "joaoPaulo@mail.com",
                "duda@mail.com"};
        insertIfNotExistsUsuarioEntity(id++, nomesProfessores[index++], "professor", "PROFESSOR");
        insertIfNotExistsUsuarioEntity(id++, nomesProfessores[index++], "professor", "PROFESSOR");
        insertIfNotExistsUsuarioEntity(id++, nomesProfessores[index++], "professor", "PROFESSOR");
        insertIfNotExistsUsuarioEntity(id++, nomesProfessores[index++], "professor", "PROFESSOR");
        insertIfNotExistsUsuarioEntity(id++, nomesProfessores[index++], "professor", "PROFESSOR");

        //alunos
        log.info("InitService -> Cria Alunos");
        String[] idAlunos = new String[]{"joao@mail.com",
                "maria@mail.com",
                "jose@mail.com",
                "fabio@mail.com",
                "oto@mail.com",
                "alice@mail.com"};
        index = 0;
        insertIfNotExistsUsuarioEntity(id++, idAlunos[index++], "aluno", "ALUNO");
        insertIfNotExistsUsuarioEntity(id++, idAlunos[index++], "aluno", "ALUNO");
        insertIfNotExistsUsuarioEntity(id++, idAlunos[index++], "aluno", "ALUNO");
        insertIfNotExistsUsuarioEntity(id++, idAlunos[index++], "aluno", "ALUNO");
        insertIfNotExistsUsuarioEntity(id++, idAlunos[index++], "aluno", "ALUNO");
        insertIfNotExistsUsuarioEntity(id++, idAlunos[index++], "aluno", "ALUNO");

        String[] idCurso = new String[]{"FrontEnd",
                "BackEnd",
                "FullStack",
                "Dados",
                "Inteligencia Ariticial",
                "Experiencia do Usuario"};
        index = 0;
        insertIfNotExistsCursoEntity(id++, idCurso[index++]);
        insertIfNotExistsCursoEntity(id++, idCurso[index++]);
        insertIfNotExistsCursoEntity(id++, idCurso[index++]);
        insertIfNotExistsCursoEntity(id++, idCurso[index++]);
        insertIfNotExistsCursoEntity(id++, idCurso[index++]);
        insertIfNotExistsCursoEntity(id++, idCurso[index++]);

        String[] idMateria = new String[]{"Introdução ao Design Responsivo",
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
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[0]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[0]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[0]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[1]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++],idCurso[1] );
        insertIfNotExistsMateriaEntity(id++,idMateria[index++],idCurso[1] );
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[2]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[2]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[2]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[3]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[3]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[3]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[4]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[4]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[4]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[5]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[5]);
        insertIfNotExistsMateriaEntity(id++,idMateria[index++], idCurso[5]);
    }
}
