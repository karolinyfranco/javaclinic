package br.faesa.javaclinic.main;

import br.faesa.javaclinic.model.Paciente;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CadastroPacienteTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())  // Desabilita o EL
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("Deveria devolver erro de validação de CPF e email.")
    public void testCadastroPacienteComDadosInvalidos() {
        Paciente paciente = new Paciente("João da Silva", "joaosilva.email",
                "Rua das Flores, 123, Vitória", "27 99800-1234",
                "123.456.789");

        Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);

        // Log das mensagens de validação para depuração
        violations.forEach(v -> System.out.println("Mensagem de erro: " + v.getMessage()));

        assertFalse(violations.isEmpty(), "Era esperado pelo menos um erro de validação.");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Formato do CPF é inválido")), "Mensagem de CPF inválido deveria estar presente.");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Formato do email é inválido")), "Mensagem de email inválido deveria estar presente.");
    }

    @Test
    @DisplayName("Não deveria devolver nenhum erro de validação de dados.")
    public void testCadastroPacienteComDadosValidos() {
        Paciente paciente = new Paciente("João da Silva", "joao.silva@gmail.com",
                "Rua das Flores, 123, Vitória", "27 99800-1234",
                "123.456.789-00");

        Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);

        assertTrue(violations.isEmpty(), "Era esperado que não houvesse erro de validação");
    }
}
