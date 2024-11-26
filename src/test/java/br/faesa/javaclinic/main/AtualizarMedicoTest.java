package br.faesa.javaclinic.main;

import br.faesa.javaclinic.model.Especialidade;
import br.faesa.javaclinic.model.Medico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AtualizarMedicoTest {

    @Test
    @DisplayName("Deveria exibir mensagem confirmando que os dados foram atualizados, CRM e especialidade deveriam permanecer inalterados.")
    void testAtualizarMedico() {
        // Passo 1: Criar um objeto Medico com dados iniciais
        Medico medico = new Medico(
                "Dr. João",
                "joao@gmail.com",
                "Rua A, 123",
                "98 34567-8911",
                "1234",
                Especialidade.CARDIOLOGIA
        );

        // Verificar dados iniciais
        assertEquals("Dr. João", medico.getNome());
        assertEquals("joao@gmail.com", medico.getEmail());
        assertEquals("Rua A, 123", medico.getEndereco());
        assertEquals("98 34567-8911", medico.getTelefone());
        assertEquals("1234", medico.getCrm());
        assertEquals(Especialidade.CARDIOLOGIA, medico.getEspecialidade());

        // Passo 2: Atualizar dados do médico
        String novoNome = "Dr. João Carlos";
        String novoTelefone = "98 76543-2100";
        String novoEndereco = "Rua B, 456";

        medico.atualizar(novoNome, novoTelefone, novoEndereco);

        // Passo 3: Verificar se os campos atualizados possuem os novos valores
        assertEquals(novoNome, medico.getNome(), "O nome não foi atualizado corretamente.");
        assertEquals(novoTelefone, medico.getTelefone(), "O telefone não foi atualizado corretamente.");
        assertEquals(novoEndereco, medico.getEndereco(), "O endereço não foi atualizado corretamente.");

        // Passo 4: Garantir que os campos não fornecidos mantiveram seus valores originais
        assertEquals("1234", medico.getCrm(), "O CRM foi alterado indevidamente.");
        assertEquals(Especialidade.CARDIOLOGIA, medico.getEspecialidade(), "A especialidade foi alterada indevidamente.");
    }

    @Test
    void testAtualizarNomeMedicoComSucesso() {
        // Arrange
        Medico medico = new Medico(
                "Dr. João",
                "joao@gmail.com",
                "Rua A, 123",
                "98 34567-8911",
                "1234",
                Especialidade.CARDIOLOGIA
        );

        // Act
        medico.atualizar("Dr. João Atualizado", "" , "");

        // Assert
        assertEquals("Dr. João Atualizado", medico.getNome(), "O nome não foi atualizado corretamente.");
        assertEquals("joao@gmail.com", medico.getEmail(), "O email foi alterado indevidamente.");
        assertEquals("Rua A, 123", medico.getEndereco(), "O endereço foi alterado indevidamente.");
        assertEquals("98 34567-8911", medico.getTelefone(), "O telefone foi alterada indevidamente.");
        assertEquals("1234", medico.getCrm(), "O CRM foi alterado indevidamente.");
        assertEquals(Especialidade.CARDIOLOGIA, medico.getEspecialidade(), "A especialidade foi alterada indevidamente.");

    }
}
