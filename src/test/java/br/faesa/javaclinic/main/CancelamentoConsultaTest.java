package br.faesa.javaclinic.main;

import br.faesa.javaclinic.model.Consulta;
import br.faesa.javaclinic.model.Especialidade;
import br.faesa.javaclinic.service.validator.ValidacaoException;
import org.junit.jupiter.api.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CancelamentoConsultaTest {
    private static final String PATH_CONSULTAS_TEST = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "consultas_test.txt";
    private static Long ultimoId = 0L;

    @BeforeEach
    void limparArquivoAntesDeCadaTeste() {
        limparArquivoConsultas();
        ultimoId = 0L;
    }

    @AfterEach
    void limparArquivoAposCadaTeste() {
        limparArquivoConsultas();
    }

    @Test
    @DisplayName("Deveria excluir a consulta de ID 1 da lista e do arquivo, mas manter a consulta de ID 2.")
    public void testCancelarConsulta() {
        // Arrange
        Consulta consulta1 = new Consulta(
                1L,
                "Dr. João",
                "Paciente A",
                Especialidade.CARDIOLOGIA,
                LocalDateTime.of(2024, 12, 1, 15, 30)
        );
        Consulta consulta2 = new Consulta(
                2L,
                "Dra. Ana",
                "Paciente B",
                Especialidade.DERMATOLOGIA,
                LocalDateTime.of(2024, 12, 2, 13, 0)
        );

        agendarConsultasTest(Arrays.asList(consulta1, consulta2));

        // Act
        // ID da consulta a ser cancelada
        Long idConsultaACancelar = 1L;

        // Cancelar a consulta
        cancelarConsultaTest(idConsultaACancelar);

        // Assert
        List<Consulta> consultas = carregarConsultasTest();

        // Verificar se a consulta com ID 1 foi removida
        assertFalse(
                consultas.stream().anyMatch(c -> c.getId().equals(idConsultaACancelar)),
                "Consulta com ID " + idConsultaACancelar + " não foi removida."
        );

        // Verificar se as demais consultas permanecem inalteradas
        assertEquals(1, consultas.size(), "A lista de consultas tem um número inesperado de elementos.");
        Consulta consultaRemanescente = consultas.get(0);
        assertEquals(2L, consultaRemanescente.getId(), "Consulta remanescente não corresponde ao esperado.");
        assertEquals("Dra. Ana", consultaRemanescente.getNomeMedico(), "Nome do médico incorreto na consulta remanescente.");
        assertEquals("Paciente B", consultaRemanescente.getNomePaciente(), "Nome do paciente incorreto na consulta remanescente.");
        assertFalse(consultas.contains(consulta1), "A consulta 1 ainda está presente na lista.");
        assertTrue(consultas.contains(consulta2), "A consulta 2 não foi encontrada na lista.");
    }

    @Test
    @DisplayName("Deveria lançar uma exceção devido ao cancelamento com menos de 24 horas.")
    public void testCancelamentoConsultaComMenosDe24Horas() {
        // Arrange - Agendar consulta com menos de 24h
        Consulta consulta = new Consulta(
                101L,
                "Dr. João Silva",
                "Maria Oliveira",
                Especialidade.CARDIOLOGIA,
                LocalDateTime.now().plusHours(23) // Menos de 24 horas
        );

        agendarConsultasTest(Collections.singletonList(consulta));

        // Act e Assert - Cancelamento com menos de 24 horas
        try {
            validarHorarioAntecedenciaCancelamentoTest(consulta.getId());
            fail("Deveria ter lançado uma exceção devido ao cancelamento com menos de 24 horas.");
        } catch (ValidacaoException e) {
            assertEquals("Consulta somente pode ser cancelada com antecedência mínima de 24h!", e.getMessage());
        }
    }

    @Test
    @DisplayName("Não deveria lançar uma exceção para cancelamento com mais de 24 horas.")
    public void testCancelamentoConsultaComMaisDe24Horas() {
        // Arrange - Agendar consulta com mais de 24h
        Consulta consulta = new Consulta(
                102L,
                "Dr. João Silva",
                "Maria Oliveira",
                Especialidade.CARDIOLOGIA,
                LocalDateTime.now().plusHours(25) // Mais de 24 horas
        );

        agendarConsultasTest(Collections.singletonList(consulta));

        // Act e Assert - Cancelamento com mais de 24 horas
        try {
            validarHorarioAntecedenciaCancelamentoTest(consulta.getId());
            cancelarConsultaTest(consulta.getId());

            // Verificar se a consulta foi removida
            List<Consulta> consultas = carregarConsultasTest();
            assertFalse(consultas.contains(consulta));
        } catch (ValidacaoException e) {
            fail("Não deveria ter lançado uma exceção para cancelamento com mais de 24 horas.");
        }
    }

    private void limparArquivoConsultas() {
        // Limpa o conteúdo do arquivo para garantir a execução isolada dos testes
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_CONSULTAS_TEST))) {
            writer.write(""); // Escreve um arquivo vazio
        } catch (IOException e) {
            e.printStackTrace();
            fail("Erro ao limpar o arquivo de consultas.");
        }
    }

    private void agendarConsultasTest(List<Consulta> novasConsultas) {
        List<Consulta> consultasExistentes = carregarConsultasTest(); // Carrega as consultas já existentes
        // Adiciona as novas consultas, verificando se já não estão na lista
        for (Consulta novaConsulta : novasConsultas) {
            boolean consultaDuplicada = consultasExistentes.stream()
                    .anyMatch(c -> c.getId().equals(novaConsulta.getId())); // Verifica duplicação pelo ID
            if (!consultaDuplicada) {
                consultasExistentes.add(novaConsulta); // Adiciona apenas se não for duplicada
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_CONSULTAS_TEST))) {
            for (Consulta c : consultasExistentes) {
                // Verifica se a consulta ainda não tem um ID
                if (c.getId() == null || c.getId() == 0) {
                    ultimoId++; // Incrementa o ID
                    c.setId(ultimoId); // Atribui o novo ID à consulta
                }
                writer.write(
                        c.getId() + ";" +
                                c.getNomeMedico() + ";" +
                                c.getNomePaciente() + ";" +
                                c.getEspecialidade().name() + ";" +
                                c.getData() + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Consulta> carregarConsultasTest() {
        List<Consulta> consultas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_CONSULTAS_TEST))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                try {
                    String[] dados = linha.split(";\\s*");
                    if (dados.length != 5) { // Apenas pule linhas inválidas
                        System.out.println("Linha inválida encontrada: " + linha);
                        continue;
                    }

                    Long id = Long.parseLong(dados[0].trim());
                    String nomeMedico = dados[1].trim();
                    String nomePaciente = dados[2].trim();
                    Especialidade especialidade = Especialidade.valueOf(dados[3].trim().toUpperCase());
                    LocalDateTime data = LocalDateTime.parse(dados[4].trim());

                    Consulta consulta = new Consulta(id, nomeMedico, nomePaciente, especialidade, data);
                    consultas.add(consulta);

                    // Atualiza o maior ID encontrado
                    if (id > ultimoId) {
                        ultimoId = id;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Erro ao interpretar a data/hora na linha: " + linha);
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro ao interpretar especialidade ou outro campo: " + linha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    private void cancelarConsultaTest(Long id) {
        // Carrega as consultas existentes
        List<Consulta> consultas = carregarConsultasTest();

        // Verifica e remove a consulta
        boolean consultaRemovida = consultas.removeIf(consulta -> consulta.getId().equals(id));

        if (!consultaRemovida) {
            System.out.println("Consulta com ID " + id + " não encontrada.");
            return;
        }

        // Regrava as consultas no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_CONSULTAS_TEST))) {
            for (Consulta consulta : consultas) {
                writer.write(
                        consulta.getId() + ";" +
                                consulta.getNomeMedico() + ";" +
                                consulta.getNomePaciente() + ";" +
                                consulta.getEspecialidade().name() + ";" +
                                consulta.getData() + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Consulta buscarConsultaPorIdTest(Long id) {
        List<Consulta> consultas = carregarConsultasTest();
        for (Consulta consulta : consultas) {
            if (consulta.getId().equals(id)) {
                return consulta;
            }
        }
        return null; // Retorna null caso não encontre
    }

    private void validarHorarioAntecedenciaCancelamentoTest(Long id) {
        // Busca a consulta pelo ID
        Consulta consulta = buscarConsultaPorIdTest(id);
        if (consulta == null) {
            throw new ValidacaoException("Consulta não encontrada.");
        }

        LocalDateTime agora = LocalDateTime.now();
        long diferencaEmHoras = Duration.between(agora, consulta.getData()).toHours();

        if (diferencaEmHoras < 24) {
            throw new ValidacaoException("Consulta somente pode ser cancelada com antecedência mínima de 24h!");
        }
    }
}