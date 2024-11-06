package br.com.fullstackedu.labpcp.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "curso")
public class CursoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<TurmaEntity> turmas;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<MateriaEntity> materias;

}