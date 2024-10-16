package br.com.fullstackedu.labpcp.database.entity;

import br.com.fullstackedu.labpcp.controller.dto.request.LoginRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@Entity
@Table(name = "usuario")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @ManyToOne
    @JoinColumn(name = "id_papel")
    @NotNull(message = "É necessário um Papel válido para o Usuário")
    private PapelEntity papel;

    public boolean isValidPassword(LoginRequest loginRequest, BCryptPasswordEncoder bCryptEncoder) {
        return bCryptEncoder.matches(
                loginRequest.senha(),
                this.senha
        );
    }

}
