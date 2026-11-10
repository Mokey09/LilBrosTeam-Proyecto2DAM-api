package dam.moscosos.api.domain;

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

    @Column(name = "contrasena")
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

    // TODO roles -> dependiendo del rol tiene X permisos
    @Column(name = "rol")
    private Rol rol;

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





