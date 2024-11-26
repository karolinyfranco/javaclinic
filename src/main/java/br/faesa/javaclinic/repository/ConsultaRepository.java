package br.faesa.javaclinic.repository;

import br.faesa.javaclinic.model.Consulta;
import br.faesa.javaclinic.model.Especialidade;
import br.faesa.javaclinic.model.Medico;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepository {
    private static final String PATH_CONSULTAS = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "consultas.txt";
    private static Long ultimoId = 0L;

    public List<Consulta> listarConsultasDoPaciente(String nomePaciente) {
        List<Consulta> consultas = carregarConsultas();
        List<Consulta> consultasDoPaciente = new ArrayList<>();

        for (Consulta consulta : consultas) {
            if (consulta.getNomePaciente().trim().equalsIgnoreCase(nomePaciente.trim())) {
                consultasDoPaciente.add(consulta);
            }
        }
        return consultasDoPaciente;
    }

    public boolean medicoPossuiOutraConsulta(String nomeMedico, LocalDateTime data) {
        List<Consulta> consultas = carregarConsultas();
        return consultas.stream()
                .anyMatch(consulta -> consulta.getNomeMedico().equalsIgnoreCase(nomeMedico) && consulta.getData().equals(data));
    }

    public Consulta buscarConsultaPorId(Long id) {
        List<Consulta> consultas = carregarConsultas();
        for (Consulta consulta : consultas) {
            if (consulta.getId().equals(id)) {
                return consulta;
            }
        }
        return null; // Retorna null caso não encontre
    }

    public static List<String> buscarMedicosPorEspecialidade(Especialidade especialidade, LocalDateTime data) {
        List<Medico> medicos = MedicoRepository.carregarMedicos();
        List<Consulta> consultas = carregarConsultas();

        List<String> medicosDisponiveis = new ArrayList<>();
        for (Medico medico : medicos) {
            if (medico.getEspecialidade() == especialidade) {
                boolean ocupado = consultas.stream()
                        .anyMatch(consulta -> consulta.getNomeMedico().equalsIgnoreCase(medico.getNome())
                                && consulta.getData().equals(data));
                if (!ocupado) {
                    medicosDisponiveis.add(medico.getNome());
                }
            }
        }
        return medicosDisponiveis;
    }

    public static void agendarConsultas(List<Consulta> novasConsultas) {
        List<Consulta> consultasExistentes = carregarConsultas(); // Carrega as consultas já existentes
        // Adiciona as novas consultas, verificando se já não estão na lista
        for (Consulta novaConsulta : novasConsultas) {
            boolean consultaDuplicada = consultasExistentes.stream()
                    .anyMatch(c -> c.getId().equals(novaConsulta.getId())); // Verifica duplicação pelo ID
            if (!consultaDuplicada) {
                consultasExistentes.add(novaConsulta); // Adiciona apenas se não for duplicada
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_CONSULTAS))) {
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

    public static List<Consulta> carregarConsultas() {
        List<Consulta> consultas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_CONSULTAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                try {
                    String[] dados = linha.split(";\\s*"); // Expressão regular para encontrar ';' seguidos de possíveis espaços em branco
                    if (dados.length == 5) { // Verifica se há exatamente 5 partes
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

    public static void cancelarConsulta(Long id) {
        // Carrega as consultas existentes
        List<Consulta> consultas = carregarConsultas();

        // Verifica e remove a consulta
        boolean consultaRemovida = consultas.removeIf(consulta -> consulta.getId().equals(id));

        if (!consultaRemovida) {
            System.out.println("Consulta com ID " + id + " não encontrada.");
            return;
        }

        // Regrava as consultas no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_CONSULTAS))) {
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
}
