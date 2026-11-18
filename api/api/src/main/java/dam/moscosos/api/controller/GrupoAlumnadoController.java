package dam.moscosos.api.controller;

import dam.moscosos.api.domain.GrupoAlumnado;
import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.service.GrupoAlumnadoService;
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

import java.util.List;

@RestController
@Tag(name = "Grupos de alumnado", description = "Gestión de los grupos de alumnado. Todos los endpoints requieren rol GESTOR o DIRECTOR.")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Token ausente, inválido o caducado", content = @Content),
        @ApiResponse(responseCode = "403", description = "El rol del usuario no tiene permiso (se requiere GESTOR o DIRECTOR)", content = @Content)
})
public class GrupoAlumnadoController {

    @Autowired
    private GrupoAlumnadoService grupoAlumnadoService;

    // GET http://localhost:8080/grupos
    @Operation(summary = "Listar todos los grupos")
    @ApiResponse(responseCode = "200", description = "Listado de grupos (puede estar vacío)")
    @GetMapping("/grupos")
    public List<GrupoAlumnado> findAll() {
        return grupoAlumnadoService.findAll();
    }

    // GET http://localhost:8080/grupos/1
    @Operation(summary = "Buscar grupo por código")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún grupo con ese código", content = @Content)
    })
    @GetMapping("/grupos/{codGrupo}")
    public GrupoAlumnado findById(
            @Parameter(description = "Código del grupo", example = "1")
            @PathVariable Integer codGrupo) {
        return grupoAlumnadoService.findById(codGrupo);
    }

    // GET http://localhost:8080/grupos/nombre/1DAM
    @Operation(summary = "Buscar grupo por nombre",
            description = "La búsqueda no distingue mayúsculas de minúsculas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún grupo con ese nombre", content = @Content)
    })
    @GetMapping("/grupos/nombre/{nombreGrupo}")
    public GrupoAlumnado findByNombre(
            @Parameter(description = "Nombre del grupo", example = "1DAM")
            @PathVariable String nombreGrupo) {
        return grupoAlumnadoService.findByNombre(nombreGrupo);
    }

    // GET http://localhost:8080/grupos/1/profesores
    @Operation(summary = "Listar los profesores de un grupo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesores asociados al grupo (puede estar vacío)"),
            @ApiResponse(responseCode = "404", description = "No existe ningún grupo con ese código", content = @Content)
    })
    @GetMapping("/grupos/{codGrupo}/profesores")
    public List<Profesor> findProfesores(
            @Parameter(description = "Código del grupo", example = "1")
            @PathVariable Integer codGrupo) {
        return grupoAlumnadoService.findProfesores(codGrupo);
    }

    // POST http://localhost:8080/grupos
    /* BODY
        {
            "nombreGrupo": "1DAM",
            "idProfesor": 1
        }

     */
    @Operation(summary = "Crear un grupo",
            description = "Crea un grupo nuevo. El código se genera automáticamente (se ignora si viene en el cuerpo). El nombre es obligatorio y no puede repetirse.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Grupo creado"),
            @ApiResponse(responseCode = "400", description = "Falta 'nombreGrupo'", content = @Content),
            @ApiResponse(responseCode = "409", description = "Ya existe un grupo con ese nombre", content = @Content)
    })
    @PostMapping("/grupos")
    public ResponseEntity<GrupoAlumnado> crear(@RequestBody GrupoAlumnado grupo) {
        return new ResponseEntity<>(grupoAlumnadoService.crear(grupo), HttpStatus.CREATED);
    }

    // PUT http://localhost:8080/grupos/1
    @Operation(summary = "Actualizar un grupo",
            description = "Actualiza el nombre y el profesor del grupo indicado. El nombre no puede coincidir con el de otro grupo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo actualizado"),
            @ApiResponse(responseCode = "400", description = "Falta 'nombreGrupo'", content = @Content),
            @ApiResponse(responseCode = "404", description = "No existe ningún grupo con ese código", content = @Content),
            @ApiResponse(responseCode = "409", description = "Ya existe otro grupo con ese nombre", content = @Content)
    })
    @PutMapping("/grupos/{codGrupo}")
    public GrupoAlumnado actualizar(
            @Parameter(description = "Código del grupo a actualizar", example = "1")
            @PathVariable Integer codGrupo,
            @RequestBody GrupoAlumnado datos) {
        return grupoAlumnadoService.actualizar(codGrupo, datos);
    }

    // DELETE http://localhost:8080/grupos/1
    @Operation(summary = "Eliminar un grupo",
            description = "Elimina el grupo y las sesiones de horario asociadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Grupo eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún grupo con ese código", content = @Content)
    })
    @DeleteMapping("/grupos/{codGrupo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @Parameter(description = "Código del grupo a eliminar", example = "1")
            @PathVariable Integer codGrupo) {
        grupoAlumnadoService.eliminar(codGrupo);
    }
}
