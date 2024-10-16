package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.request.NovoUsuarioRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.NovoUsuarioResponse;
import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.service.PapelService;
import br.com.fullstackedu.labpcp.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequestMapping("/cadastro")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PapelService papelService;

    public UsuarioController(UsuarioService usuarioService, PapelService papelService) {
        this.usuarioService = usuarioService;
        this.papelService = papelService;
    }

    @PostMapping()
    public ResponseEntity<NovoUsuarioResponse> newUsuario(
            @RequestHeader(name = "Authorization") String authToken,
            @Valid @RequestBody NovoUsuarioRequest nuRequest
    ) throws Exception {
        log.info("POST /cadastro -> Novo Usuario ");

        UsuarioEntity newUser = new UsuarioEntity();
        newUser.setNome(nuRequest.nome());
        newUser.setLogin(nuRequest.login());
        newUser.setSenha(nuRequest.senha());
        newUser.setPapel(
                papelService.getPapelById(nuRequest.idPapel())
        );

        NovoUsuarioResponse response = usuarioService.novoUsuario(newUser, authToken);
        if (response.success()){
            log.info("POST /cadastro -> Usuário cadastrado com sucesso.");
        } else {
            log.info("POST /cadastro -> Erro ao cadastrar novo usuário: [{}].", response.message());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}
