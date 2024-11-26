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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static ConsultaRepository consultaRepository = new ConsultaRepository();
    private static List<Usuario> usuariosList = UsuarioRepository.carregarUsuarios();
    private static List<Medico> medicosList = MedicoRepository.carregarMedicos();
    private static List<Paciente> pacientesList = PacienteRepository.carregarPacientes();
    private static List<Consulta> consultasList = ConsultaRepository.carregarConsultas();
    private static Scanner scanner = new Scanner(System.in);
    private static ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())  // Desabilita a Expression Language
            .buildValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("""
                    \n--- Menu Inicial ---
                    1. Cadastrar Novo Usuário
                    2. Realizar Login
                    0. Sair
                    Escolha uma opção:\s""");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            switch (opcao) {
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    realizarLogin();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarUsuario() {
        System.out.println("\n--- Cadastro de Novo Usuário ---");
        System.out.print("Digite o nome de usuário (sem espaços): ");
        String usuario = scanner.nextLine();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();
        System.out.print("Digite o tipo de usuário (F para funcionário / P para paciente): ");
        char tipo = scanner.next().charAt(0);

        // Lista para guardar os usuários existentes
        List<Usuario> usuariosExistentes = UsuarioRepository.carregarUsuarios(); // Carrega usuários já cadastrados

        // Stream: recurso para processar coleções de dados, nesse caso uma List
        // Verifica se há um usuário duplicado na lista comparando o nome de usuário com a variável `usuario`
        boolean usuarioDuplicado = usuariosExistentes.stream().anyMatch(u -> u.getUsuario().equals(usuario));

        if (usuarioDuplicado) {
            System.out.println("Erro: O nome de usuário '" + usuario + "' já está em uso.");
            return; // Interrompe o fluxo se o usuário já existe, sem a necessidade de ter "else" e evitando aninhamento
        }

        // Cria a entidade usuário
        Usuario novoUsuario = new Usuario(usuario, senha, tipo);

        usuariosList.add(novoUsuario);  // Adiciona o novo usuário à lista
        // Salva a lista de usuários no arquivo
        UsuarioRepository.salvarUsuarios(usuariosList);

        System.out.println("Novo usuário cadastrado com sucesso!");
    }

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
            System.out.println("""
                    \n--- Menu do Funcionário ---
                    1. Cadastrar médico
                    2. Cadastrar paciente
                    3. Listar médicos
                    4. Listar pacientes
                    5. Listar consultas de um paciente
                    6. Atualizar dados de um médico
                    7. Atualizar dados de um paciente
                    8. Excluir médico
                    9. Excluir paciente
                    0. Sair
                    Escolha uma opção:\s""");
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
                    listarConsultas();
                    break;
                case 6:
                    atualizarMedico();
                    break;
                case 7:
                    atualizarPaciente();
                    break;
                case 8:
                    excluirMedico();
                    break;
                case 9:
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
            System.out.println("""
                    \n--- Menu do Paciente ---
                    1. Agendar consulta
                    2. Cancelar consulta
                    3. Listar consultas de um paciente
                    4. Listar médicos
                    0. Sair
                    Escolha uma opção:\s""");
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
                    listarConsultas();
                    break;
                case 4:
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
        String nome, email, endereco, telefone, crm;
        Especialidade especialidade;

        System.out.print("Nome do médico: ");
        nome = scanner.nextLine();
        System.out.print("Email do médico: ");
        email = scanner.nextLine();
        System.out.print("Endereço do médico (Rua-número-bairro-cidade-UF): ");
        endereco = scanner.nextLine();
        System.out.print("Telefone do médico: ");
        telefone = scanner.nextLine();
        System.out.print("CRM do médico: ");
        crm = scanner.nextLine();
        System.out.print("Especialidade do médico: ");
        try {
            // Lê a entrada do usuário e tenta converter para o tipo Especialidade usando o método fromString
            especialidade = Especialidade.fromString(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return; // Caso a entrada seja inválida, captura a exceção, exibe a mensagem de erro e encerra o fluxo
        }

        // Guarda os médicos já cadastrados na lista
        List<Medico> medicosExistentes = MedicoRepository.carregarMedicos();

        // Verifica se há um médico duplicado na lista comparando o CRM com a variável `crm`.
        boolean crmDuplicado = medicosExistentes.stream().anyMatch(m -> m.getCrm().equals(crm));

        if (crmDuplicado) {
            System.out.println("Erro: O CRM " + crm + " já está em uso.");
            return; // Interrompe o fluxo se o CRM já existe
        }

        Medico medico = new Medico(nome, email, endereco, telefone, crm, especialidade);

        // Realiza a validação do objeto médico com base nas anotações de Bean Validation.
        // Caso existam violações (erros), elas são armazenadas no conjunto 'violations'.
        Set<ConstraintViolation<Medico>> violations = validator.validate(medico);

        if (!violations.isEmpty()) {
            // Exibe todas as mensagens de erro encontradas no objeto 'medico'.
            System.out.println("Erros encontrados:");
            for (ConstraintViolation<Medico> violation : violations) {
                System.out.println("- " + violation.getMessage());
            }
            return; // Interrompe o fluxo para evitar o cadastro de um objeto inválido.
        }

        medicosList.add(medico);
        MedicoRepository.salvarMedicos(medicosList);
        System.out.println("Médico cadastrado com sucesso!");
    }

    private static void cadastrarPaciente() {
        String nome, email, endereco, telefone, cpf;

        System.out.print("Nome do paciente: ");
        nome = scanner.nextLine();
        System.out.print("Email do paciente: ");
        email = scanner.nextLine();
        System.out.print("Endereço do paciente (Rua-número-bairro-cidade-UF): ");
        endereco = scanner.nextLine();
        System.out.print("Telefone do paciente: ");
        telefone = scanner.nextLine();
        System.out.print("CPF do paciente: ");
        cpf = scanner.nextLine();

        // Verifica se o CPF já está em uso
        if (PacienteRepository.buscarPacientePorCpf(cpf) != null) {
            System.out.println("Erro: O CPF " + cpf + " já está em uso.");
            return;
        }

        Paciente paciente = new Paciente(nome, email, endereco, telefone, cpf);

        // Valida a entidade paciente
        Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);

        if (!violations.isEmpty()) {
            System.out.println("Erros encontrados:");
            for (ConstraintViolation<Paciente> violation : violations) {
                System.out.println("- " + violation.getMessage());
            }
            return;
        }

        pacientesList.add(paciente);
        PacienteRepository.salvarPacientes(pacientesList);
        System.out.println("Paciente cadastrado com sucesso!");
    }

    private static void listarMedicos() {
        // Chama o método carregarMedicos para obter a lista de médicos
        MedicoRepository.carregarMedicos();

        // Se a lista de médicos não estiver vazia, lista as informações de todos os médicos
        if (medicosList.isEmpty()) {
            System.out.println("Nenhum médico encontrado.");
        } else {
            System.out.println("Lista de Médicos:");

            // For-Each loop
            /*Itera diretamente sobre elementos de uma coleção, array, etc.,
            sem a necessidade de usar índices ou iteradores explicitamente.*/
            for (Medico medico : medicosList) {
                System.out.println(medico.toString());
            }
        }
    }

    private static void listarPacientes() {
        // Chama o método carregarPacientes para obter a lista de pacientes
        PacienteRepository.carregarPacientes();

        // Se a lista de pacientes não estiver vazia, lista as informações de todos os pacientes
        if (pacientesList.isEmpty()){
            System.out.println("Nenhum paciente encontrado.");
        } else {
            System.out.println("Lista de Pacientes:");
            for (Paciente paciente : pacientesList) {
                System.out.println(paciente.toString());
            }
        }
    }

    private static void listarConsultas() {
        // Solicitar o nome do paciente para filtrar as consultas
        System.out.print("Digite o nome do paciente: ");
        String nomePaciente = scanner.nextLine();

        // Listar todas as consultas do paciente
        List<Consulta> consultasDoPaciente = consultaRepository.listarConsultasDoPaciente(nomePaciente);

        if (consultasDoPaciente.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada para o paciente " + nomePaciente + ".");
        } else {
            System.out.println("Consultas do paciente " + nomePaciente + ":");
            for (Consulta consulta : consultasDoPaciente) {
                System.out.println(consulta.toString());
            }
        }
    }

    private static void atualizarMedico() {
        System.out.print("Digite o CRM do médico que deseja atualizar: ");
        String crm = scanner.nextLine().trim();

        // Verifica se o médico existe
        Medico medico = MedicoRepository.buscarMedicoPorCrm(crm);
        if (medico == null) {
            System.out.println("Médico com CRM " + crm + " não encontrado.");
            return;
        }

        // Exibe os dados atuais do médico
        System.out.println("Dados atuais do médico:" +
                "\nNome: " + medico.getNome() +
                "\nTelefone: " + medico.getTelefone() +
                "\nEndereço: " + medico.getEndereco());

        // Solicita os novos dados ao usuário
        System.out.println("Digite os novos dados (pressione Enter para manter os atuais):");
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Novo endereço: ");
        String endereco = scanner.nextLine();

        // Chama o método do repositório para atualizar o médico
        MedicoRepository.atualizarMedico(crm, nome, telefone, endereco);
        System.out.println("Dados do médico atualizados com sucesso.");
    }

    private static void atualizarPaciente(){
        System.out.print("Digite o CPF do paciente que deseja atualizar: ");
        String cpf = scanner.nextLine().trim();

        // Verifica se o médico existe
        Paciente paciente = PacienteRepository.buscarPacientePorCpf(cpf);
        if (paciente == null) {
            System.out.println("Paciente com CPF " + cpf + " não encontrado.");
            return;
        }

        // Exibe os dados atuais do médico
        System.out.println("Dados atuais do paciente:" +
                "\nNome: " + paciente.getNome() +
                "\nTelefone: " + paciente.getTelefone() +
                "\nEndereço: " + paciente.getEndereco());

        // Solicita os novos dados ao usuário
        System.out.println("Digite os novos dados (pressione Enter para manter os atuais):");
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Novo endereço: ");
        String endereco = scanner.nextLine();

        // Chama o método do repositório para atualizar o médico
        PacienteRepository.atualizarPaciente(cpf, nome, telefone, endereco);
        System.out.println("Dados do paciente atualizados com sucesso.");
    }

    private static void excluirMedico() {
        System.out.print("Digite o CRM do médico que deseja excluir: ");
        String crm = scanner.nextLine().trim();

        // Chama o método do repositório para excluir o médico
        MedicoRepository.excluirMedico(crm);
        System.out.println("Médico com CRM " + crm + " excluído com sucesso.");
    }

    private static void excluirPaciente(){
        System.out.print("Digite o CPF do paciente que deseja excluir: ");
        String cpf = scanner.nextLine().trim();

        // Chama o método do repositório para excluir o paciente
        PacienteRepository.excluirPaciente(cpf);
        System.out.println("Paciente com CPF " + cpf + " excluído com sucesso.");
    }

    private static void agendarConsulta() {
        String nomeMedico, nomePaciente;
        Especialidade especialidade;
        LocalDateTime data;

        System.out.print("Informe o nome do paciente: ");
        nomePaciente = scanner.nextLine();

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

            System.out.print("Informe a data e hora da consulta (yyyy-MM-ddTHH:mm): ");
            data = LocalDateTime.parse(scanner.nextLine());
        } else {

            // Exibe as especialidades disponíveis para o usuário escolher
            System.out.print("Informe a especialidade desejada: ");
            for (Especialidade e : Especialidade.values()) {
                System.out.println(" - " + e.name());
            }
            try {
                especialidade = Especialidade.fromString(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }

            System.out.print("Informe a data e hora da consulta (yyyy-MM-ddTHH:mm): ");
            data = LocalDateTime.parse(scanner.nextLine());

            // Busca médicos disponíveis para a especialidade na data e hora informadas pelo paciente
            List<String> medicosDisponiveis = ConsultaRepository.buscarMedicosPorEspecialidade(especialidade, data);

            if (medicosDisponiveis.isEmpty()) {
                System.out.println("Não há médicos disponíveis para essa especialidade na data e hora informadas.");
                return; // Interrompe o fluxo caso não haja médicos disponíveis
            }

            // Seleciona aleatoriamente o nome de um médico disponível na lista 'medicosDisponiveis'
            /* O índice do médico é determinado por um número aleatório gerado entre 0 (inclusivo)
            e o tamanho da lista (exclusivo), usando a classe Random.*/
            nomeMedico = medicosDisponiveis.get(new Random().nextInt(medicosDisponiveis.size()));
            System.out.println("Médico selecionado automaticamente: " + nomeMedico);
        }

        // Validações de horário, antecedência de agendamento e disponibilidade do médico
        try {
            ValidadorHorario validadorHorario = new ValidadorHorario(consultaRepository);
            ValidadorMedico validadorMedico = new ValidadorMedico(consultaRepository);

            validadorHorario.validarHorarioFuncionamentoClinica(data);
            validadorHorario.validarHorarioAntecedenciaAgendamento(data);
            validadorMedico.validarAgendamento(nomeMedico, data);
        } catch (ValidacaoException e) {
            System.out.println("Erro ao agendar consulta: " + e.getMessage());
            return; // Interrompe o agendamento se houver erro
        }

        // Cria a entidade consulta
        Consulta consulta = new Consulta(null, nomeMedico, nomePaciente, especialidade, data);

        // Valida a entidade consulta
        Set<ConstraintViolation<Consulta>> violations = validator.validate(consulta);

        if (!violations.isEmpty()) {
            System.out.println("Erros encontrados:");
            for (ConstraintViolation<Consulta> violation : violations) {
                System.out.println("- " + violation.getMessage());
            }
            return; // Interrompe o fluxo se houver erros
        }

        // Carrega as consultas existentes
        consultasList.add(consulta); // Adiciona a nova consulta
        ConsultaRepository.agendarConsultas(consultasList); // Salva todas as consultas no repositório

        System.out.println("Consulta agendada com sucesso!");
    }

    private static void cancelarConsulta() {
        Long id;

        // Método para listar as consultas de um determinado paciente
        listarConsultas();

        // Capturar o ID da consulta para cancelar
        System.out.print("Digite o ID da consulta para cancelar: ");
        id = scanner.nextLong();
        scanner.nextLine(); // Limpa o buffer do scanner

        // Validação de antecedência do cancelamento
        try {
            ValidadorHorario validadorHorario = new ValidadorHorario(consultaRepository);
            validadorHorario.validarHorarioAntecedenciaCancelamento(id);
        } catch (ValidacaoException e) {
            System.out.println("Erro ao cancelar consulta: " + e.getMessage());
            return; // Interrompe o cancelamento se houver erro
        }

        // Chama o método para cancelar a consulta
        ConsultaRepository.cancelarConsulta(id);
        System.out.println("Consulta com ID " + id + " foi cancelada com sucesso.");
    }
}