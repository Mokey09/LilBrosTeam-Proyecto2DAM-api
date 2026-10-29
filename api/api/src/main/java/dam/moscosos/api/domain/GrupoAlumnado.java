/*
package dam.moscosos.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "grupos_alumnado")
public class GrupoAlumnado {
    // TODO lul LMAO
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    // Relaciones

    @OneToMany(mappedBy = "Sesion Horario", cascade = CascadeType.ALL)
    private List<SesionHorario> sesiones = new ArrayList<>();
}
*/