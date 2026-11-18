package dam.moscosos.api.controller;

import dam.moscosos.api.domain.SesionHorario;
import dam.moscosos.api.service.SesionHorarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@Tag(name = "Sesiones de horario", description = "Consulta y gestión de las sesiones del horario (profesor, grupo, día y franja horaria). "
        + "Todos los endpoints requieren rol GESTOR o DIRECTOR. El día de la semana se expresa como número (1 = lunes ... 5 = viernes).")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Token ausente, inválido o caducado", content = @Content),
        @ApiResponse(responseCode = "403", description = "El rol del usuario no tiene permiso (se requiere GESTOR o DIRECTOR)", content = @Content)
})
public class SesionHorarioController {
    @Autowired
    private SesionHorarioService sesionHorarioService;

    // === GETs ===

    // Ver todos
    // GET /sesiones
    @Operation(summary = "Listar todas las sesiones")
    @ApiResponse(responseCode = "200", description = "Listado de sesiones (puede estar vacío)")
    @GetMapping("/sesiones")
    public List<SesionHorario> verTodos() {
        return sesionHorarioService.verTodos();
    }

    // Buscar por horas
    // GET /sesiones/horario?inicio=09:00&diaSemana=1
    @Operation(summary = "Buscar sesiones por hora de inicio y día",
            description = "Devuelve las sesiones que empiezan a la hora indicada en un día de la semana.")
    @ApiResponse(responseCode = "200", description = "Sesiones encontradas (puede estar vacío)")
    @GetMapping(value = "/sesiones/horario", params = {"inicio", "diaSemana"})
    public List<SesionHorario> buscarHoraEnDia(
            @Parameter(description = "Hora de inicio (HH:mm)", example = "09:00")
            @RequestParam LocalTime inicio,
            @Parameter(description = "Día de la semana (1 = lunes ... 5 = viernes)", example = "1")
            @RequestParam Integer diaSemana) {
        return sesionHorarioService.buscarHoraEnDia(inicio, diaSemana);
    }

