package br.faesa.javaclinic.service;

import br.faesa.javaclinic.repository.UsuarioRepository;

import java.util.List;
import java.util.Scanner;

public class LoginService {
    private static Scanner scanner = new Scanner(System.in);

    public static Usuario fazerLogin() {
        // Método para carregar dados dos usuários
        List<Usuario> usuariosList = UsuarioRepository.carregarUsuarios();
        System.out.print("Digite seu nome de usuário: ");
        String usuario = scanner.nextLine();
        System.out.print("Digite sua senha: ");
        String senha = scanner.nextLine();

        for (Usuario u : usuariosList) {
            if (u.autenticar(usuario, senha)) {
                System.out.println("Login bem-sucedido!");
                return u;
            }
        }
        System.out.println("Usuário ou senha inválidos!");
        return null;
    }
}
