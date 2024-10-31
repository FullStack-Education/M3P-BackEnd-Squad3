package br.com.fullstackedu.labpcp.database.entity;

import com.fasterxml.jackson.annotation.*;
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


        @NotNull
        @Column(nullable = false)
        private String telefone;

        @NotNull
        @Column(nullable = false)
        private String genero;

        @NotNull
        @Column(nullable = false)
        private String estadoCivil;

        @NotNull
        @Column(nullable = false)
        private LocalDate dataNascimento;

        @NotNull
        @Column(nullable = false)
        private String email;

        @NotNull
        @Column(nullable = false)
        private String CPF;

        @NotNull
        @Column(nullable = false)
        private String RG;

        @NotNull
        @Column(nullable = false)
        private String naturalidade;

        @NotNull
        @Column(nullable = false)
        private String cep;

        @NotNull
        @Column(nullable = false)
        private String logradouro;

        @NotNull
        @Column(nullable = false)
        private String numero;

        @NotNull
        @Column(nullable = false)
        private String cidade;

        @NotNull
        @Column(nullable = false)
        private String estado;

        @Column
        private String complemento;

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
        @JsonBackReference
        private Set<MateriaEntity> materias;


        public void addMateria(MateriaEntity materia) {
                this.materias.add(materia);
        }

        public void removeMateria(MateriaEntity materia) {
                this.materias.remove(materia);
        }
}
