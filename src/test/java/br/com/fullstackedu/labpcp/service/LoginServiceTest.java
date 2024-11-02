package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.LoginRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.LoginResponse;
import br.com.fullstackedu.labpcp.database.entity.AlunoEntity;
import br.com.fullstackedu.labpcp.database.entity.DocenteEntity;
import br.com.fullstackedu.labpcp.database.entity.PapelEntity;
import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.database.repository.AlunoRepository;
import br.com.fullstackedu.labpcp.database.repository.DocenteRepository;
import br.com.fullstackedu.labpcp.database.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DocenteRepository docenteRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private LoginService loginService;

    @Mock
    private BCryptPasswordEncoder bCryptEncoder;

    @Mock
    private Jwt jwt;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private JwtClaimsSet claims;

    @Mock
    private JwtEncoderParameters jwtEncoderParameters;

    @Mock
    private LoginRequest loginRequest;

    @Test
    public void loginSuccess() throws Exception {
        String password = "senhaCorreta";

        UsuarioEntity usuario = new UsuarioEntity();
        PapelEntity papel = new PapelEntity();
        DocenteEntity docente = new DocenteEntity();

        usuario.setId(1L);
        usuario.setNome("ADM");
        usuario.setPapel(papel);

        papel.setNome("ADM");

        docente.setId(1L);

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        when(jwt.getTokenValue()).thenReturn("lorem");

        when(loginRequest.login()).thenReturn("ADM");

        when(usuarioRepository.findByLogin(loginRequest.login())).thenReturn(Optional.of(usuario));

        when(docenteRepository.findByUsuarioId(any(Long.class))).thenReturn(Optional.of(docente));

        when(usuario.isValidPassword(loginRequest, bCryptEncoder)).thenReturn(true);

        LoginResponse response = loginService.doLogin(loginRequest);

        assertEquals("Sucesso na autenticação do usuário [ADM]", response.message());
    }

    @Test
    public void loginFailure() throws Exception {
        String password = "senhaCorreta";
        UsuarioEntity usuario = new UsuarioEntity();
        PapelEntity papel = new PapelEntity();
        usuario.setId(1L);
        usuario.setNome("ADM");
        usuario.setPapel(papel);
        papel.setNome("ADM");

        when(usuarioRepository.findByLogin(loginRequest.login())).thenReturn(Optional.of(usuario));

        when(usuario.isValidPassword(loginRequest, bCryptEncoder)).thenReturn(false);

        LoginResponse response = loginService.doLogin(loginRequest);

        assertEquals(false, response.success());
    }
}
