package dam.moscosos.api.service;

import dam.moscosos.api.domain.BloqueoCalendario;
import dam.moscosos.api.enums.Tipo;
import dam.moscosos.api.repository.BloqueoCalendarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class BloqueoCalendarioServiceImpl implements BloqueoCalendarioService {
    @Autowired
    BloqueoCalendarioRepository bloqueoCalendarioRepository;

    @Override
    public List<BloqueoCalendario> findAll() {
        return bloqueoCalendarioRepository.findAll();
    }

    @Override
    public List<BloqueoCalendario> buscarPorDia(LocalDate dia) {
        return bloqueoCalendarioRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(dia, dia);
    }

    @Override
    public List<BloqueoCalendario> buscarPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return bloqueoCalendarioRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(fechaFin, fechaInicio);
    }

    @Override
    public BloqueoCalendario bloquearCalendario(BloqueoCalendario calendario) {
        return bloqueoCalendarioRepository.save(calendario);
    }
    @Override
    public List<BloqueoCalendario> findByTipo(Tipo tipo) {
        return bloqueoCalendarioRepository.findByTipo(tipo);
    }

    @Override
    @Transactional
    public void desbloquearDias(LocalDate desde, LocalDate hasta) {
        if (hasta.isBefore(desde)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'hasta' no puede ser anterior a 'desde'");
        }

        List<BloqueoCalendario> afectados =
                bloqueoCalendarioRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(hasta, desde);

        for (BloqueoCalendario b : afectados) {
            boolean empiezaAntes = b.getFechaInicio().isBefore(desde);
            boolean terminaDespues = b.getFechaFin().isAfter(hasta);

            if (empiezaAntes && terminaDespues) {
                // El rango queda en medio: se parte en dos
                BloqueoCalendario resto = new BloqueoCalendario(
                        null, hasta.plusDays(1), b.getFechaFin(), b.getTipo(), b.getComentario());
                b.setFechaFin(desde.minusDays(1));
                bloqueoCalendarioRepository.save(b);
                bloqueoCalendarioRepository.save(resto);
            } else if (empiezaAntes) {
                b.setFechaFin(desde.minusDays(1));
                bloqueoCalendarioRepository.save(b);
            } else if (terminaDespues) {
                b.setFechaInicio(hasta.plusDays(1));
                bloqueoCalendarioRepository.save(b);
            } else {
                bloqueoCalendarioRepository.delete(b);
            }
        }
    }
}
