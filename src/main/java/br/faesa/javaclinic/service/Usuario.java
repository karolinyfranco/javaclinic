package br.faesa.javaclinic.service;

import java.util.Objects;

public class Usuario {
    private String usuario;
    private String senha;
    private char tipo; // "funcionario" ou "paciente"

    public Usuario(String usuario, String senha, char tipo) {
        this.usuario = usuario;
        this.senha = senha;
        this.tipo = tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public boolean autenticar(String usuario, String senha) {
        return this.usuario.equals(usuario) && this.senha.equals(senha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario1 = (Usuario) o;
        return Objects.equals(usuario, usuario1.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(usuario);
    }

    /*Operador ternário:
    condição ? valor_se_verdadeiro : valor_se_falso;*/
    @Override
    public String toString() {
        return "Usuario: " + usuario + " | Tipo: " + (tipo == 'F' ? "Funcionário" : "Paciente");
    }
}
