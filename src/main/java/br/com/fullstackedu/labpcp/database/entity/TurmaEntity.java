package br.com.fullstackedu.labpcp.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
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

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim",nullable = false)
    private LocalDate dataFim;

    @Column(nullable = false)
    private String hora;

    @OneToMany(mappedBy = "turma", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    List<AlunoEntity> alunos;

    @OneToMany(mappedBy = "turma", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<NotaEntity> notas;

    @ManyToOne
    @JoinColumn(name = "id_professor")
    @JsonIgnore()
    private DocenteEntity professor;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    @NotNull(message = "É necessário um Curso Válido para cadastrar uma turma")
    @JsonIgnore
    private CursoEntity curso;


}



