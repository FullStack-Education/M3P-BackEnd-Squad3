package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.request.LoginRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.LoginResponse;
import br.com.fullstackedu.labpcp.service.LoginService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        log.info("POST /login -> {}",loginRequest.login());
        LoginResponse loginResponse = loginService.doLogin(loginRequest);
        if (loginResponse.success()) {
            log.info("POST /login -> Sucesso na autenticação de {}",loginRequest.login());
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        } else {
            log.warn("POST /login -> Falha na autenticação de {}",loginRequest.login());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }
    }
}
