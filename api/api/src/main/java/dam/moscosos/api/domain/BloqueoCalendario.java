package dam.moscosos.api.domain;

import dam.moscosos.api.enums.Tipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "bloqueo_calendario")
public class BloqueoCalendario {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "tipo")
    private Tipo tipo;

    // TODO esto hay que verlo
    @Column(name = "comentario")
    private String comentario;

    // TODO aqui o en servicio?
    /*public ArrayList<LocalDate> bloquearCalendario(LocalDate fechaInicio, LocalDate fechaFin) {
        ArrayList<LocalDate> diasBloqueados = new ArrayList<>();

        for (LocalDate fecha = fechaInicio; fecha.isBefore(fechaFin); fecha.plusDays(1)) {
            diasBloqueados.add(fecha);
        }

        return diasBloqueados;
    }*/

    // Si fuera en servicio?
    public void bloquearCalendario(BloqueoCalendario calendario) {
        for (LocalDate fecha = calendario.fechaInicio; fecha.isBefore(calendario.fechaFin); fecha.plusDays(1)) {
            // False a la base de datos XD
        }
    }

    public boolean comprobarDiaBloqueado(LocalDate dia) {
        // Si devuelve 'true', es un dia bloqueado
        return dia.isAfter(fechaInicio) && dia.isBefore(fechaFin);
    }

    /* posibilidad
    public BloqueoCalendario bloquear(LocalDate inicio, LocalDate fin, Tipo tipo, String comentario) {
    if (inicio == null || fin == null || fin.isBefore(inicio)) {
        throw new IllegalArgumentException("Rango de fechas no válido");
    }
    if (rangoBloqueado(inicio, fin)) {
        throw new BloqueoSolapadoException("Alguno de los días ya está bloqueado");
    }

    return repo.save(new BloqueoCalendario(null, inicio, fin, tipo, comentario));
    }
     */

    //Dias de locura : I
}
