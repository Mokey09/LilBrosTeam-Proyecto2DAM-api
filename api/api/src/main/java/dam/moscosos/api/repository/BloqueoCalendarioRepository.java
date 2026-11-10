package dam.moscosos.api.repository;

import dam.moscosos.api.domain.BloqueoCalendario;
import dam.moscosos.api.enums.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BloqueoCalendarioRepository extends JpaRepository<BloqueoCalendario, Integer> {

    List<BloqueoCalendario> findByFecha(LocalDate fecha);

    // Between es inclusivo en ambos extremos
    List<BloqueoCalendario> findByFechaBetweenOrderByFechaAsc(LocalDate inicio, LocalDate fin);

    List<BloqueoCalendario> findByTipo(Tipo tipo);

    void deleteByFechaBetween(LocalDate desde, LocalDate hasta);
}