package br.com.fullstackedu.labpcp.dto;

import br.com.fullstackedu.labpcp.controller.dto.request.DocenteUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DocenteUpdateRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private DocenteUpdateRequest createDocenteUpdateRequest(
            String nome, LocalDate dataEntrada, String telefone, String genero, String estadoCivil,
            LocalDate dataNascimento, String email, String CPF, String RG, String naturalidade,
            String cep, String logradouro, String numero, String cidade, String estado, String complemento, Long idUsuario
    ) {
        return new DocenteUpdateRequest(
                nome, dataEntrada, telefone, genero, estadoCivil, dataNascimento, email, CPF, RG,
                naturalidade, cep, logradouro, numero, cidade, estado, complemento, idUsuario
        );
    }

    @ParameterizedTest
    @CsvSource({
            "null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null",  // All null - invalid
            "'João Silva', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null",  // nome only - valid
            "null, 2020-01-01, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null"    // dataEntrada only - valid
    })
    void testAtLeastOneFieldNotNullValidation(ArgumentsAccessor args) {
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
        String complemento = !"null".equals(args.getString(15)) ? args.getString(15) : null;
        Long idUsuario = !"null".equals(args.getString(16)) ? args.getLong(16) : null;

        DocenteUpdateRequest request = createDocenteUpdateRequest(
                nome, dataEntrada, telefone, genero, estadoCivil, dataNascimento, email, CPF, RG,
                naturalidade, cep, logradouro, numero, cidade, estado, complemento, idUsuario
        );

        Set<ConstraintViolation<DocenteUpdateRequest>> violations = validator.validate(request);

        if (nome == null && dataEntrada == null && telefone == null && genero == null && estadoCivil == null &&
                dataNascimento == null && email == null && CPF == null && RG == null && naturalidade == null &&
                cep == null && logradouro == null && numero == null && cidade == null && estado == null &&
                complemento == null && idUsuario == null) {
            assertFalse(violations.isEmpty(), "Expected violation for all-null fields");
        } else {
            assertTrue(violations.isEmpty(), "Expected no violations when at least one field is non-null");
        }
    }
}