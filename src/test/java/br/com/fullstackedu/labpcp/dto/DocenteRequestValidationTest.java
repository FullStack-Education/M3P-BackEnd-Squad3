package br.com.fullstackedu.labpcp.dto;


import br.com.fullstackedu.labpcp.controller.dto.request.DocenteCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DocenteRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private DocenteCreateRequest createValidDocenteRequest(
            String nome, LocalDate dataEntrada, String telefone, String genero, String estadoCivil,
            LocalDate dataNascimento, String email, String CPF, String RG, String naturalidade,
            String cep, String logradouro, String numero, String cidade, String estado, Long idUsuario
    ) {
        return new DocenteCreateRequest(
                nome, dataEntrada, telefone, genero, estadoCivil, dataNascimento, email, CPF, RG, naturalidade,
                cep, logradouro, numero, cidade, estado, "Apt 101", idUsuario
        );
    }

    @ParameterizedTest
    @CsvSource({
            "null, 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // nome
            "'João Silva', null, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // dataEntrada
            "'João Silva', 2020-01-01, null, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // telefone
            "'João Silva', 2020-01-01, 123456789, null, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // genero
            "'João Silva', 2020-01-01, 123456789, Masculino, null, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // estadoCivil
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, null, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // dataNascimento
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, null, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // email
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, null, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // CPF
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, null, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // RG
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, null, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // naturalidade
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, null, Rua dos Exemplo, 123, Belo Horizonte, MG, 1",  // cep
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, null, 123, Belo Horizonte, MG, 1",  // logradouro
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, null, Belo Horizonte, MG, 1",  // numero
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, null, MG, 1",  // cidade
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, null, 1",  // estado
            "'João Silva', 2020-01-01, 123456789, Masculino, Solteiro, 1985-05-15, joao.silva@example.com, 12345678901, MG1234567, Belo Horizonte, 30140-000, Rua dos Exemplo, 123, Belo Horizonte, MG, null"  // idUsuario
    })

    void testRequiredFieldValidation(ArgumentsAccessor args) {
        String nome = !"null".equals(args.getString(0)) ? args.getString(0) : null;
        LocalDate dataEntrada = !"null".equals(args.getString(1)) ? args.get(1, LocalDate.class) : null;
        String telefone = !"null".equals(args.getString(2)) ? args.getString(2) : null;
        String genero = !"null".equals(args.getString(3)) ? args.getString(3) : null;
        String estadoCivil = !"null".equals(args.getString(4)) ? args.getString(4) : null;
        LocalDate dataNascimento = !"null".equals(args.getString(5)) ? args.get(5, LocalDate.class) : null;
        String email = !"null".equals(args.getString(6)) ? args.getString(6) : null;
        String CPF = !"null".equals(args.getString(7)) ? args.getString(7) : null;
        String RG = !"null".equals(args.getString(8)) ? args.getString(8) : null;
        String naturalidade = !"null".equals(args.getString(9)) ? args.getString(9) : null;
        String cep = !"null".equals(args.getString(10)) ? args.getString(10) : null;
        String logradouro = !"null".equals(args.getString(11)) ? args.getString(11) : null;
        String numero = !"null".equals(args.getString(12)) ? args.getString(12) : null;
        String cidade = !"null".equals(args.getString(13)) ? args.getString(13) : null;
        String estado = !"null".equals(args.getString(14)) ? args.getString(14) : null;
        Long idUsuario = !"null".equals(args.getString(15)) ? args.getLong(15) : null;

        DocenteCreateRequest request = createValidDocenteRequest(
                nome, dataEntrada, telefone, genero, estadoCivil, dataNascimento, email, CPF, RG,
                naturalidade, cep, logradouro, numero, cidade, estado, idUsuario
        );

        Set<ConstraintViolation<DocenteCreateRequest>> violations = validator.validate(request);
        System.out.println(violations);
        assertFalse(violations.isEmpty(), "Expected violations for missing required field");
    }
}