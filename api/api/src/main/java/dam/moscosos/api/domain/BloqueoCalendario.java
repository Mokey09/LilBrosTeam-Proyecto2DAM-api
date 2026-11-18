package dam.moscosos.api.domain;

import dam.moscosos.api.enums.Tipo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "bloqueo_calendario")
@Schema(description = "Día bloqueado del calendario (una fila por día)")
public class BloqueoCalendario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Schema(description = "Identificador único (autogenerado)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    // Un día por fila. unique = true impide repetidos también a nivel de base de datos
    @Column(name = "fecha", nullable = false, unique = true)
    @Schema(description = "Día bloqueado (único)", example = "2026-12-24")
    private LocalDate fecha;

    // Se guarda el nombre en vez de la posicion
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    @Schema(description = "Tipo de bloqueo", example = "VACACIONES")
    private Tipo tipo;

    @Column(name = "comentario")
    @Schema(description = "Comentario libre sobre el bloqueo", example = "Vacaciones de Navidad")
    private String comentario;
}
