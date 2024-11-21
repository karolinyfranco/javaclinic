package br.faesa.javaclinic.model;

import java.util.Objects;

public class Paciente extends Pessoa{
    private String cpf;

    public Paciente(String nome, String email, String endereco, String telefone, String cpf) {
        super(nome, email, endereco, telefone);
        this.cpf = cpf;
    }

    public Paciente() {
        this.cpf = "";
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(cpf, paciente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cpf);
    }

    @Override
    public String toString() {
        return "Paciente{" + super.toString() +
                "cpf='" + cpf + '\'' +
                '}';
    }
}
