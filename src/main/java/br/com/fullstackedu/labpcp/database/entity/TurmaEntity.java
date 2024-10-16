package br.com.fullstackedu.labpcp.database.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "turma")
public class TurmaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<AlunoEntity> alunos;

    @ManyToOne
    @JoinColumn(name = "id_professor")
    @JsonManagedReference
    private DocenteEntity professor;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    @NotNull(message = "É necessário um Curso Válido para cadastrar uma turma")
    @JsonManagedReference
    private CursoEntity curso;
}



