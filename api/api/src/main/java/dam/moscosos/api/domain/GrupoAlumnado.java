
package dam.moscosos.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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

    // esto evita bucles de peticion profesor-sesion y tambien tiene exclusiones lombok
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "grupoAlumnado", cascade = CascadeType.ALL)
    private List<SesionHorario> sesiones = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "grupoAlumnado")
    private List<Profesor> profesores = new ArrayList<>();
}
