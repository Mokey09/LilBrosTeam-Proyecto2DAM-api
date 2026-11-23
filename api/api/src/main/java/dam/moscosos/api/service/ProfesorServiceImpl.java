package dam.moscosos.api.service;

import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;   // CAMBIO: import nuevo
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    // CAMBIO: nuevo. Es el BCryptPasswordEncoder definido en SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Profesor> findAll() {
        return profesorRepository.findAll();
    }

    @Override
    public Optional<Profesor> findById(Integer id) {
        return profesorRepository.findById(id);
    }

    @Override
    public Optional<Profesor> findByEmail(String email) {
        return profesorRepository.findByEmail(email);
    }

    @Override
    public Profesor save(Profesor profesor) {
        // CAMBIO: validamos y hasheamos la contraseña antes de guardar. Antes se guardaba tal cual llegaba.
        if (profesor.getContrasena() == null || profesor.getContrasena().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        }
        profesor.setContrasena(passwordEncoder.encode(profesor.getContrasena()));
        return profesorRepository.save(profesor);
    }

    @Override
    public Profesor update(Integer id, Profesor profesorDetalles) {
        // 1. Buscamos el profesor existente. Si no existe, devuelve un 404 Not Found limpio.
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + id));

        // 2. Actualizamos cada campo mapeando los datos nuevos (profesorDetalles) al objeto de la BD (profesor)
        profesor.setNombre(profesorDetalles.getNombre());
        profesor.setApellidos(profesorDetalles.getApellidos());
        profesor.setDni(profesorDetalles.getDni());
        profesor.setEmail(profesorDetalles.getEmail());

        // CAMBIO: antes se copiaba la contraseña directamente. Ahora, como GET ya no la devuelve,
        // un PUT típico (GET -> editar -> PUT) llegaría SIN contraseña y la borraría.
        // Solo se cambia si viene una nueva, y se guarda hasheada; si no, se conserva la actual.
        if (profesorDetalles.getContrasena() != null && !profesorDetalles.getContrasena().isBlank()) {
            profesor.setContrasena(passwordEncoder.encode(profesorDetalles.getContrasena()));
        }

        profesor.setCodigoPostal(profesorDetalles.getCodigoPostal());
        profesor.setLocalidad(profesorDetalles.getLocalidad());
        profesor.setTelefono(profesorDetalles.getTelefono());
        profesor.setDireccion(profesorDetalles.getDireccion());
        profesor.setCuerpo(profesorDetalles.getCuerpo());
        profesor.setRol(profesorDetalles.getRol());
        profesor.setEspecialidad(profesorDetalles.getEspecialidad());
        profesor.setDepartamento(profesorDetalles.getDepartamento());
        profesor.setGrupoAlumnado(profesorDetalles.getGrupoAlumnado());

        // 3. Guardamos los cambios en la base de datos
        return profesorRepository.save(profesor);
    }

    @Override
    public void cambiarContrasena(String email, String contrasenaActual, String contrasenaNueva) {
        Profesor profesor = profesorRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado"));

        // Se devuelve 400 (y no 401) a propósito: un 401 haría que el cliente crea que el token ha caducado y cierre la sesión
        if (!passwordEncoder.matches(contrasenaActual, profesor.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual no es correcta");
        }
        if (passwordEncoder.matches(contrasenaNueva, profesor.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña debe ser distinta de la actual");
        }

        profesor.setContrasena(passwordEncoder.encode(contrasenaNueva));
        profesorRepository.save(profesor);
    }

    @Override
    public void delete(Integer id) {
        if (!profesorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado para eliminar");
        }
        profesorRepository.deleteById(id);
    }
}
