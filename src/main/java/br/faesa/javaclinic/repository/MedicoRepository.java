package br.faesa.javaclinic.repository;

import br.faesa.javaclinic.model.Especialidade;
import br.faesa.javaclinic.model.Medico;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MedicoRepository {
    private static final String PATH_MEDICOS = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "medicos.txt";

    public static void salvarMedicos(List<Medico> medicos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_MEDICOS))) {
            for (Medico m : medicos) {
                writer.write(m.getNome() + "-" + m.getEmail() + "-" + m.getEndereco() + "-" + m.getTelefone() + "-" + m.getCrm() + "-" + m.getEspecialidade() +"\n");
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

    /*public static void atualizarMedico(String crm, Medico medicoAtualizado) {
        List<Medico> medicos = carregarMedicos();
        boolean encontrado = false;

        for (int i = 0; i < medicos.size(); i++) {
            Medico medico = medicos.get(i);
            if (medico.getCrm().equalsIgnoreCase(crm)) {
                medicos.set(i, medicoAtualizado); // Substitui o médico existente pelo atualizado
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Médico com CRM " + crm + " não encontrado.");
            return;
        }

        salvarMedicos(medicos);
        System.out.println("Médico atualizado com sucesso!");
    }*/
    /* Uso:
    Medico medicoAtualizado = new Medico("Novo Nome", "novoemail@dominio.com", "Novo Endereço", "12345678", "CRM1234", Especialidade.PEDIATRIA);
    MedicoRepository.atualizarMedico("CRM1234", medicoAtualizado);*/


    /*public static void excluirMedico(String crm) {
        List<Medico> medicos = carregarMedicos();
        boolean encontrado = false;

        Iterator<Medico> iterator = medicos.iterator();
        while (iterator.hasNext()) {
            Medico medico = iterator.next();
            if (medico.getCrm().equalsIgnoreCase(crm)) {
                iterator.remove(); // Remove o médico da lista
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Médico com CRM " + crm + " não encontrado.");
            return;
        }

        salvarMedicos(medicos);
        System.out.println("Médico excluído com sucesso!");
    }*/
    /*Uso:
    MedicoRepository.excluirMedico("CRM1234");*/

}
