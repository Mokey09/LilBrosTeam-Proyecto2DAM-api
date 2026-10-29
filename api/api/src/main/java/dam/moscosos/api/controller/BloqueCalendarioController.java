package dam.moscosos.api.controller;

import dam.moscosos.api.domain.BloqueoCalendario;
import dam.moscosos.api.enums.Tipo;
import dam.moscosos.api.service.BloqueoCalendarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
public class BloqueCalendarioController {
    @Autowired
    private BloqueoCalendarioService bloqueoCalendarioService;

    @GetMapping("/bloqueados")
    public List<BloqueoCalendario> findAll() {
        return bloqueoCalendarioService.findAll();
    }

    // GET http://localhost:8080/bloqueados/dia/2026-12-24
    @GetMapping("/bloqueados/dia/{fecha}")
    public List<BloqueoCalendario> buscarPorDia(@PathVariable LocalDate fecha) {
        return bloqueoCalendarioService.buscarPorDia(fecha);
    }

    // GET http://localhost:8080/bloqueados/rango?inicio=2026-12-23&fin=2027-01-05
    @GetMapping("/bloqueados/rango")
    public List<BloqueoCalendario> buscarPorRango(@RequestParam LocalDate inicio, @RequestParam LocalDate fin) {
        return bloqueoCalendarioService.buscarPorRango(inicio,fin);
    }

    // GET http://localhost:8080/bloqueados/tipo/VACACIONES
    @GetMapping("/bloqueados/tipo/{tipo}")
    public List<BloqueoCalendario> buscarPorTipo(@PathVariable String tipo) {
        if (!Tipo.validarTipo(tipo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo no válido: " + tipo);
        }
        return bloqueoCalendarioService.findByTipo(Tipo.valueOf(tipo.toUpperCase().trim()));
    }

    //alternativa fácil
    /*@PostMapping("/bloqueados")
    public BloqueoCalendario addBloqueoCalendario(@RequestBody BloqueoCalendario bloqueoCalendario) {
        return bloqueoCalendarioService.bloquearCalendario(bloqueoCalendario);
    }*/


    /* BODY
    {
      "fechaInicio": "2026-12-23",
      "fechaFin": "2027-01-07",
      "tipo": "VACACIONES",
      "comentario": "Vacaciones de Navidad"
    }
     */


    @PostMapping("/bloqueados")
    public ResponseEntity<BloqueoCalendario> addBloqueoCalendario(@RequestBody BloqueoCalendario bloqueoCalendario) {
        return new ResponseEntity<>(bloqueoCalendarioService.bloquearCalendario(bloqueoCalendario), HttpStatus.OK);
    }

    // DELETE http://localhost:8080/bloqueados/desbloquear?desde=2026-12-25&hasta=2026-12-26
    @DeleteMapping("/bloqueados/desbloquear")
    public String desbloquearDias(@RequestParam LocalDate desde, @RequestParam LocalDate hasta) {
        bloqueoCalendarioService.desbloquearDias(desde, hasta);
        return "Días desbloqueados";
    }


}
