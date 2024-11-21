package br.faesa.javaclinic.repository;

import br.faesa.javaclinic.model.Especialidade;
import br.faesa.javaclinic.model.Medico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoRepository {
    private static final String PATH_MEDICOS = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "medicos.txt";

    public static Medico buscarMedicoPorCrm(String crm) {
        List<Medico> medicos = carregarMedicos();
        for (Medico medico : medicos) {
            if (medico.getCrm().equals(crm)) {
                return medico;
            }
        }
        return null; // Retorna null caso não encontre
    }

    public static Especialidade getEspecialidadeDoMedico(String nomeMedico) {
        List<Medico> medicos = carregarMedicos(); // Carrega todos os médicos
        for (Medico medico : medicos) {
            if (medico.getNome().equalsIgnoreCase(nomeMedico)) {
                return medico.getEspecialidade(); // Retorna a especialidade do médico encontrado
            }
        }
        return null; // Retorna null se o médico não for encontrado
    }

    public static void salvarMedicos(List<Medico> medicos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_MEDICOS))) {
            for (Medico m : medicos) {
                writer.write(m.getNome() + ";" + m.getEmail() + ";" + m.getEndereco() + ";" + m.getTelefone() + ";" + m.getCrm() + ";" + m.getEspecialidade() +"\n");
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
                String[] dados = linha.split(";\\s*"); // Usa uma expressão regular para ignorar espaços ao redor da vírgula
                if (dados.length == 6) { // Verifica se há exatamente 6 partes (ajustar de acordo com a quantidade de atributos)
                    String nome = dados[0].trim();
                    String email = dados[1].trim();
                    String crm = dados[2].trim();
                    String endereco = dados[3].trim();
                    String telefone = dados[4].trim();
                    Especialidade especialidade = Especialidade.valueOf(dados[5].trim().toUpperCase()); // Converte dados[5] para o tipo enum Especialidade

                    // Adiciona um novo objeto Medico com os valores convertidos
                    medicos.add(new Medico(nome, email, endereco, telefone, crm, especialidade));
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return medicos;
    }

    public static void atualizarMedico(String crm, String nome, String telefone, String endereco) {
        List<Medico> medicos = carregarMedicos();
        Medico medico = buscarMedicoPorCrm(crm);
        if (medico != null) {
            // Atualiza as informações do médico
            medico.atualizar(nome, telefone, endereco);
            salvarMedicos(medicos); // Método para salvar a lista de médicos novamente no arquivo
        }
    }
    /* Uso:
    MedicoRepository.atualizarMedico("CRM1234", "Novo Nome", "27992978669", "Novo Endereço");*/

    // Método para excluir um médico
    public void excluirMedico(String crm) {
        List<Medico> medicos = carregarMedicos();
        Medico medico = buscarMedicoPorCrm(crm);
        if (medico != null) {
            medicos.remove(medico);
            salvarMedicos(medicos); // Salva os médicos restantes no arquivo
        }
    }
    /*Uso:
    MedicoRepository.excluirMedico("CRM1234");*/

}
