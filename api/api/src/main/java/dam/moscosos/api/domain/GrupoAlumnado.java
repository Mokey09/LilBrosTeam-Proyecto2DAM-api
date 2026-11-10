
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
    @Column(name = "cod_grupo")
    private Integer codGrupo;

    @Column(name = "id_profesor")
    private Integer idProfesor;

    @Column(name = "nombre_grupo")
    private String nombreGrupo;

    // Relaciones

    @OneToMany(mappedBy = "grupoAlumnado", cascade = CascadeType.ALL)
    private List<SesionHorario> sesiones = new ArrayList<>();

    @OneToMany(mappedBy = "grupoAlumnado", cascade = CascadeType.ALL)
    private List<Profesor> profesores = new ArrayList<>();
}
