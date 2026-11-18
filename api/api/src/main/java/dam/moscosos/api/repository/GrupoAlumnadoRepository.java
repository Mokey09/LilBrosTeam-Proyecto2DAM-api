package dam.moscosos.api.repository;

import dam.moscosos.api.domain.GrupoAlumnado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoAlumnadoRepository extends JpaRepository<GrupoAlumnado, Integer> {

    Optional<GrupoAlumnado> findByNombreGrupoIgnoreCase(String nombreGrupo);

    boolean existsByNombreGrupoIgnoreCase(String nombreGrupo);
}