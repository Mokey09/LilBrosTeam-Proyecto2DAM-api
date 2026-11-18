package dam.moscosos.api.repository;

import dam.moscosos.api.domain.SesionHorario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface SesionHorarioRepository extends JpaRepository<SesionHorario, Integer> {
    // GET
    // Buscar por horas
    List<SesionHorario> findByHoraInicioAndDiaSemana(LocalTime horaInicio, Integer diaSemana);
    List<SesionHorario> findByHoraInicioBetweenOrderByHoraInicioAsc(LocalTime horaInicio, LocalTime horaFin);

    // Buscar por grupos
    List<SesionHorario> findByCodGrupo(String codGrupo);
    List<SesionHorario> findByCodGrupoAndHoraInicio(String codGrupo, LocalTime horaInicio);
    List<SesionHorario> findByCodGrupoAndDiaSemana(String codGrupo, Integer diaSemana);

    // Buscar por profesor
    List<SesionHorario> findByIdProfesor(String idProfesor);
    List<SesionHorario> findByIdProfesorAndDiaSemana(String idProfesor, Integer diaSemana);

    // Buscar por dia de la semana
    List<SesionHorario> findByDiaSemana(Integer diaSemana);

    // Si tiene el profesor una sesion ese dia
    boolean existsByIdProfesorAndDiaSemana(String idProfesor, Integer diaSemana);
}
