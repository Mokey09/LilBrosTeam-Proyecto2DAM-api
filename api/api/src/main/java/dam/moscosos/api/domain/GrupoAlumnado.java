
package dam.moscosos.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "grupos_alumnado")
@Schema(description = "Grupo de alumnado")
public class GrupoAlumnado {
    // TODO lul LMAO
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_grupo")
    @Schema(description = "Código del grupo (autogenerado)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer codGrupo;

    @Column(name = "id_profesor")
    @Schema(description = "ID del profesor asociado al grupo", example = "1")
    private Integer idProfesor;

    @Column(name = "nombre_grupo")
    @Schema(description = "Nombre del grupo (obligatorio y único)", example = "1DAM")
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
