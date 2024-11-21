package br.faesa.javaclinic;

import br.faesa.javaclinic.model.*;
import br.faesa.javaclinic.repository.ConsultaRepository;
import br.faesa.javaclinic.repository.MedicoRepository;
import br.faesa.javaclinic.repository.PacienteRepository;
import br.faesa.javaclinic.repository.UsuarioRepository;
import br.faesa.javaclinic.service.LoginService;
import br.faesa.javaclinic.service.Usuario;
import br.faesa.javaclinic.service.validator.ValidacaoException;
import br.faesa.javaclinic.service.validator.ValidadorHorario;
import br.faesa.javaclinic.service.validator.ValidadorMedico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static List<Usuario> usuarios = UsuarioRepository.carregarUsuarios();
    static List<Medico> medicos = MedicoRepository.carregarMedicos();
    static List<Paciente> pacientes = PacienteRepository.carregarPacientes();
    static List<Consulta> consultas = ConsultaRepository.carregarConsultas();
    static ConsultaRepository consultaRepository = new ConsultaRepository();
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
        System.out.print("Digite o tipo de usuário (F para funcionário / P para paciente): ");
        char tipo = scanner.next().charAt(0);

        // Criando o novo usuário
        Usuario novoUsuario = new Usuario(usuario, senha, tipo);

        usuarios.add(novoUsuario);  // Adiciona o novo usuário à lista
        // Salvar a lista de usuários no arquivo
        UsuarioRepository.salvarUsuarios(usuarios);

        System.out.println("Novo usuário cadastrado com sucesso!");
    }

    // Método de login
    private static void realizarLogin() {
        Usuario usuario = LoginService.fazerLogin();
        if (usuario == null) {
            return;  // Sai do programa caso o login falhe
        }

        // Menu de opções após o login
        if (usuario.getTipo() == 'F') {
            menuFuncionario();
        } else if (usuario.getTipo() == 'P') {
            menuPaciente();
        }
    }

    private static void menuFuncionario() {
        int opcao;
        do {
            System.out.println("\n--- Menu do Funcionário ---");
            System.out.println("1. Cadastrar médico");
            System.out.println("2. Cadastrar paciente");
            System.out.println("3. Listar médicos");
            System.out.println("4. Listar pacientes");
            System.out.println("5. Atualizar dados de um médico");
            System.out.println("6. Atualizar dados de um médico");
            System.out.println("7. Excluir médico");
            System.out.println("8. Excluir paciente");
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
                case 5:
                    atualizarMedico();
                    break;
                case 6:
                    atualizarPaciente();
                    break;
                case 7:
                    excluirMedico();
                    break;
                case 8:
                    excluirPaciente();
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
            System.out.println("1. Agendar consulta");
            System.out.println("2. Cancelar consulta");
            System.out.println("3. Listar médicos");
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

    }

    private static void listarPacientes() {

    }

    private static void atualizarMedico(){

    }

    private static void atualizarPaciente(){

    }

    private static void excluirMedico(){

    }

    private static void excluirPaciente(){

    }

    private static void agendarConsulta() {
        String nomeMedico;
        String nomePaciente;
        Especialidade especialidade;
        LocalDateTime data;

        // Informar o nome do paciente
        System.out.print("Informe o nome do paciente: ");
        nomePaciente = scanner.nextLine();

        // Perguntar se deseja escolher o médico
        System.out.print("Deseja escolher um médico? (S/N): ");
        char escolha = scanner.next().charAt(0);
        scanner.nextLine();

        if (escolha == 'S') {
            System.out.print("Informe o nome do médico desejado: ");
            nomeMedico = scanner.nextLine();

            // Buscar a especialidade do médico escolhido
            especialidade = MedicoRepository.getEspecialidadeDoMedico(nomeMedico);
            if (especialidade == null) {
                System.out.println("Médico não encontrado ou especialidade não associada.");
                return;
            }

            // Informar a data da consulta
            System.out.print("Informe a data e hora da consulta (yyyy-MM-ddTHH:mm): ");
            data = LocalDateTime.parse(scanner.nextLine());
        } else {
            System.out.print("Informe a especialidade desejada: ");
            for (Especialidade e : Especialidade.values()) {
                System.out.println(e.ordinal() + " - " + e.name());
            }
            try {
                especialidade = Especialidade.fromString(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }

            // Buscar médicos disponíveis
            System.out.print("Informe a data e hora da consulta (yyyy-MM-ddTHH:mm): ");
            data = LocalDateTime.parse(scanner.nextLine());

            List<String> medicosDisponiveis = ConsultaRepository.buscarMedicosPorEspecialidade(especialidade, data);

            if (medicosDisponiveis.isEmpty()) {
                System.out.println("Não há médicos disponíveis para essa especialidade na data e hora informadas.");
                return;
            }

            // Seleção aleatória de um médico
            nomeMedico = medicosDisponiveis.get(new Random().nextInt(medicosDisponiveis.size()));
            System.out.println("Médico selecionado automaticamente: " + nomeMedico);
        }

        // Validações
        try {
            // Instâncias dos validadores
            ValidadorHorario validadorHorario = new ValidadorHorario(consultaRepository);
            ValidadorMedico validadorMedico = new ValidadorMedico(consultaRepository);

            validadorHorario.validarHorarioFuncionamentoClinica(data);
            validadorHorario.validarHorarioAntecedenciaAgendamento(data);
            validadorMedico.validarAgendamento(nomeMedico, data);
        } catch (ValidacaoException e) {
            System.out.println("Erro ao agendar consulta: " + e.getMessage());
            return; // Interrompe o agendamento se houver erro
        }

        // Criar a consulta e salvar
        Consulta consulta = new Consulta(null, nomeMedico, nomePaciente, especialidade, data);

        // Carrega as consultas existentes
        consultas.add(consulta); // Adiciona a nova consulta
        ConsultaRepository.agendarConsultas(consultas); // Salva todas as consultas no repositório

        System.out.println("Consulta agendada com sucesso!");
    }

    private static void cancelarConsulta() {
        String nomePaciente;
        Long id;

        // Solicitar o nome do paciente para filtrar as consultas
        System.out.print("Digite o nome do paciente: ");
        nomePaciente = scanner.nextLine();

        // Listar todas as consultas do paciente
        List<Consulta> consultasDoPaciente = consultaRepository.listarConsultasDoPaciente(nomePaciente);

        if (consultasDoPaciente.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada para o paciente " + nomePaciente + ".");
            return;
        }

        System.out.println("Consultas do paciente " + nomePaciente + ":");
        for (Consulta consulta : consultasDoPaciente) {
            System.out.println("ID: " + consulta.getId() + ", Medico: " + consulta.getNomeMedico() +
                    ", Especialidade: " + consulta.getEspecialidade() + ", Data: " + consulta.getData());
        }

        // Capturar o ID da consulta para cancelar
        System.out.print("Digite o ID da consulta para cancelar: ");
        id = scanner.nextLong();
        scanner.nextLine(); // Limpa o buffer do scanner

        // Validação de antecedência e cancelamento
        try {
            ValidadorHorario validadorHorario = new ValidadorHorario(consultaRepository);
            validadorHorario.validarHorarioAntecedenciaCancelamento(id);
        } catch (ValidacaoException e) {
            System.out.println("Erro ao cancelar consulta: " + e.getMessage());
            return; // Interrompe o cancelamento se houver erro
        }

        // Chama o método para cancelar a consulta
        consultaRepository.cancelarConsulta(id);
        System.out.println("Consulta com ID " + id + " foi cancelada com sucesso.");
    }
}