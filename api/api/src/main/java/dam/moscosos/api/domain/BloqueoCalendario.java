package dam.moscosos.api.domain;

import dam.moscosos.api.enums.Tipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "bloqueo_calendario")
public class BloqueoCalendario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    // Un día por fila. unique = true impide repetidos también a nivel de base de datos
    @Column(name = "fecha", nullable = false, unique = true)
    private LocalDate fecha;

    // Se guarda el nombre en vez de la posicion
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private Tipo tipo;

    @Column(name = "comentario")
    private String comentario;
}