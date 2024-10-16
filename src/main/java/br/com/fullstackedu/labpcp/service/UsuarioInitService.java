package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.database.repository.PapelRepository;
import br.com.fullstackedu.labpcp.database.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsuarioInitService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final BCryptPasswordEncoder bCryptEncoder;

    public UsuarioInitService(UsuarioRepository usuarioRepository, PapelRepository papelRepository, BCryptPasswordEncoder bCryptEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.bCryptEncoder = bCryptEncoder;
    }

    private void insertIfNotExists(Long id, String login, String senha) throws Exception{
        if (usuarioRepository.findByLogin(login).isEmpty()) {
            log.info("UsuarioInitService -> Inserindo o usuário [{}] ",login);
            UsuarioEntity newUser = new UsuarioEntity();
            newUser.setId(id);
            newUser.setLogin(login);
            newUser.setSenha(bCryptEncoder.encode(senha));
            newUser.setNome(login);
            newUser.setPapel(papelRepository.findByNome(login)
                    .orElseThrow(
                            () -> new RuntimeException("Erro ao inserir usuario inicial. O Papel ["+ login + "] não foi encontrado" )
                    )
            );
            usuarioRepository.save(newUser);
        }
    }

    @PostConstruct
    public void initUsuarios() throws Exception {
        log.info("UsuarioInitService -> Verificando necessidade de inserir usuários iniciais ");
        insertIfNotExists(1L, "ADM", "ADM" );
        //TODO encapsule the users bellow to env "test"
        insertIfNotExists(2L, "PEDAGOGICO", "PEDAGOGICO" );
        insertIfNotExists(3L, "RECRUITER", "RECRUITER" );
        insertIfNotExists(4L, "PROFESSOR", "PROFESSOR" );
        insertIfNotExists(5L, "ALUNO", "ALUNO" );
    }
}
