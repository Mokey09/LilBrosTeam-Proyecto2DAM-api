package dam.moscosos.api.controller;

import dam.moscosos.api.domain.BloqueoCalendario;
import dam.moscosos.api.enums.Tipo;
import dam.moscosos.api.service.BloqueoCalendarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Bloqueos de calendario", description = "Gestión de los días bloqueados del calendario (festivos, vacaciones, evaluaciones...). Hay una fila por día. "
        + "Consultar: cualquier usuario autenticado. Crear y eliminar: GESTOR o DIRECTOR.")
@ApiResponse(responseCode = "401", description = "Token ausente, inválido o caducado", content = @Content)
public class BloqueCalendarioController {
    @Autowired
    private BloqueoCalendarioService bloqueoCalendarioService;

    @Operation(summary = "Listar todos los bloqueos",
            description = "Devuelve todos los días bloqueados del calendario.")
    @ApiResponse(responseCode = "200", description = "Listado de bloqueos (puede estar vacío)")
    @GetMapping("/bloqueados")
    public List<BloqueoCalendario> findAll() {
        return bloqueoCalendarioService.findAll();
    }

    // GET http://localhost:8080/bloqueados/dia/2026-12-24
    @Operation(summary = "Buscar bloqueos por día",
            description = "Devuelve los bloqueos de un día concreto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bloqueos del día (lista vacía si no hay ninguno)"),
            @ApiResponse(responseCode = "400", description = "Fecha con formato no válido", content = @Content)
    })
    @GetMapping("/bloqueados/dia/{fecha}")
    public List<BloqueoCalendario> buscarPorDia(
            @Parameter(description = "Día a consultar (yyyy-MM-dd)", example = "2026-12-24")
            @PathVariable LocalDate fecha) {
        return bloqueoCalendarioService.buscarPorDia(fecha);
    }

    // GET http://localhost:8080/bloqueados/rango?inicio=2026-12-23&fin=2027-01-05
    @Operation(summary = "Buscar bloqueos por rango de fechas",
            description = "Devuelve los bloqueos entre dos fechas (ambas incluidas), ordenados por fecha ascendente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bloqueos dentro del rango"),
            @ApiResponse(responseCode = "400", description = "Fecha no válida o 'fin' anterior a 'inicio'", content = @Content)
    })
    @GetMapping("/bloqueados/rango")
    public List<BloqueoCalendario> buscarPorRango(
            @Parameter(description = "Primer día del rango (yyyy-MM-dd)", example = "2026-12-23")
            @RequestParam LocalDate inicio,
            @Parameter(description = "Último día del rango (yyyy-MM-dd)", example = "2027-01-05")
            @RequestParam LocalDate fin) {
        return bloqueoCalendarioService.buscarPorRango(inicio, fin);
    }

    // GET http://localhost:8080/bloqueados/tipo/VACACIONES
    @Operation(summary = "Buscar bloqueos por tipo",
            description = "Devuelve los bloqueos de un tipo concreto. El valor no distingue mayúsculas de minúsculas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bloqueos del tipo indicado"),
            @ApiResponse(responseCode = "400", description = "Tipo no válido", content = @Content)
    })
    @GetMapping("/bloqueados/tipo/{tipo}")
    public List<BloqueoCalendario> buscarPorTipo(
            @Parameter(description = "Tipo de bloqueo", schema = @Schema(implementation = Tipo.class), example = "VACACIONES")
            @PathVariable String tipo) {
        if (!Tipo.validarTipo(tipo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo no válido: " + tipo);
        }
        return bloqueoCalendarioService.findByTipo(Tipo.valueOf(tipo.toUpperCase().trim()));
    }

    /* POST http://localhost:8080/bloqueados
        BODY (fechaFin es opcional; si no se envía, se bloquea solo fechaInicio)
        {
            "fechaInicio": "2026-12-23",
            "fechaFin": "2027-01-07",
            "tipo": "VACACIONES",
            "comentario": "Vacaciones de Navidad"
        }
     */
    @Operation(summary = "Bloquear días del calendario",
            description = "Crea un bloqueo por cada día del rango [fechaInicio, fechaFin]. Si no se envía fechaFin, se bloquea solo fechaInicio. "
                    + "Los días que ya estaban bloqueados se respetan y no se duplican. El rango máximo es de 366 días. Requiere rol GESTOR o DIRECTOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bloqueos creados (solo los días que no estaban bloqueados)"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos: falta fechaInicio o tipo, fechaFin anterior a fechaInicio o rango superior a 366 días", content = @Content),
            @ApiResponse(responseCode = "403", description = "El rol del usuario no tiene permiso (se requiere GESTOR o DIRECTOR)", content = @Content)
    })
    @PostMapping("/bloqueados")
    public ResponseEntity<List<BloqueoCalendario>> addBloqueoCalendario(@RequestBody BloqueoRequest request) {
        List<BloqueoCalendario> creados = bloqueoCalendarioService.bloquearCalendario(
                request.fechaInicio(), request.fechaFin(), request.tipo(), request.comentario());
        return new ResponseEntity<>(creados, HttpStatus.CREATED);
    }

    // DELETE http://localhost:8080/bloqueados/desbloquear?desde=2026-12-25&hasta=2026-12-26
    @Operation(summary = "Desbloquear días del calendario",
            description = "Elimina los bloqueos comprendidos entre dos fechas (ambas incluidas). Requiere rol GESTOR o DIRECTOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Días desbloqueados"),
            @ApiResponse(responseCode = "400", description = "Fecha no válida o 'hasta' anterior a 'desde'", content = @Content),
            @ApiResponse(responseCode = "403", description = "El rol del usuario no tiene permiso (se requiere GESTOR o DIRECTOR)", content = @Content)
    })
    @DeleteMapping("/bloqueados/desbloquear")
    public String desbloquearDias(
            @Parameter(description = "Primer día a desbloquear (yyyy-MM-dd)", example = "2026-12-25")
            @RequestParam LocalDate desde,
            @Parameter(description = "Último día a desbloquear (yyyy-MM-dd)", example = "2026-12-26")
            @RequestParam LocalDate hasta) {
        bloqueoCalendarioService.desbloquearDias(desde, hasta);
        return "Días desbloqueados";
    }

    // Cuerpo del POST: la entidad ya no tiene fechaInicio/fechaFin, así que se usa un record aparte
    @Schema(description = "Datos para bloquear uno o varios días del calendario")
    public record BloqueoRequest(
            @Schema(description = "Primer día a bloquear", example = "2026-12-23", requiredMode = Schema.RequiredMode.REQUIRED)
            LocalDate fechaInicio,
            @Schema(description = "Último día a bloquear (opcional; si no se envía, solo se bloquea fechaInicio)", example = "2027-01-07", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            LocalDate fechaFin,
            @Schema(description = "Tipo de bloqueo", example = "VACACIONES", requiredMode = Schema.RequiredMode.REQUIRED)
            Tipo tipo,
            @Schema(description = "Comentario libre sobre el bloqueo", example = "Vacaciones de Navidad", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            String comentario) {}
}
