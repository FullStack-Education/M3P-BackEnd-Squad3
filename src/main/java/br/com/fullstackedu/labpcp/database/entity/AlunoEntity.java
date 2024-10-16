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
