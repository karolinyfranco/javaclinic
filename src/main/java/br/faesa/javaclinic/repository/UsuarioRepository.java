package br.faesa.javaclinic.repository;

import br.faesa.javaclinic.model.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    private static final String PATH_USUARIOS = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "usuarios.txt";

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
