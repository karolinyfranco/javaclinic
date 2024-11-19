package br.faesa.javaclinic.model;

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
    public String toString() {
        return "Paciente{" + super.toString() +
                "cpf='" + cpf + '\'' +
                '}';
    }
}
