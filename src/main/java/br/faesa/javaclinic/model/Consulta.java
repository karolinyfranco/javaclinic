package br.faesa.javaclinic.model;

import java.time.LocalDateTime;

public class Consulta {
    private Long id;
    private String nomeMedico;
    private String nomePaciente;
    private LocalDateTime data;

    public Consulta(Long id, String nomeMedico, String nomePaciente, LocalDateTime data) {
        this.id = id;
        this.nomeMedico = nomeMedico;
        this.nomePaciente = nomePaciente;
        this.data = data;
    }

    public Consulta() {
        this.id = null;
        this.nomeMedico = null;
        this.nomePaciente = null;
        this.data = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Consulta{" +
                "nomeMedico='" + nomeMedico + '\'' +
                ", nomePaciente='" + nomePaciente + '\'' +
                ", data=" + data +
                '}';
    }
}
