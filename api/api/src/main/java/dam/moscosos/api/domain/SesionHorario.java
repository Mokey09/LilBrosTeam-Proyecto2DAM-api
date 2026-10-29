/*
package dam.moscosos.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "horarios")
public class SesionHorario {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(name = "dia_semana")
    private Integer diaSemana;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    private GrupoAlumnado grupoAlumnado;


    @Column(name = "cod_grupo")
    private String codGrupo;

    @Column(name = "id_profesor")
    private String idProfesor;

}
*/