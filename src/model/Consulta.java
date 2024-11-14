package model;

import java.time.LocalDateTime;

public class Consulta {
    private Medico medico;
    private Paciente paciente;
    private LocalDateTime data;

    public Consulta(Medico medico, Paciente paciente, LocalDateTime data) {
        this.medico = medico;
        this.paciente = paciente;
        this.data = data;
    }

    public Consulta() {
        this.medico = null;
        this.paciente = null;
        this.data = null;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
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
                "medico=" + medico +
                ", paciente=" + paciente +
                ", data=" + data +
                '}';
    }
}
