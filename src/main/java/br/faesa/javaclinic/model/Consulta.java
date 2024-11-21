package br.faesa.javaclinic.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Consulta {
    private Long id;
    private String nomeMedico;
    private String nomePaciente;
    private Especialidade especialidade;
    private LocalDateTime data;

    public Consulta(Long id, String nomeMedico, String nomePaciente, Especialidade especialidade, LocalDateTime data) {
        this.id = id;
        this.nomeMedico = nomeMedico;
        this.nomePaciente = nomePaciente;
        this.especialidade = especialidade;
        this.data = data;
    }

    public Consulta() {
        this.id = null;
        this.nomeMedico = null;
        this.nomePaciente = null;
        this.especialidade = null;
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

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consulta consulta = (Consulta) o;
        return Objects.equals(id, consulta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Informações da consulta: " +
                "\n Nome do médico: " + nomeMedico + '\'' +
                "\n Nome do paciente: " + nomePaciente + '\'' +
                "\n Especialidade: " + especialidade.name() + '\'' +
                "\n Data da consulta: " + data;
    }
}
