package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.UsuarioCreateRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.NovoUsuarioResponse;
import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.database.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptEncoder;
    private final LoginService loginService;
    private final PapelService papelService;
    

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder bCryptEncoder, LoginService loginService, PapelService papelService) {
        this.usuarioRepository = usuarioRepository;
        this.bCryptEncoder = bCryptEncoder;
        this.loginService = loginService;
        this.papelService = papelService;
    }

    public NovoUsuarioResponse novoUsuario(UsuarioCreateRequest nuRequest, String authToken) throws Exception{
        try {
            String papelName =  loginService.getFieldInToken(authToken, "scope");
            if (!Objects.equals(papelName, "ADM")){
                String errMessage = "Usuários com papel [" + papelName + "] não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new NovoUsuarioResponse(false, LocalDateTime.now() , errMessage , null, HttpStatus.UNAUTHORIZED);
            }
            UsuarioEntity novoUsuarioEntity = new UsuarioEntity();
            novoUsuarioEntity.setNome(nuRequest.nome());
            novoUsuarioEntity.setLogin(nuRequest.login());
            novoUsuarioEntity.setSenha(nuRequest.senha());
            novoUsuarioEntity.setPapel(
                    papelService.getPapelById(nuRequest.idPapel())
            );
            novoUsuarioEntity.setSenha(bCryptEncoder.encode(novoUsuarioEntity.getSenha()));
            UsuarioEntity savedUser = usuarioRepository.save(novoUsuarioEntity);
            log.info("Usuario adicionado com sucesso: {}", savedUser);
            return new NovoUsuarioResponse(true, LocalDateTime.now(),"Usuário cadastrado com sucesso.", novoUsuarioEntity, HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Falha ao adicionar usuario. Erro: {}", e.getMessage());
            return new NovoUsuarioResponse(false, LocalDateTime.now() , e.getMessage() , null, HttpStatus.BAD_REQUEST );
        }
    }
}
