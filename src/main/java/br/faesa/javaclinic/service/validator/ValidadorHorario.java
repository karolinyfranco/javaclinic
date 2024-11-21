package br.faesa.javaclinic.service.validator;

import br.faesa.javaclinic.model.Consulta;
import br.faesa.javaclinic.repository.ConsultaRepository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

public class ValidadorHorario {
    private final ConsultaRepository consultaRepository;

    public ValidadorHorario(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public void validarHorarioFuncionamentoClinica(LocalDateTime data) {
        boolean domingo = data.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        boolean antesDaAbertura = data.getHour() < 7;
        boolean depoisDoEncerramento = data.getHour() > 18;

        if (domingo || antesDaAbertura || depoisDoEncerramento) {
            throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica");
        }
    }

    public void validarHorarioAntecedenciaAgendamento(LocalDateTime data) {
        long diferencaEmMinutos = Duration.between(LocalDateTime.now(), data).toMinutes();

        if (diferencaEmMinutos < 30) {
            throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos.");
        }
    }

    public void validarHorarioAntecedenciaCancelamento(Long id) {
        // Busca a consulta pelo ID
        Consulta consulta = consultaRepository.buscarConsultaPorId(id);
        if (consulta == null) {
            throw new ValidacaoException("Consulta não encontrada.");
        }

        LocalDateTime agora = LocalDateTime.now();
        long diferencaEmHoras = Duration.between(agora, consulta.getData()).toHours();

        if (diferencaEmHoras < 24) {
            throw new ValidacaoException("Consulta somente pode ser cancelada com antecedência mínima de 24h!");
        }
    }
}
