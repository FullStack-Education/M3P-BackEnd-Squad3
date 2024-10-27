package br.com.fullstackedu.labpcp.entity;

import br.com.fullstackedu.labpcp.database.entity.DocenteEntity;
import br.com.fullstackedu.labpcp.database.entity.MateriaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DocenteEntityTest {


    private DocenteEntity docente;

    @BeforeEach
    void setUp() {
        docente = new DocenteEntity();
        docente.setNome("Maria Silva");
        docente.setDataEntrada(LocalDate.of(2023, 1, 1));
        docente.setTelefone("11987654321");
        docente.setGenero("Feminino");
        docente.setEstadoCivil("Solteiro");
        docente.setDataNascimento(LocalDate.of(1990, 5, 20));
        docente.setEmail("maria.silva@email.com");
        docente.setCPF("12345678901");
        docente.setRG("MG123456");
        docente.setNaturalidade("Brasileira");
        docente.setCep("12345000");
        docente.setLogradouro("Rua das Flores");
        docente.setNumero("123");
        docente.setCidade("São Paulo");
        docente.setEstado("SP");
        docente.setComplemento("Apt 101");

//        docente.setMaterias(new HashSet<>());
    }

    @Test
    void testDocenteFields() {
        assertEquals("Maria Silva", docente.getNome());
        assertEquals(LocalDate.of(2023, 1, 1), docente.getDataEntrada());
        assertEquals("11987654321", docente.getTelefone());
        assertEquals("Feminino", docente.getGenero());
        assertEquals("Solteiro", docente.getEstadoCivil());
        assertEquals(LocalDate.of(1990, 5, 20), docente.getDataNascimento());
        assertEquals("maria.silva@email.com", docente.getEmail());
        assertEquals("12345678901", docente.getCPF());
        assertEquals("MG123456", docente.getRG());
        assertEquals("Brasileira", docente.getNaturalidade());
        assertEquals("12345000", docente.getCep());
        assertEquals("Rua das Flores", docente.getLogradouro());
        assertEquals("123", docente.getNumero());
        assertEquals("São Paulo", docente.getCidade());
        assertEquals("SP", docente.getEstado());
        assertEquals("Apt 101", docente.getComplemento());
    }

//    @Test
//    void testAddMateria() {
//        MateriaEntity materia = new MateriaEntity();
//        materia.setNome("Matemática");
//
//        docente.addMateria(materia);
//
//        assertNotNull(docente.getMaterias());
//        assertEquals(1, docente.getMaterias().size());
//        assertEquals("Matemática", docente.getMaterias().iterator().next().getNome());
//    }
}