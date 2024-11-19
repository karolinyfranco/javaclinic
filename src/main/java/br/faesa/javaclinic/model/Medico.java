package br.faesa.javaclinic.model;

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
    public String toString() {
        return "Medico{" + super.toString() +
                "crm='" + crm + '\'' +
                ", especialidade=" + especialidade +
                '}';
    }
}
