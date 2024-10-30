package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.request.LoginRequest;
import br.com.fullstackedu.labpcp.controller.dto.response.LoginResponse;
import br.com.fullstackedu.labpcp.database.entity.UsuarioEntity;
import br.com.fullstackedu.labpcp.database.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private static final long EXPIRATION_TIME = 72000L;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String getFieldInToken(String token, String field) {
        String result = jwtDecoder
                .decode(token)
                .getClaims()
                .get(field)
                .toString();
        return result;
    }

    public LoginResponse doLogin(LoginRequest loginRequest) throws Exception{
        try {
            UsuarioEntity usuarioEntity = usuarioRepository
                    .findByLogin(loginRequest.login())
                    .orElseThrow(
                            () -> {
                                String errMessageUsrNotFound = "Usuário ["+ loginRequest.login() +"] não encontrado";
                                log.error(errMessageUsrNotFound);
                                return new RuntimeException(errMessageUsrNotFound);
                            }
                    );

            if (!usuarioEntity.isValidPassword(loginRequest, bCryptEncoder)) {
                String errMessage = "Credenciais inválidas. O Usuário [" + loginRequest.login() +"] não está autorizado a acessar o sistema.";
                log.error(errMessage);
                return new LoginResponse(
                        false,
                        LocalDateTime.now(),
                        errMessage,
                        null,
                        EXPIRATION_TIME);
            }

            log.info("Sucesso na autenticação do usuário [{}]", loginRequest.login());
            Instant now = Instant.now();
            String scope = usuarioEntity.getPapel().getNome();
            Long id_usuario = usuarioEntity.getId();
            String nome = usuarioEntity.getNome();

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("labpcp_system")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(EXPIRATION_TIME))
                    .subject(usuarioEntity.getId().toString())  // token owner
                    .claim("scope", scope)
                    .claim("id_usuario", id_usuario)
                    .claim("name", nome)
                    .build();
            log.info("claims: [{}]", claims);

            var tokenJWT = jwtEncoder.encode(
                    JwtEncoderParameters.from(claims) // encoding params
            ).getTokenValue(); // get the actual token from the object

            return new LoginResponse(
                    true,
                    LocalDateTime.now(),
                    "Sucesso na autenticação do usuário [" + loginRequest.login() + "]",
                    tokenJWT,
                    EXPIRATION_TIME);


        } catch(Exception e ) {
            String errMessage = "Falha ao tentar autenticação do usuário [" + loginRequest.login() + "]. Erro: " + e.getMessage();
            log.error(errMessage);
            return new LoginResponse(false, LocalDateTime.now(), errMessage, null, EXPIRATION_TIME);
        }
    }

}
