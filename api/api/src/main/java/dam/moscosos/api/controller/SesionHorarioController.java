package dam.moscosos.api.controller;

import dam.moscosos.api.domain.SesionHorario;
import dam.moscosos.api.service.SesionHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
public class SesionHorarioController {
    @Autowired
    private SesionHorarioService sesionHorarioService;

    // === GETs ===

    // Ver todos
    @GetMapping("/sesiones")
    public List<SesionHorario> verTodos() {
        return sesionHorarioService.verTodos();
    }

    // Buscar por horas
    @GetMapping(value = "/sesiones/horario", params = {"inicio", "diaSemana"})
    public List<SesionHorario> buscarHoraEnDia(@RequestParam LocalTime inicio, @RequestParam Integer diaSemana) {
        return sesionHorarioService.buscarHoraEnDia(inicio, diaSemana);
    }

    @GetMapping(value = "/sesiones/rango", params = {"inicio", "fin"})
    public List<SesionHorario> buscarRango(@RequestParam LocalTime inicio, @RequestParam LocalTime fin) {
        return sesionHorarioService.buscarRango(inicio, fin);
    }

    // Buscar por grupos
    @GetMapping(value = "/sesiones/grupo/{codGrupo}")
    public List<SesionHorario> buscarGrupo(@PathVariable String codGrupo) {
        return sesionHorarioService.buscarGrupo(codGrupo);
    }

    @GetMapping(value = "/sesiones/grupo", params = {"codGrupo", "horaInicio"})
    public List<SesionHorario> buscarGrupoHora(@RequestParam String codGrupo, @RequestParam LocalTime horaInicio) {
        return sesionHorarioService.buscarGrupoEnHora(codGrupo, horaInicio);
    }

    @GetMapping(value = "/sesiones/grupo", params = {"codGrupo", "diaSemana"})
    public List<SesionHorario> buscarGrupoEnDia(@RequestParam String codGrupo, @RequestParam Integer diaSemana) {
        return sesionHorarioService.buscarGrupoEnDia(codGrupo, diaSemana);
    }

    // Buscar por profesor
    @GetMapping(value = "/sesiones/profesor/{idProfesor}")
    public List<SesionHorario> buscarProfesor(@PathVariable String idProfesor) {
        return sesionHorarioService.buscarProfesor(idProfesor);
    }

    @GetMapping(value = "/sesiones/profesor", params = {"idProfesor", "diaSemana"})
    public List<SesionHorario> buscarProfesorEnDia(@RequestParam String idProfesor, @RequestParam Integer diaSemana) {
        return sesionHorarioService.buscarProfesorEnDia(idProfesor, diaSemana);
    }

    // Buscar por día
    @GetMapping(value = "/sesiones/dia/{diaSemana}")
    public List<SesionHorario> buscarDia(@PathVariable Integer diaSemana) {
        return sesionHorarioService.buscarDia(diaSemana);
    }

    // Comprobar sesiones de un profesor
    @GetMapping(value = "/sesiones/profesor/existe", params = {"idProfesor", "diaSemana"})
    public boolean tieneProfesorEnDia(@RequestParam String idProfesor, @RequestParam Integer diaSemana) {
        return sesionHorarioService.tieneProfesorEnDia(idProfesor, diaSemana);
    }

    // === POSTs ===

    @PostMapping("/sesiones")
    public ResponseEntity<SesionHorario> crearSesionHorario(@RequestBody SesionHorario sesion) {
        return new ResponseEntity<>(sesionHorarioService.crearSesion(sesion), HttpStatus.CREATED);
    }

    // === PUTs ===

    @PutMapping("/sesiones/{id}")
    public ResponseEntity<SesionHorario> modificarSesionHorario(@PathVariable Integer id, @RequestBody SesionHorario newSesion) {
        return new ResponseEntity<>(sesionHorarioService.modificarSesion(id, newSesion), HttpStatus.OK);
    }

    // === DELETEs ===

    @PostMapping("/sesiones/{id}")
    public ResponseEntity<SesionHorario> borrarSesionHorario(@PathVariable Integer id) {
        sesionHorarioService.borrarSesion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

