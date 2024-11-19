package br.faesa.javaclinic.service.validator;

import br.faesa.javaclinic.repository.ConsultaRepository;

import java.time.LocalDateTime;

public class ValidadorMedico {
    private ConsultaRepository consultaRepository;

    public ValidadorMedico(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public void validarAgendamento(String nomeMedico, LocalDateTime data) {
        if (consultaRepository.medicoPossuiOutraConsulta(nomeMedico, data)) {
            throw new ValidacaoException("Médico já possui outra consulta nesse horário!");
        }
    }
}
