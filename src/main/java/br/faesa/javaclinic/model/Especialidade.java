package br.faesa.javaclinic.model;

public enum Especialidade {
    ORTOPEDIA,
    PEDIATRIA,
    CARDIOLOGIA,
    GINECOLOGIA,
    DERMATOLOGIA,
    PSIQUIATRIA,
    OFTALMOLOGIA;

    public static Especialidade fromString(String value) {
        try {
            return Especialidade.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Especialidade inválida.");
        }
    }
}
