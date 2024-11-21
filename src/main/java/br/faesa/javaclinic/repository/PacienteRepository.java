package br.faesa.javaclinic.repository;

import br.faesa.javaclinic.model.Paciente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {
    private static final String PATH_PACIENTES = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "pacientes.txt";

    public static void salvarPacientes(List<Paciente> pacientes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_PACIENTES))) {
            for (Paciente p : pacientes) {
                writer.write(p.getNome() + ";" + p.getEmail() + ";" + p.getEndereco() + ";" + p.getTelefone() + ";" + p.getCpf() +"\n");
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
                String[] dados = linha.split(";\\s*"); // Usa uma expressão regular para ignorar espaços ao redor da vírgula
                if (dados.length == 5) { // Verifica se há exatamente 6 partes (ajustar de acordo com a quantidade de atributos)
                    String nome = dados[0].trim();
                    String email = dados[1].trim();
                    String cpf = dados[2].trim();
                    String endereco = dados[3].trim();
                    String telefone = dados[4].trim();

                    // Adiciona um novo objeto Paciente com os valores convertidos
                    pacientes.add(new Paciente(nome, email, endereco, telefone, cpf));
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return pacientes;
    }
}
