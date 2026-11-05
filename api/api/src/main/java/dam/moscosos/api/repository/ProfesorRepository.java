package dam.moscosos.api.repository;

import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfesorRepository extends JpaRepository<Profesor,Integer> {
    List<Profesor> findAll();
    Optional<Profesor> findById(Integer id);
    Optional<Profesor> findByDni(String dni);
    Optional<Profesor> findByEmail(String email);
    List<Profesor> findByNombreAndApellidos(String nombre, String apellidos);
    List<Profesor> findByNombreOrApellidos(String nombre, String apellidos); //se puede dejar uno vacio???????!?!?!?"!?!"??!
    List<Profesor> findByLocalidad(String localidad);
    List<Profesor> findByTelefono(String telefono);
    List<Profesor> findByCuerpo(String cuerpo); // que era cuerpo?!?!?!?!?!?!??!?!
    List<Profesor> findByRol(Rol rol);
    List<Profesor> findByEspecialidad(String especialidad);
    List<Profesor> findByDepartamento(String departamento);
}
