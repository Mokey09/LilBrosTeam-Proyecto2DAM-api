package dam.moscosos.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;   // CAMBIO: import nuevo
import dam.moscosos.api.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "profesores")
@Schema(description = "Profesor del centro")
public class Profesor {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Identificador único (autogenerado)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(name = "nombre")
    @Schema(description = "Nombre del profesor", example = "Laura")
    private String nombre;

    @Column(name = "apellidos")
    @Schema(description = "Apellidos del profesor", example = "García Pérez")
    private String apellidos;

    @Column(name = "dni", unique = true)
    @Schema(description = "DNI del profesor (único)", example = "12345678A")
    private String dni;

    @Column(name = "email", unique = true)
    @Schema(description = "Correo electrónico (único), también se usa para iniciar sesión", example = "laura.garcia@centro.es")
    private String email;

    // CAMBIO: WRITE_ONLY -> la contraseña se acepta al RECIBIR JSON (crear/editar/login)
    // pero NUNCA se incluye al DEVOLVERLO. Antes, GET /profesores la mostraba en claro.
    // Lo que se guarda aquí es el hash BCrypt (ver ProfesorServiceImpl), nunca el texto original.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "contrasena", nullable = false)
    @Schema(description = "Contraseña en texto plano (se guarda cifrada con BCrypt y nunca se devuelve)", example = "1234", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String contrasena;

    @Column(name = "cp")
    @Schema(description = "Código postal", example = "46001")
    private Integer codigoPostal;

    @Column(name = "localidad")
    @Schema(description = "Localidad de residencia", example = "Valencia")
    private String localidad;

    @Column(name = "telefono")
    @Schema(description = "Teléfono de contacto", example = "600123456")
    private String telefono;

    @Column(name = "direccion")
    @Schema(description = "Dirección postal", example = "Calle Mayor 12")
    private String direccion;

    // TODO si es secundaria, etc (puede ser un enum?)
    @Column(name = "cuerpo")
    @Schema(description = "Cuerpo docente al que pertenece", example = "Profesor de Enseñanza Secundaria")
    private String cuerpo;

    // CAMBIO: antes sin @Enumerated, JPA guardaba la POSICIÓN (0,1,2). Si reordenabas el enum
    // cambiaban los permisos de todos. Ahora se guarda el nombre, como en BloqueoCalendario.
    // También: nullable = false y valor por defecto USUARIO, para que nunca haya un profesor sin rol
    // (ProfesorUserDetailsService lo necesita para construir los permisos).
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    @Schema(description = "Rol dentro de la aplicación (determina los permisos). Por defecto USUARIO", example = "USUARIO", defaultValue = "USUARIO")
    private Rol rol = Rol.USUARIO;

    @Column(name = "especialidad")
    @Schema(description = "Especialidad del profesor", example = "Programación")
    private String especialidad;

    @Column(name = "departamento")
    @Schema(description = "Departamento al que pertenece", example = "Informática")
    private String departamento;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(description = "Grupo de alumnado asociado al profesor")
    private GrupoAlumnado grupoAlumnado;


/*
    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private List<Solicitud> solcitudes = new ArrayList<>();

    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL)
    private List<SesionHorario> sesiones = new ArrayList<>();
    */
}
