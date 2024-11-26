package br.faesa.javaclinic.repository;

import br.faesa.javaclinic.model.Medico;
import br.faesa.javaclinic.model.Paciente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_PACIENTES))) {
            for (Paciente p : pacientes) {
                writer.write(p.getNome() + ";" +
                        p.getEmail() + ";" +
                        p.getEndereco() + ";" +
                        p.getTelefone() + ";" +
                        p.getCpf() +"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Paciente> carregarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_PACIENTES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";\\s*"); // Expressão regular para encontrar ';' seguidos de possíveis espaços em branco
                if (dados.length == 5) { // Verifica se há exatamente 5 partes
                    String nome = dados[0].trim();
                    String email = dados[1].trim();
                    String endereco = dados[2].trim();
                    String telefone = dados[3].trim();
                    String cpf = dados[4].trim();

                    // Adiciona um novo objeto Paciente com os valores convertidos
                    pacientes.add(new Paciente(nome, email, endereco, telefone, cpf));
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return pacientes;
    }

    public static void atualizarPaciente(String cpf, String nome, String telefone, String endereco) {
        List<Paciente> pacientes = carregarPacientes(); // Carrega todos os pacientes do arquivo
        Paciente paciente = null;

        // Procura o paciente na lista
        for (Paciente p : pacientes) {
            if (p.getCpf().equals(cpf)) {
                paciente = p;  // Atualiza o objeto 'paciente' com o médico encontrado
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
        List<Paciente> pacientes = carregarPacientes();
        Paciente paciente = buscarPacientePorCpf(cpf);
        if (paciente != null) {
            pacientes.remove(paciente);
            salvarPacientes(pacientes); // Salva os pacientes restantes no arquivo
        }
    }
}
