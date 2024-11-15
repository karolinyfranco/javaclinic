package service;

import model.Usuario;
import persistence.Persistencia;

import java.util.List;
import java.util.Scanner;

public class SistemaLogin {
    private static List<Usuario> usuarios = Persistencia.carregarUsuarios();  // Método para carregar dados dos usuários
    private static Scanner scanner = new Scanner(System.in);

    public static Usuario realizarLogin() {
        System.out.print("Digite seu nome de usuário: ");
        String usuario = scanner.nextLine();
        System.out.print("Digite sua senha: ");
        String senha = scanner.nextLine();

        for (Usuario u : usuarios) {
            if (u.autenticar(usuario, senha)) {
                System.out.println("Login bem-sucedido!");
                return u;
            }
        }

        System.out.println("Usuário ou senha inválidos!");
        return null;
    }
}

