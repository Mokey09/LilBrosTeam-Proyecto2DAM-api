package dam.moscosos.api.controller;
import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.service.ProfesorService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @GetMapping("/profesores")
    public List<Profesor> findAll(){
        return profesorService.findAll();
    }

    @GetMapping("/profesores/{id]")
    public ResponseEntity<Profesor> findById(@PathVariable Integer id){
        return profesorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/profesores/{email}")
    public ResponseEntity<Profesor> findByEmail(@PathVariable String email){
        return profesorService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/profesores")
    public ResponseEntity<Profesor> save(@RequestBody Profesor profesor) {
        return new ResponseEntity<>(profesorService.save(profesor), HttpStatus.OK);
    }

    @PutMapping("/profesores/{id}")
    public ResponseEntity<Profesor> update(@PathVariable Integer id, Profesor profesor) {
        return new ResponseEntity<>(profesorService.update(id,profesor), HttpStatus.OK);
    }

    @DeleteMapping("/profesores/{id}")
    public String delete(@PathVariable Integer id){
        profesorService.delete(id);
        return "Profesor eliminado correctamente";
    }
}
