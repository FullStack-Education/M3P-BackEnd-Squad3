package br.com.fullstackedu.labpcp.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "nota")
public class NotaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    @NotNull(message = "É necessário um Aluno válido para cadastrar uma Nota")
    private AlunoEntity aluno;

    @ManyToOne
    @JoinColumn(name = "id_professor")
    @NotNull(message = "É necessário um Docente(professor) válido para cadastrar uma Nota")
    private DocenteEntity professor;

    @ManyToOne
    @JoinColumn(name = "id_materia")
    @NotNull(message = "É necessário uma Materia válida para cadastrar uma Nota")
    private MateriaEntity materia;

    @ManyToOne
    @JoinColumn(name = "id_turma")
    @NotNull(message = "É necessário uma turma válida para cadastrar uma Nota")
    private TurmaEntity turma;

    @Column(name = "nome",nullable = true)
    private String nome;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "data")
    private LocalDate data;

}