    // GET /sesiones/rango?inicio=09:00&fin=14:00
    @Operation(summary = "Buscar sesiones por rango horario",
            description = "Devuelve las sesiones comprendidas entre dos horas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesiones dentro del rango (puede estar vacío)"),
            @ApiResponse(responseCode = "400", description = "'fin' anterior o igual a 'inicio'", content = @Content)
    })
    @GetMapping(value = "/sesiones/rango", params = {"inicio", "fin"})
    public List<SesionHorario> buscarRango(
            @Parameter(description = "Hora de inicio del rango (HH:mm)", example = "09:00")
            @RequestParam LocalTime inicio,
            @Parameter(description = "Hora de fin del rango (HH:mm)", example = "14:00")
            @RequestParam LocalTime fin) {
        return sesionHorarioService.buscarRango(inicio, fin);
    }

    // Buscar por grupos
    // GET /sesiones/grupo/DAM
    @Operation(summary = "Buscar sesiones por grupo")
    @ApiResponse(responseCode = "200", description = "Sesiones del grupo (puede estar vacío)")
    @GetMapping(value = "/sesiones/grupo/{codGrupo}")
    public List<SesionHorario> buscarGrupo(
            @Parameter(description = "Código del grupo", example = "DAM")
            @PathVariable String codGrupo) {
        return sesionHorarioService.buscarGrupo(codGrupo);
    }

    // GET /sesiones/grupo?codGrupo=DAW1&horaInicio=09:00
    @Operation(summary = "Buscar sesiones de un grupo a una hora",
            description = "Devuelve las sesiones del grupo que empiezan a la hora indicada.")
    @ApiResponse(responseCode = "200", description = "Sesiones encontradas (puede estar vacío)")
    @GetMapping(value = "/sesiones/grupo", params = {"codGrupo", "horaInicio"})
    public List<SesionHorario> buscarGrupoHora(
            @Parameter(description = "Código del grupo", example = "DAW1")
            @RequestParam String codGrupo,
            @Parameter(description = "Hora de inicio (HH:mm)", example = "09:00")
            @RequestParam LocalTime horaInicio) {
        return sesionHorarioService.buscarGrupoEnHora(codGrupo, horaInicio);
    }

    // GET /sesiones/grupo?codGrupo=DAW1&diaSemana=1
    @Operation(summary = "Buscar sesiones de un grupo en un día",
            description = "Devuelve las sesiones del grupo en un día de la semana.")
    @ApiResponse(responseCode = "200", description = "Sesiones encontradas (puede estar vacío)")
    @GetMapping(value = "/sesiones/grupo", params = {"codGrupo", "diaSemana"})
    public List<SesionHorario> buscarGrupoEnDia(
            @Parameter(description = "Código del grupo", example = "DAW1")
            @RequestParam String codGrupo,
            @Parameter(description = "Día de la semana (1 = lunes ... 5 = viernes)", example = "1")
            @RequestParam Integer diaSemana) {
        return sesionHorarioService.buscarGrupoEnDia(codGrupo, diaSemana);
    }

    // Buscar por profesor
    // GET /sesiones/profesor/12345678
    @Operation(summary = "Buscar sesiones por profesor")
    @ApiResponse(responseCode = "200", description = "Sesiones del profesor (puede estar vacío)")
    @GetMapping(value = "/sesiones/profesor/{idProfesor}")
    public List<SesionHorario> buscarProfesor(
            @Parameter(description = "Identificador del profesor", example = "12345678")
            @PathVariable String idProfesor) {
        return sesionHorarioService.buscarProfesor(idProfesor);
    }

    // GET /sesiones/profesor?idProfesor=12345678&diaSemana=1
    @Operation(summary = "Buscar sesiones de un profesor en un día",
            description = "Devuelve las sesiones del profesor en un día de la semana.")
    @ApiResponse(responseCode = "200", description = "Sesiones encontradas (puede estar vacío)")
    @GetMapping(value = "/sesiones/profesor", params = {"idProfesor", "diaSemana"})
    public List<SesionHorario> buscarProfesorEnDia(
            @Parameter(description = "Identificador del profesor", example = "12345678")
            @RequestParam String idProfesor,
            @Parameter(description = "Día de la semana (1 = lunes ... 5 = viernes)", example = "1")
            @RequestParam Integer diaSemana) {
        return sesionHorarioService.buscarProfesorEnDia(idProfesor, diaSemana);
    }

    // Buscar por día
    // GET /sesiones/dia/1
    @Operation(summary = "Buscar sesiones por día de la semana")
    @ApiResponse(responseCode = "200", description = "Sesiones del día (puede estar vacío)")
    @GetMapping(value = "/sesiones/dia/{diaSemana}")
    public List<SesionHorario> buscarDia(
            @Parameter(description = "Día de la semana (1 = lunes ... 5 = viernes)", example = "1")
            @PathVariable Integer diaSemana) {
        return sesionHorarioService.buscarDia(diaSemana);
    }

    // Comprobar sesiones de un profesor
    // GET /sesiones/profesor/existe?idProfesor=12345678A&diaSemana=1
    @Operation(summary = "Comprobar si un profesor tiene sesiones en un día",
            description = "Devuelve true si el profesor tiene al menos una sesión ese día de la semana.")
    @ApiResponse(responseCode = "200", description = "true si tiene sesiones, false si no")
    @GetMapping(value = "/sesiones/profesor/existe", params = {"idProfesor", "diaSemana"})
    public boolean tieneProfesorEnDia(
            @Parameter(description = "Identificador del profesor", example = "12345678A")
            @RequestParam String idProfesor,
            @Parameter(description = "Día de la semana (1 = lunes ... 5 = viernes)", example = "1")
            @RequestParam Integer diaSemana) {
        return sesionHorarioService.tieneProfesorEnDia(idProfesor, diaSemana);
    }

    // === POSTs ===

    // POST http://localhost:8080/sesiones
    @Operation(summary = "Crear una sesión",
            description = "Guarda una nueva sesión de horario. El ID se genera automáticamente.")
    @ApiResponse(responseCode = "201", description = "Sesión creada")
    @PostMapping("/sesiones")
    public ResponseEntity<SesionHorario> crearSesionHorario(@RequestBody SesionHorario sesion) {
        return new ResponseEntity<>(sesionHorarioService.crearSesion(sesion), HttpStatus.CREATED);
    }

    // === PUTs ===

    // PUT http://localhost:8080/sesiones/1
    @Operation(summary = "Modificar una sesión",
            description = "Sustituye los datos de la sesión indicada por los recibidos en el cuerpo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesión modificada"),
            @ApiResponse(responseCode = "404", description = "No existe ninguna sesión con ese ID", content = @Content)
    })
    @PutMapping("/sesiones/{id}")
    public ResponseEntity<SesionHorario> modificarSesionHorario(
            @Parameter(description = "ID de la sesión a modificar", example = "1")
            @PathVariable Integer id,
            @RequestBody SesionHorario newSesion) {
        return new ResponseEntity<>(sesionHorarioService.modificarSesion(id, newSesion), HttpStatus.OK);
    }

    // === DELETEs ===

    // DELETE http://localhost:8080/sesiones/1
    @Operation(summary = "Borrar una sesión",
            description = "Elimina la sesión indicada. Atención: este endpoint se expone con el método POST (no DELETE).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sesión eliminada"),
            @ApiResponse(responseCode = "404", description = "No existe ninguna sesión con ese ID", content = @Content)
    })
    @PostMapping("/sesiones/{id}")
    public ResponseEntity<SesionHorario> borrarSesionHorario(
            @Parameter(description = "ID de la sesión a eliminar", example = "1")
            @PathVariable Integer id) {
        sesionHorarioService.borrarSesion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
