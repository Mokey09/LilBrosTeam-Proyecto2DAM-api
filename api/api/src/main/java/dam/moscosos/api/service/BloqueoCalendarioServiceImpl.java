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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BloqueoCalendarioServiceImpl implements BloqueoCalendarioService {

    private static final int MAX_DIAS = 366;

    @Autowired
    BloqueoCalendarioRepository bloqueoCalendarioRepository;

    @Override
    public List<BloqueoCalendario> findAll() {
        return bloqueoCalendarioRepository.findAll();
    }

    @Override
    public List<BloqueoCalendario> buscarPorDia(LocalDate dia) {
        return bloqueoCalendarioRepository.findByFecha(dia);
    }

    @Override
    public List<BloqueoCalendario> buscarPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'fin' no puede ser anterior a 'inicio'");
        }
        return bloqueoCalendarioRepository.findByFechaBetweenOrderByFechaAsc(fechaInicio, fechaFin);
    }

    @Override
    public List<BloqueoCalendario> findByTipo(Tipo tipo) {
        return bloqueoCalendarioRepository.findByTipo(tipo);
    }

    @Override
    @Transactional
    public List<BloqueoCalendario> bloquearCalendario(LocalDate inicio, LocalDate fin, Tipo tipo, String comentario) {
        if (inicio == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'fechaInicio' es obligatoria");
        }
        if (fin == null) {
            fin = inicio; // un solo día
        }
        if (fin.isBefore(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'fechaFin' no puede ser anterior a 'fechaInicio'");
        }
        if (tipo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'tipo' es obligatorio");
        }
        if (inicio.datesUntil(fin.plusDays(1)).count() > MAX_DIAS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rango no puede superar " + MAX_DIAS + " días");
        }

        // Días ya bloqueados dentro del rango: se respetan tal cual (con su tipo y comentario originales)
        Set<LocalDate> yaBloqueados = bloqueoCalendarioRepository
                .findByFechaBetweenOrderByFechaAsc(inicio, fin)
                .stream()
                .map(BloqueoCalendario::getFecha)
                .collect(Collectors.toSet());

        // Una fila por día, saltándose los que ya existen
        List<BloqueoCalendario> nuevos = new ArrayList<>();
        for (LocalDate dia = inicio; !dia.isAfter(fin); dia = dia.plusDays(1)) {
            if (!yaBloqueados.contains(dia)) {
                nuevos.add(new BloqueoCalendario(null, dia, tipo, comentario));
            }
        }

        return bloqueoCalendarioRepository.saveAll(nuevos);
    }

    @Override
    @Transactional
    public void desbloquearDias(LocalDate desde, LocalDate hasta) {
        if (hasta.isBefore(desde)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'hasta' no puede ser anterior a 'desde'");
        }
        bloqueoCalendarioRepository.deleteByFechaBetween(desde, hasta);
    }
}