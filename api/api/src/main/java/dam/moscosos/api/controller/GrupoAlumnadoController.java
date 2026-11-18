package dam.moscosos.api.controller;

import dam.moscosos.api.domain.GrupoAlumnado;
import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.service.GrupoAlumnadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GrupoAlumnadoController {

    @Autowired
    private GrupoAlumnadoService grupoAlumnadoService;

    // GET http://localhost:8080/grupos
    @GetMapping("/grupos")
    public List<GrupoAlumnado> findAll() {
        return grupoAlumnadoService.findAll();
    }

    // GET http://localhost:8080/grupos/1
    @GetMapping("/grupos/{codGrupo}")
    public GrupoAlumnado findById(@PathVariable Integer codGrupo) {
        return grupoAlumnadoService.findById(codGrupo);
    }

    // GET http://localhost:8080/grupos/nombre/1DAM
    @GetMapping("/grupos/nombre/{nombreGrupo}")
    public GrupoAlumnado findByNombre(@PathVariable String nombreGrupo) {
        return grupoAlumnadoService.findByNombre(nombreGrupo);
    }

    // GET http://localhost:8080/grupos/1/profesores
    @GetMapping("/grupos/{codGrupo}/profesores")
    public List<Profesor> findProfesores(@PathVariable Integer codGrupo) {
        return grupoAlumnadoService.findProfesores(codGrupo);
    }

    // POST http://localhost:8080/grupos
    /* BODY
        {
            "nombreGrupo": "1DAM",
            "idProfesor": 1
        }

     */
    @PostMapping("/grupos")
    public ResponseEntity<GrupoAlumnado> crear(@RequestBody GrupoAlumnado grupo) {
        return new ResponseEntity<>(grupoAlumnadoService.crear(grupo), HttpStatus.CREATED);
    }

    // PUT http://localhost:8080/grupos/1
    @PutMapping("/grupos/{codGrupo}")
    public GrupoAlumnado actualizar(@PathVariable Integer codGrupo, @RequestBody GrupoAlumnado datos) {
        return grupoAlumnadoService.actualizar(codGrupo, datos);
    }

    // DELETE http://localhost:8080/grupos/1
    @DeleteMapping("/grupos/{codGrupo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer codGrupo) {
        grupoAlumnadoService.eliminar(codGrupo);
    }
}