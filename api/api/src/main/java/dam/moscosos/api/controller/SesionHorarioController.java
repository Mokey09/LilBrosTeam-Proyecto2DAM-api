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
    // GET /sesiones
    @GetMapping("/sesiones")
    public List<SesionHorario> verTodos() {
        return sesionHorarioService.verTodos();
    }

    // Buscar por horas
    // GET /sesiones/horario?inicio=09:00&diaSemana=1
    @GetMapping(value = "/sesiones/horario", params = {"inicio", "diaSemana"})
    public List<SesionHorario> buscarHoraEnDia(@RequestParam LocalTime inicio, @RequestParam Integer diaSemana) {
        return sesionHorarioService.buscarHoraEnDia(inicio, diaSemana);
    }

    // GET /sesiones/rango?inicio=09:00&fin=14:00
    @GetMapping(value = "/sesiones/rango", params = {"inicio", "fin"})
    public List<SesionHorario> buscarRango(@RequestParam LocalTime inicio, @RequestParam LocalTime fin) {
        return sesionHorarioService.buscarRango(inicio, fin);
    }

    // Buscar por grupos
    // GET /sesiones/grupo/DAM
    @GetMapping(value = "/sesiones/grupo/{codGrupo}")
    public List<SesionHorario> buscarGrupo(@PathVariable String codGrupo) {
        return sesionHorarioService.buscarGrupo(codGrupo);
    }

    // GET /sesiones/grupo?codGrupo=DAW1&horaInicio=09:00
    @GetMapping(value = "/sesiones/grupo", params = {"codGrupo", "horaInicio"})
    public List<SesionHorario> buscarGrupoHora(@RequestParam String codGrupo, @RequestParam LocalTime horaInicio) {
        return sesionHorarioService.buscarGrupoEnHora(codGrupo, horaInicio);
    }

    // GET /sesiones/grupo?codGrupo=DAW1&diaSemana=1
    @GetMapping(value = "/sesiones/grupo", params = {"codGrupo", "diaSemana"})
    public List<SesionHorario> buscarGrupoEnDia(@RequestParam String codGrupo, @RequestParam Integer diaSemana) {
        return sesionHorarioService.buscarGrupoEnDia(codGrupo, diaSemana);
    }

    // Buscar por profesor
    // GET /sesiones/profesor/12345678
    @GetMapping(value = "/sesiones/profesor/{idProfesor}")
    public List<SesionHorario> buscarProfesor(@PathVariable String idProfesor) {
        return sesionHorarioService.buscarProfesor(idProfesor);
    }

    // GET /sesiones/profesor?idProfesor=12345678&diaSemana=1
    @GetMapping(value = "/sesiones/profesor", params = {"idProfesor", "diaSemana"})
    public List<SesionHorario> buscarProfesorEnDia(@RequestParam String idProfesor, @RequestParam Integer diaSemana) {
        return sesionHorarioService.buscarProfesorEnDia(idProfesor, diaSemana);
    }

    // Buscar por día
    // GET /sesiones/dia/1
    @GetMapping(value = "/sesiones/dia/{diaSemana}")
    public List<SesionHorario> buscarDia(@PathVariable Integer diaSemana) {
        return sesionHorarioService.buscarDia(diaSemana);
    }

    // Comprobar sesiones de un profesor
    // GET /sesiones/profesor/existe?idProfesor=12345678A&diaSemana=1
    @GetMapping(value = "/sesiones/profesor/existe", params = {"idProfesor", "diaSemana"})
    public boolean tieneProfesorEnDia(@RequestParam String idProfesor, @RequestParam Integer diaSemana) {
        return sesionHorarioService.tieneProfesorEnDia(idProfesor, diaSemana);
    }

    // === POSTs ===

    // POST http://localhost:8080/sesiones
    @PostMapping("/sesiones")
    public ResponseEntity<SesionHorario> crearSesionHorario(@RequestBody SesionHorario sesion) {
        return new ResponseEntity<>(sesionHorarioService.crearSesion(sesion), HttpStatus.CREATED);
    }

    // === PUTs ===

    // PUT http://localhost:8080/sesiones/1
    @PutMapping("/sesiones/{id}")
    public ResponseEntity<SesionHorario> modificarSesionHorario(@PathVariable Integer id, @RequestBody SesionHorario newSesion) {
        return new ResponseEntity<>(sesionHorarioService.modificarSesion(id, newSesion), HttpStatus.OK);
    }

    // === DELETEs ===

    // DELETE http://localhost:8080/sesiones/1
    @PostMapping("/sesiones/{id}")
    public ResponseEntity<SesionHorario> borrarSesionHorario(@PathVariable Integer id) {
        sesionHorarioService.borrarSesion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

