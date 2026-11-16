package dam.moscosos.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;   // CAMBIO: import nuevo
import dam.moscosos.api.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "profesores")
public class Profesor {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "dni", unique = true)
    private String dni;

    @Column(name = "email", unique = true)
    private String email;

    // CAMBIO: WRITE_ONLY -> la contraseña se acepta al RECIBIR JSON (crear/editar/login)
    // pero NUNCA se incluye al DEVOLVERLO. Antes, GET /profesores la mostraba en claro.
    // Lo que se guarda aquí es el hash BCrypt (ver ProfesorServiceImpl), nunca el texto original.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "cp")
    private Integer codigoPostal;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    // TODO si es secundaria, etc (puede ser un enum?)
    @Column(name = "cuerpo")
    private String cuerpo;

    // CAMBIO: antes sin @Enumerated, JPA guardaba la POSICIÓN (0,1,2). Si reordenabas el enum
    // cambiaban los permisos de todos. Ahora se guarda el nombre, como en BloqueoCalendario.
    // También: nullable = false y valor por defecto USUARIO, para que nunca haya un profesor sin rol
    // (ProfesorUserDetailsService lo necesita para construir los permisos).
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol = Rol.USUARIO;

    @Column(name = "especialidad")
    private String especialidad;

    @Column(name = "departamento")
    private String departamento;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    private GrupoAlumnado grupoAlumnado;


/*
    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private List<Solicitud> solcitudes = new ArrayList<>();

    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL)
    private List<SesionHorario> sesiones = new ArrayList<>();
    */
}
