package br.faesa.javaclinic.repository;

import br.faesa.javaclinic.model.Paciente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PacienteRepository {
    private static final String PATH_PACIENTES = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "pacientes.txt";

    public static Paciente buscarPacientePorCpf(String cpf) {
        List<Paciente> pacientes = carregarPacientes();
        for (Paciente paciente : pacientes) {
            if (paciente.getCpf().equals(cpf)) {
                return paciente;
            }
        }
        return null; // Retorna null caso não encontre
    }

    public static void salvarPacientes(List<Paciente> pacientes) {
        List<Paciente> pacientesUnicos = pacientes.stream()
                .distinct()
                .collect(Collectors.toList()); // Remove duplicatas
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_PACIENTES))) {
            for (Paciente p : pacientesUnicos) {
                writer.write(p.getNome() + ";" +
                        p.getEmail() + ";" +
                        p.getEndereco() + ";" +
                        p.getTelefone() + ";" +
                        p.getCpf());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pacientes: " + e.getMessage());
        }
    }

    public static List<Paciente> carregarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_PACIENTES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";\\s*"); // Expressão regular para encontrar ';' seguidos de possíveis espaços em branco
                if (dados.length == 5) {
                    try {
                        String nome = dados[0].trim();
                        String email = dados[1].trim();
                        String endereco = dados[2].trim();
                        String telefone = dados[3].trim();
                        String cpf = dados[4].trim();

                        pacientes.add(new Paciente(nome, email, endereco, telefone, cpf));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro ao processar linha: " + linha);
                    }
                } else {
                    System.out.println("Linha inválida ignorada: " + linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar pacientes: " + e.getMessage());
        }
        return pacientes;
    }

    public static void atualizarPaciente(String cpf, String nome, String telefone, String endereco) {
        List<Paciente> pacientes = carregarPacientes(); // Carrega todos os pacientes do arquivo
        Paciente paciente = null;

        // Procura o paciente na lista
        for (Paciente p : pacientes) {
            if (p.getCpf().equals(cpf)) {
                paciente = p;  // Atualiza o objeto 'paciente' com o paciente encontrado
                break;  // Encontra o paciente e sai do loop
            }
        }

        if (paciente != null) {
            System.out.println("Antes da atualização: " + paciente);
            paciente.atualizar(nome, telefone, endereco);
            System.out.println("Depois da atualização: " + paciente);
            salvarPacientes(pacientes);
        }
    }

    public static void excluirPaciente(String cpf) {
        List<Paciente> pacientes = carregarPacientes(); // Carrega os pacientes do arquivo
        Paciente paciente = buscarPacientePorCpf(cpf); // Encontra o paciente pelo CPF

        if (paciente != null) {
            pacientes.remove(paciente); // Remove o paciente da lista
            salvarPacientes(pacientes); // Salva a lista atualizada
        } else {
            System.out.println("Paciente com CPF " + cpf + " não encontrado.");
        }
    }
}
