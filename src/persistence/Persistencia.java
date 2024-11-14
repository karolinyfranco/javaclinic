package persistence;

import model.Especialidade;
import model.Medico;
import model.Paciente;
import model.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Persistencia {
    private static final String PATH_PACIENTES = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "pacientes.txt";
    private static final String PATH_MEDICOS = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "medicos.txt";
    private static final String PATH_USUARIOS = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "usuarios.txt";

    public static void salvarMedicos(List<Medico> medicos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_MEDICOS))) {
            for (Medico m : medicos) {
                writer.write(m.getNome() + "-" + m.getIdade() + "-" + m.getEndereco() + "-" + m.getTelefone() + "-" + m.getCrm() + "-" + m.getEspecialidade() +"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Medico> carregarMedicos() {
        List<Medico> medicos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_MEDICOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("-\\s*"); // Usa uma expressão regular para ignorar espaços ao redor da vírgula
                if (dados.length == 6) { // Verifica se há exatamente 6 partes (ajuste de acordo com a quantidade de atributos)
                    String nome = dados[0].trim();
                    int idade = Integer.parseInt(dados[1].trim()); // Converte dados[1] para int
                    String crm = dados[2].trim();
                    String endereco = dados[3].trim();
                    String telefone = dados[4].trim();
                    Especialidade especialidade = Especialidade.valueOf(dados[5].trim().toUpperCase()); // Converte dados[5] para o tipo enum Especialidade

                    // Adiciona um novo objeto Medico com os valores convertidos
                    medicos.add(new Medico(nome, idade, endereco, telefone, crm, especialidade));
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return medicos;
    }

    public static void salvarPacientes(List<Paciente> pacientes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_PACIENTES))) {
            for (Paciente p : pacientes) {
                writer.write(p.getNome() + "-" + p.getIdade() + "-" + p.getEndereco() + "-" + p.getTelefone() + "-" + p.getCpf() +"\n");
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
                String[] dados = linha.split("-\\s*"); // Usa uma expressão regular para ignorar espaços ao redor da vírgula
                if (dados.length == 5) { // Verifica se há exatamente 6 partes (ajuste de acordo com a quantidade de atributos)
                    String nome = dados[0].trim();
                    int idade = Integer.parseInt(dados[1].trim()); // Converte dados[1] para int
                    String cpf = dados[2].trim();
                    String endereco = dados[3].trim();
                    String telefone = dados[4].trim();

                    // Adiciona um novo objeto Paciente com os valores convertidos
                    pacientes.add(new Paciente(nome, idade, endereco, telefone, cpf));
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return pacientes;
    }

    public static void salvarUsuarios(List<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_USUARIOS))) {
            for (Usuario u : usuarios) {
                writer.write(u.getUsuario() + "-" + u.getSenha() + "-" + u.getTipo() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Usuario> carregarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_USUARIOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("-\\s*"); // Usa uma expressão regular para ignorar espaços ao redor da vírgula
                if (dados.length == 3) { // Verifica se há exatamente 3 partes
                    usuarios.add(new Usuario(dados[0].trim(), dados[1].trim(), dados[2].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
}

