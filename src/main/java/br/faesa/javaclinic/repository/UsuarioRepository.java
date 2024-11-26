package br.faesa.javaclinic.repository;

import br.faesa.javaclinic.service.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioRepository {
    private static final String PATH_USUARIOS = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "usuarios.txt";

    public static void salvarUsuarios(List<Usuario> usuarios) {
        List<Usuario> usuariosUnicos = usuarios.stream()
                .distinct()
                .collect(Collectors.toList()); // Remove duplicatas
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_USUARIOS))) {
            for (Usuario u : usuariosUnicos) {
                writer.write(u.getUsuario() + ";"
                        + u.getSenha() + ";"
                        + u.getTipo());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }

    public static List<Usuario> carregarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_USUARIOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";\\s*"); // Expressão regular para encontrar ';' seguidos de possíveis espaços em branco
                if (dados.length == 3) { // Verifica se há exatamente 3 partes
                    usuarios.add(new Usuario(dados[0].trim(),
                            dados[1].trim(),
                            dados[2].charAt(0)));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar usuários: " + e.getMessage());
        }
        return usuarios;
    }
}
