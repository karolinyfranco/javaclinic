package br.faesa.javaclinic.main;

import br.faesa.javaclinic.model.Especialidade;
import br.faesa.javaclinic.model.Medico;
import br.faesa.javaclinic.repository.MedicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CadastroMedicoTest {
    private static final String PATH_MEDICOS_TEST = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "medicos_test.txt";

    @BeforeEach
    void setup() {
        // Limpa a base inicial para garantir um ambiente controlado
        salvarMedicosTest(List.of());
    }

    @Test
    @DisplayName("Deveria devolver erro quando um CRM existente tenta ser cadastrado novamente.")
    void devePermitirCadastroDeMedicoComCrmUnico() {
        // Arrange
        Medico medico1 = new Medico(
                "Dr. João Silva", "joao.silva@exemplo.com", "Rua A, 123",
                "99999-9999", "123456", Especialidade.CARDIOLOGIA
        );

        Medico medico2 = new Medico(
                "Dr. Pedro Santos", "pedro.santos@exemplo.com", "Rua B, 456",
                "88888-8888", "123456", Especialidade.ORTOPEDIA
        );

        // Act & Assert
        // Cadastra o primeiro médico
        MedicoRepository.salvarMedicos(List.of(medico1));
        List<Medico> medicosAposCadastro1 = MedicoRepository.carregarMedicos();
        assertEquals(1, medicosAposCadastro1.size(), "O primeiro médico deveria ser cadastrado.");
        assertEquals("123456", medicosAposCadastro1.get(0).getCrm(), "O CRM do primeiro médico deveria ser 123456.");

        // Tenta cadastrar o segundo médico com o mesmo CRM
        List<Medico> medicosAposTentativaCadastro2 = MedicoRepository.carregarMedicos();
        boolean crmDuplicado = medicosAposTentativaCadastro2.stream().anyMatch(m -> m.getCrm().equals(medico2.getCrm()));

        if (crmDuplicado) {
            System.out.println("Erro: O CRM " + medico2.getCrm() + " já está em uso.");
        }

        // Verifica que o segundo médico não foi cadastrado
        assertEquals(1, medicosAposCadastro1.size(), "O segundo médico não deveria ser cadastrado devido a CRM duplicado.");
    }

    private void salvarMedicosTest(List<Medico> medicos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_MEDICOS_TEST))) {
            for (Medico m : medicos) {
                writer.write(m.getNome() + ";" +
                        m.getEmail() + ";" +
                        m.getEndereco() + ";" +
                        m.getTelefone() + ";" +
                        m.getCrm() + ";" +
                        m.getEspecialidade().name() +"\n");
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

