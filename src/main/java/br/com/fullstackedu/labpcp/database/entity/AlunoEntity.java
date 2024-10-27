package br.com.fullstackedu.labpcp.database.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "aluno")
public class AlunoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_nascimento")
    @NotNull(message = "É necessário um Data de nascimento válida para cadastrar um Aluno")
    private LocalDate dataNascimento;


    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String genero;

    @Column(name = "estado_civil", nullable = false)
    private String estadoCivil;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String rg;

    @Column(nullable = false)
    private String naturalidade;

    @Column(nullable = true)
    private String cep;

    @Column(nullable = true)
    private String logadouro;

    @Column(nullable = true)
    private String numero;

    @Column(nullable = true)
    private String cidade;

    @Column(nullable = true)
    private String complemento;


    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @NotNull(message = "É necessário um Usuário Valido para cadastrar um Aluno")
    @JsonBackReference
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "id_turma")
    @JsonBackReference
    private TurmaEntity turma;
}
