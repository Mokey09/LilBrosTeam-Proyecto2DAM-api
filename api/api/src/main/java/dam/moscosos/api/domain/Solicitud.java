
/*package dam.moscosos.api.domain;

import dam.moscosos.api.enums.Estado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "solicitudes")
public class Solicitud {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "estado")
    private Estado estado;

    @Column(name = "lectivo")
    private Boolean esLectivo;

    @Column(name = "fecha")
    private LocalDate fecha;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    private Profesor profesor;
}
*/