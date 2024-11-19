package br.faesa.javaclinic.repository;

import br.faesa.javaclinic.model.Consulta;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepository {
    private static final String PATH_CONSULTAS = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "consultas.txt";

    public boolean medicoPossuiOutraConsulta(String nomeMedico, LocalDateTime data) {
        List<Consulta> consultas = carregarConsultas();
        return consultas.stream()
                .anyMatch(consulta -> consulta.getNomeMedico().equalsIgnoreCase(nomeMedico) && consulta.getData().equals(data));
    }

    public Consulta buscarConsultaPorId(Long idConsulta) {
        List<Consulta> consultas = carregarConsultas();
        return consultas.stream()
                .filter(consulta -> consulta.getId().equals(idConsulta))
                .findFirst()
                .orElse(null);
    }

    public static void agendarConsultas(List<Consulta> consultas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_CONSULTAS))) {
            for (Consulta c : consultas) {
                writer.write(c.getNomeMedico() + "-" + c.getNomePaciente() + "-" + c.getData() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Consulta> carregarConsultas() {
        List<Consulta> consultas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_CONSULTAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("-");
                Long id = Long.parseLong(dados[0].trim());
                String nomeMedico = dados[1].trim();
                String nomePaciente = dados[2].trim();
                LocalDateTime data = LocalDateTime.parse(dados[3].trim());

                Consulta consulta = new Consulta(id, nomeMedico, nomePaciente, data);
                consultas.add(consulta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return consultas;
    }
}
