package dam.moscosos.api.repository;

import dam.moscosos.api.domain.BloqueoCalendario;
import dam.moscosos.api.enums.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BloqueoCalendarioRepository extends JpaRepository<BloqueoCalendario,Integer> {
   // List<BloqueoCalendario> findByFechaInicioAndFechaFin(LocalDate fechaInicio, LocalDate fechaFin);
    List<BloqueoCalendario> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            LocalDate fechaFin,
            LocalDate fechaInicio
    );

    List<BloqueoCalendario> findByTipo(Tipo tipo);

    //Alguno mas?


}
