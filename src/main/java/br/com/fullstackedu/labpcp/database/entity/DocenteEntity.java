package br.com.fullstackedu.labpcp.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "docente")
public class DocenteEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String nome;

        @Column(name = "data_entrada")
        private LocalDate dataEntrada;


        @ManyToOne
        @JoinColumn(name = "id_usuario")
        @NotNull(message = "É necessário um Usuário Valido para cadastrar um Docente")
        @JsonBackReference
        private UsuarioEntity usuario;

        public DocenteEntity(String nome, LocalDate dataEntrada, UsuarioEntity usuario) {
                this.nome = nome;
                this.dataEntrada = dataEntrada;
                this.usuario = usuario;
        }

        public DocenteEntity() {

        }

        @ManyToMany
        @JoinTable(
                name = "docente_materias",
                joinColumns = @JoinColumn(name = "docente_id"),
                inverseJoinColumns = @JoinColumn(name = "materia_id")
        )
        private Set<MateriaEntity> materias;


        public void addMateria(MateriaEntity materia) {
                this.materias.add(materia);
                materia.getDocentes().add(this);
        }

        public void removeMateria(MateriaEntity materia) {
                this.materias.remove(materia);
                materia.getDocentes().remove(this);
        }
}
