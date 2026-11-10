package dam.moscosos.api.service;

import dam.moscosos.api.domain.SesionHorario;
import dam.moscosos.api.repository.SesionHorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;

@Service
public class SesionHorarioServiceImpl implements SesionHorarioService {
    @Autowired
    SesionHorarioRepository sesionHorarioRepository;

    // === READ ===

    // Ver todos
    @Override
    public List<SesionHorario> verTodos() {
        return sesionHorarioRepository.findAll();
    }

    // Buscar por horas
    @Override
    public List<SesionHorario> buscarHoraEnDia(LocalTime horaInicio, Integer diaSemana) {
        return sesionHorarioRepository.findByHoraInicioAndDiaSemana(horaInicio, diaSemana);
    }

    @Override
    public List<SesionHorario> buscarRango(LocalTime horaInicio, LocalTime horaFin) {
        if (!horaFin.isAfter(horaInicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'fin' no puede ser anterior ni igual a 'inicio'");
        }

        return sesionHorarioRepository.findByHoraInicioBetweenOrderByHoraInicioAsc(horaInicio, horaFin);
    }

    // Buscar por grupos
    @Override
    public List<SesionHorario> buscarGrupo(String codGrupo) {
        return sesionHorarioRepository.findByCodGrupo(codGrupo);
    }

    @Override
    public List<SesionHorario> buscarGrupoEnHora(String codGrupo, LocalTime horaInicio) {
        return sesionHorarioRepository.findByCodGrupoAndHoraInicio(codGrupo, horaInicio);
    }

    @Override
    public List<SesionHorario> buscarGrupoEnDia(String codGrupo, Integer diaSemana) {
        return sesionHorarioRepository.findByCodGrupoAndDiaSemana(codGrupo, diaSemana);
    }

    // Buscar por profesor
    @Override
    public List<SesionHorario> buscarProfesor(String idProfesor) {
        return sesionHorarioRepository.findByIdProfesor(idProfesor);
    }

    @Override
    public List<SesionHorario> buscarProfesorEnDia(String idProfesor, Integer diaSemana) {
        return sesionHorarioRepository.findByIdProfesorAndDiaSemana(idProfesor, diaSemana);
    }

    // Buscar por dia de la semana
    @Override
    public List<SesionHorario> buscarDia(Integer diaSemana) {
        return sesionHorarioRepository.findByDiaSemana(diaSemana);
    }

    // Si tiene el profesor una sesion ese dia
    @Override
    public boolean tieneProfesorEnDia(String idProfesor, Integer diaSemana) {
        return sesionHorarioRepository.existsByIdProfesorAndDiaSemana(idProfesor, diaSemana);
    }

    // === CREATE ===

    @Override
    public SesionHorario crearSesion(SesionHorario sesion) {
        return sesionHorarioRepository.save(sesion);
    }

    // === UPDATE ===

    @Override
    public SesionHorario modificarSesion(Integer id, SesionHorario sesion) {
        // TODO esto me da mucho miedo
        return null;
    }

    // === DELETE ===

    @Override
    public void borrarSesion(Integer id) {
        // Tambien podria hacerse una excepcion propia como en los ejercicios
        sesionHorarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sesión no encontrada: " + id));
        sesionHorarioRepository.deleteById(id);
    }
}
