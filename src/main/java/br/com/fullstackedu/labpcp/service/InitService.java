package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.database.entity.CursoEntity;
import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.database.repository.CursoRepository;
import br.com.fullstackedu.labpcp.database.repository.PapelRepository;
import br.com.fullstackedu.labpcp.database.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InitService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final BCryptPasswordEncoder bCryptEncoder;
    private final CursoRepository cursoRepository;

    public InitService(UsuarioRepository usuarioRepository, PapelRepository papelRepository, BCryptPasswordEncoder bCryptEncoder, CursoRepository cursoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.bCryptEncoder = bCryptEncoder;
        this.cursoRepository = cursoRepository;
    }

    private void insertIfNotExistsUsuarioEntity(Long id, String login, String senha, String papel) throws Exception{
        if (usuarioRepository.findByLogin(login).isEmpty()) {
            log.info("InitService -> Inserindo o usuário [{}] ",login);
            UsuarioEntity newUser = new UsuarioEntity();
            newUser.setId(id);
            newUser.setLogin(login);
            newUser.setSenha(bCryptEncoder.encode(senha));
            newUser.setNome(login);
            newUser.setPapel(papelRepository.findByNome(papel)
                    .orElseThrow(
                            () -> new RuntimeException("Erro ao inserir usuario inicial. O Papel ["+ login + "] não foi encontrado" )
                    )
            );
            usuarioRepository.save(newUser);
        }
    }

    private void insertIfNotExistsCursoEntity(Long id, String nome) throws Exception{
        if (cursoRepository.findByNome(nome).isEmpty()) {
            log.info("InitService -> Inserindo o curso [{}] ",nome);
            var newCurso = new CursoEntity();
            newCurso.setId(id);
            newCurso.setNome(nome);
            cursoRepository.save(newCurso);
        }
    }


    @PostConstruct
    public void initUsuarios() throws Exception {
        log.info("InitService -> Verificando necessidade de inserir usuários iniciais ");
        Long id = 1L;
        insertIfNotExistsUsuarioEntity(id++, "ADM", "ADM","ADM");
        insertIfNotExistsUsuarioEntity(id++, "admin@mail.com", "admin","ADM");
        //TODO encapsule the users bellow to env "test"
        insertIfNotExistsUsuarioEntity(id++, "pedagogico@mail.com", "pedagogico","PEDAGOGICO" );
        insertIfNotExistsUsuarioEntity(id++, "recruiter@mail.com", "recruiter","RECRUITER" );
        insertIfNotExistsUsuarioEntity(id++, "professor@mail.com", "professor","PROFESSOR" );
        insertIfNotExistsUsuarioEntity(id++, "aluno@mail.com", "aluno","ALUNO" );

        //professores
        log.info("InitService -> Cria profesores");

        Long[] idProfessores = new Long[]{id++,id++,id++,id++,id++,id++};
        insertIfNotExistsUsuarioEntity(idProfessores[0], "eduardo@mail.com", "professor","PROFESSOR" );
        insertIfNotExistsUsuarioEntity(idProfessores[1], "gabriel@mail.com", "professor","PROFESSOR" );
        insertIfNotExistsUsuarioEntity(idProfessores[2], "ray@mail.com", "professor","PROFESSOR" );
        insertIfNotExistsUsuarioEntity(idProfessores[3], "joaoPaulo@mail.com", "professor","PROFESSOR" );
        insertIfNotExistsUsuarioEntity(idProfessores[4], "duda@mail.com", "professor","PROFESSOR" );

        //alunos
        log.info("InitService -> Cria Alunos");
        Long[] idAlunos = new Long[]{id++,id++,id++,id++,id++,id++};
        insertIfNotExistsUsuarioEntity(idAlunos[0], "joao@mail.com", "aluno","ALUNO" );
        insertIfNotExistsUsuarioEntity(idAlunos[1], "maria@mail.com", "aluno","ALUNO" );
        insertIfNotExistsUsuarioEntity(idAlunos[2], "jose@mail.com", "aluno","ALUNO" );
        insertIfNotExistsUsuarioEntity(idAlunos[3], "fabio@mail.com", "aluno","ALUNO" );
        insertIfNotExistsUsuarioEntity(idAlunos[4], "oto@mail.com", "aluno","ALUNO" );
        insertIfNotExistsUsuarioEntity(idAlunos[5], "alice@mail.com", "aluno","ALUNO" );

        Long[] idCurso = new Long[]{id++,id++,id++,id++,id++,id++};
        insertIfNotExistsCursoEntity(idCurso[0],"FrontEnd");
        insertIfNotExistsCursoEntity(idCurso[1],"BackEnd");
        insertIfNotExistsCursoEntity(idCurso[2],"FullStack");
        insertIfNotExistsCursoEntity(idCurso[3],"Dados");
        insertIfNotExistsCursoEntity(idCurso[4],"Inteligencia Ariticial");
        insertIfNotExistsCursoEntity(idCurso[5],"Experiencia do Usuario");
    }
}
