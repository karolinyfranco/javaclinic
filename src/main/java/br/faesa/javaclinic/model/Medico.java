package br.faesa.javaclinic.model;

import java.util.Objects;

public class Medico extends Pessoa{
    private String crm;
    private Especialidade especialidade;

    public Medico(String nome, String email, String endereco, String telefone, String crm, Especialidade especialidade) {
        super(nome, email, endereco, telefone);
        this.crm = crm;
        this.especialidade = especialidade;
    }

    public Medico() {
        this.crm = "";
        this.especialidade = null;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Medico medico = (Medico) o;
        return Objects.equals(crm, medico.crm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), crm);
    }

    @Override
    public String toString() {
        return "Medico{" + super.toString() +
                "crm='" + crm + '\'' +
                ", especialidade=" + especialidade +
                '}';
    }
}
