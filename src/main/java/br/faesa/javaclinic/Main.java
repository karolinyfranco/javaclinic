package br.faesa.javaclinic;

import br.faesa.javaclinic.model.Especialidade;
import br.faesa.javaclinic.model.Medico;
import br.faesa.javaclinic.model.Paciente;
import br.faesa.javaclinic.model.Usuario;
import br.faesa.javaclinic.repository.MedicoRepository;
import br.faesa.javaclinic.repository.PacienteRepository;
import br.faesa.javaclinic.repository.UsuarioRepository;
import br.faesa.javaclinic.service.LoginService;

import java.util.List;
import java.util.Scanner;

public class Main {
    static List<Usuario> usuarios = UsuarioRepository.carregarUsuarios();
    static List<Medico> medicos = MedicoRepository.carregarMedicos();
    static List<Paciente> pacientes = PacienteRepository.carregarPacientes();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n--- Menu Inicial ---");
            System.out.println("1. Cadastrar Novo Usuário");
            System.out.println("2. Realizar Login");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            switch (opcao) {
                case 1:
                    cadastrarUsuario(); // Chamando método para cadastrar usuário
                    break;
                case 2:
                    realizarLogin(); // Chamando método de login
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    // Método de cadastro de novo usuário
    private static void cadastrarUsuario() {
        System.out.println("\n--- Cadastro de Novo Usuário ---");
        System.out.print("Digite o nome de usuário: ");
        String usuario = scanner.nextLine();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();
        System.out.print("Digite o tipo de usuário (funcionario/paciente): ");
        String tipo = scanner.nextLine();

        // Criando o novo usuário
        Usuario novoUsuario = new Usuario(usuario, senha, tipo);

        usuarios.add(novoUsuario);  // Adiciona o novo usuário à lista
        // Salvar a lista de usuários no arquivo
        UsuarioRepository.salvarUsuarios(usuarios);

        System.out.println("Novo usuário cadastrado com sucesso!");
    }

    // Método de login
    private static void realizarLogin() {
        Usuario usuario = LoginService.realizarLogin();
        if (usuario == null) {
            return;  // Sai do programa caso o login falhe
        }

        // Menu de opções após o login
        if (usuario.getTipo().equals("funcionario")) {
            menuFuncionario();
        } else if (usuario.getTipo().equals("paciente")) {
            menuPaciente();
        }
    }

    private static void menuFuncionario() {
        int opcao;
        do {
            System.out.println("\n--- Menu do Funcionário ---");
            System.out.println("1. Cadastrar Médico");
            System.out.println("2. Cadastrar Paciente");
            System.out.println("3. Listar Médicos");
            System.out.println("4. Listar Pacientes");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            switch (opcao) {
                case 1:
                    cadastrarMedico();
                    break;
                case 2:
                    cadastrarPaciente();
                    break;
                case 3:
                    listarMedicos();
                    break;
                case 4:
                    listarPacientes();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void menuPaciente() {
        int opcao;
        do {
            System.out.println("\n--- Menu do Paciente ---");
            System.out.println("1. Agendar Consulta");
            System.out.println("2. Cancelar Consulta");
            System.out.println("3. Listar Médicos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            switch (opcao) {
                case 1:
                    agendarConsulta();
                    break;
                case 2:
                    cancelarConsulta();
                    break;
                case 3:
                    listarMedicos();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarMedico() {
        System.out.print("Nome do médico: ");
        String nome = scanner.nextLine();
        System.out.print("Email do médico: ");
        String email = scanner.nextLine();
        System.out.print("Endereço do médico (Rua, número, bairro, cidade): ");
        String endereco = scanner.nextLine();
        System.out.print("Telefone do médico: ");
        String telefone = scanner.nextLine();
        System.out.print("CRM do médico: ");
        String crm = scanner.nextLine();
        System.out.print("Especialidade do médico: ");
        Especialidade especialidade = Especialidade.valueOf(scanner.nextLine());

        Medico medico = new Medico(nome, email, endereco, telefone, crm, especialidade);

        medicos.add(medico);
        MedicoRepository.salvarMedicos(medicos);
        System.out.println("Médico cadastrado com sucesso!");
    }

    private static void cadastrarPaciente() {
        System.out.print("Nome do paciente: ");
        String nome = scanner.nextLine();
        System.out.print("Email do paciente: ");
        String email = scanner.nextLine();
        System.out.print("Endereço do paciente (Rua, número, bairro, cidade): ");
        String endereco = scanner.nextLine();
        System.out.print("Telefone do paciente: ");
        String telefone = scanner.nextLine();
        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        Paciente paciente = new Paciente(nome, email, endereco, telefone, cpf);

        pacientes.add(paciente);
        PacienteRepository.salvarPacientes(pacientes);
        System.out.println("Paciente cadastrado com sucesso!");
    }

    private static void listarMedicos() {
        if (medicos.isEmpty()) {
            System.out.println("Nenhum médico cadastrado.");
        } else {
            System.out.println("\n--- Lista de Médicos ---");
            for (Medico medico : medicos) {
                System.out.println(medicos);
            }
        }
    }

    private static void listarPacientes() {
        if (pacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
        } else {
            System.out.println("\n--- Lista de Pacientes ---");
            for (Paciente paciente : pacientes) {
                System.out.println(paciente);
            }
        }
    }

    private static void agendarConsulta() {

    }

    private static void cancelarConsulta() {

    }
}