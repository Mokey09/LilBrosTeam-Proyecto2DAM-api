
package dam.moscosos.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "horarios")
@Schema(description = "Sesión del horario de un profesor con un grupo")
public class SesionHorario {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Identificador único (autogenerado)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(name = "hora_inicio")
    @Schema(description = "Hora de inicio de la sesión", example = "08:30:00", type = "string", format = "time")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    @Schema(description = "Hora de fin de la sesión", example = "09:25:00", type = "string", format = "time")
    private LocalTime horaFin;

    @Column(name = "dia_semana")
    @Schema(description = "Día de la semana (1 = lunes ... 5 = viernes)", example = "1")
    private Integer diaSemana;

    @Column(name = "cod_grupo")
    @Schema(description = "Código del grupo de alumnado", example = "DAW1")
    private String codGrupo;

    @Column(name = "id_profesor")
    @Schema(description = "Identificador del profesor que imparte la sesión", example = "12345678A")
    private String idProfesor;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(description = "Profesor que imparte la sesión")
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(description = "Grupo de alumnado de la sesión")
    private GrupoAlumnado grupoAlumnado;
}
