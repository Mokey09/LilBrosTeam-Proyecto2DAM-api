package dam.moscosos.api.controller;

import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.service.ProfesorService;
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
@Tag(name = "Profesores", description = "Operaciones CRUD sobre los profesores. Todos los endpoints requieren rol DIRECTOR.")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Token ausente, inválido o caducado", content = @Content),
        @ApiResponse(responseCode = "403", description = "El rol del usuario no tiene permiso (se requiere DIRECTOR)", content = @Content)
})
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    // GET http://localhost:8080/profesores
    @Operation(summary = "Listar todos los profesores",
            description = "Devuelve todos los profesores registrados. La contraseña nunca se incluye en la respuesta.")
    @ApiResponse(responseCode = "200", description = "Listado de profesores (puede estar vacío)")
    @GetMapping("/profesores")
    public List<Profesor> findAll(){
        return profesorService.findAll();
    }

    // GET http://localhost:8080/profesores/1
    @Operation(summary = "Buscar profesor por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún profesor con ese ID", content = @Content)
    })
    @GetMapping("/profesores/{id}")
    public ResponseEntity<Profesor> findById(
            @Parameter(description = "ID del profesor", example = "1")
            @PathVariable Integer id){
        return profesorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET http://localhost:8080/profesores/email/juan.perez@moscosos.dam
    @Operation(summary = "Buscar profesor por email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún profesor con ese email", content = @Content)
    })
    @GetMapping("/profesores/email/{email}")
    public ResponseEntity<Profesor> findByEmail(
            @Parameter(description = "Email del profesor", example = "juan.perez@moscosos.dam")
            @PathVariable String email){
        return profesorService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST http://localhost:8080/profesores
    /* EL DNI Y EMAIL SON UNICOS
        {
          "nombre": "Laura",
          "apellidos": "García Pérez",
          "dni": "12345678A",
          "email": "laura.garcia@centro.es",
          "contrasena": "1234",
          "rol": "USUARIO",
          "cuerpo": "Profesor de Enseñanza Secundaria",
          "departamento": "Informática",
          "especialidad": "Programación",
          "direccion": "Calle Mayor 12",
          "localidad": "Valencia",
          "cp": 46001,
          "telefono": "600123456"
}
     */
    @Operation(summary = "Crear un profesor",
            description = "Guarda un nuevo profesor. El ID se genera automáticamente. El DNI y el email deben ser únicos. "
                    + "La contraseña se guarda cifrada (BCrypt).")
    @ApiResponse(responseCode = "200", description = "Profesor creado")
    @PostMapping("/profesores")
    public ResponseEntity<Profesor> save(@RequestBody Profesor profesor) {
        return new ResponseEntity<>(profesorService.save(profesor), HttpStatus.OK);
    }

    // PUT http://localhost:8080/profesores/1
    @Operation(summary = "Actualizar un profesor",
            description = "Sustituye los datos del profesor indicado por los recibidos en el cuerpo. "
                    + "Nombre, apellidos, DNI y email son obligatorios. Si no se envía contraseña, rol o grupo de alumnado, "
                    + "se conservan los actuales.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor actualizado"),
            @ApiResponse(responseCode = "400", description = "Falta algún dato obligatorio", content = @Content),
            @ApiResponse(responseCode = "404", description = "No existe ningún profesor con ese ID", content = @Content),
            @ApiResponse(responseCode = "409", description = "El DNI o el email ya pertenecen a otro profesor", content = @Content)
    })
    @PutMapping("/profesores/{id}")
    public ResponseEntity<Profesor> update(
            @Parameter(description = "ID del profesor a actualizar", example = "1")
            @PathVariable Integer id,
            @RequestBody Profesor profesor) {
        return new ResponseEntity<>(profesorService.update(id, profesor), HttpStatus.OK);
    }

    // DELETE http://localhost:8080/profesores/1
    @Operation(summary = "Eliminar un profesor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe ningún profesor con ese ID", content = @Content)
    })
    @DeleteMapping("/profesores/{id}")
    public String delete(
            @Parameter(description = "ID del profesor a eliminar", example = "1")
            @PathVariable Integer id){
        profesorService.delete(id);
        return "Profesor eliminado correctamente";
    }
}
