package br.com.fullstackedu.labpcp.database.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name= "papel")
public class PapelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String nome;
}
