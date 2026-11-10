package dam.moscosos.api.service;

import dam.moscosos.api.domain.SesionHorario;

import java.time.LocalTime;
import java.util.List;

public interface SesionHorarioService {
    // === READ ===

    // Ver todos
    List<SesionHorario> verTodos();

    // Buscar por horas
    List<SesionHorario> buscarHoraEnDia(LocalTime horaInicio, Integer diaSemana);
    List<SesionHorario> buscarRango(LocalTime horaInicio, LocalTime horaFin);

    // Buscar por grupos
    List<SesionHorario> buscarGrupo(String codGrupo);
    List<SesionHorario> buscarGrupoEnHora(String codGrupo, LocalTime horaInicio);
    List<SesionHorario> buscarGrupoEnDia(String codGrupo, Integer diaSemana);

    // Buscar por profesor
    List<SesionHorario> buscarProfesor(String idProfesor);
    List<SesionHorario> buscarProfesorEnDia(String idProfesor, Integer diaSemana);

    // Buscar por dia de la semana
    List<SesionHorario> buscarDia(Integer diaSemana);

    // Si tiene el profesor una sesion ese dia
    boolean tieneProfesorEnDia(String idProfesor, Integer diaSemana);

    // === CREATE ===

    SesionHorario crearSesion(SesionHorario sesion);

    // === UPDATE ===

    SesionHorario modificarSesion(Integer id, SesionHorario sesion);

    // === DELETE ===

    void borrarSesion(Integer id);
}
