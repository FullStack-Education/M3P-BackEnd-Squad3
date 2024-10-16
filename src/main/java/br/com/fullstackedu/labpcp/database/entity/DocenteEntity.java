package br.com.fullstackedu.labpcp.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

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
}
