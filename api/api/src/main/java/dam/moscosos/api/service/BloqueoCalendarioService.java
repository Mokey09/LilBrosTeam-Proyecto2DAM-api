package dam.moscosos.api.service;
import dam.moscosos.api.domain.BloqueoCalendario;
import dam.moscosos.api.enums.Tipo;

import java.time.LocalDate;
import java.util.List;


public interface BloqueoCalendarioService {
    List<BloqueoCalendario> buscarPorRango(LocalDate fechaInicio, LocalDate fechaFin);

    List<BloqueoCalendario> buscarPorDia(LocalDate dia);
    List<BloqueoCalendario> findByTipo(Tipo tipo);
    BloqueoCalendario bloquearCalendario(BloqueoCalendario calendario);
    List<BloqueoCalendario> findAll();
    void desbloquearDias(LocalDate desde, LocalDate hasta);

}
