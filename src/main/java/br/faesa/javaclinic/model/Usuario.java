package br.faesa.javaclinic.model;

public class Usuario {
    private String usuario;
    private String senha;
    private String tipo; // "funcionario" ou "paciente"

    public Usuario(String usuario, String senha, String tipo) {
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean autenticar(String usuario, String senha) {
        return this.usuario.equals(usuario) && this.senha.equals(senha);
    }

    @Override
    public String toString() {
        return "Usuario: " + usuario + " | Tipo: " + tipo;
    }

    // Método equals (opcional, para comparar dois objetos de Usuario)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario1 = (Usuario) obj;
        return usuario.equals(usuario1.usuario);
    }

    // Método hashCode (opcional, geralmente implementado junto com equals)
    @Override
    public int hashCode() {
        return usuario.hashCode();
    }
}
