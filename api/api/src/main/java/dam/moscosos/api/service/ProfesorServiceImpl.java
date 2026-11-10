package dam.moscosos.api.service;

import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorServiceImpl implements ProfesorService{


    @Autowired
    private ProfesorRepository profesorRepository;


    @Override
    public List<Profesor> findAll(){
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
        return profesorRepository.save(profesor);
    }

    @Override
    public Profesor update(Integer id, Profesor profesorActualizado) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado"));

        profesor.setNombre(profesorActualizado.getNombre());
        profesor.setApellidos(profesorActualizado.getApellidos());
        profesor.setDni(profesorActualizado.getDni());
        profesor.setEmail(profesorActualizado.getEmail());
        profesor.setContrasena(profesorActualizado.getContrasena());
        profesor.setCodigoPostal(profesorActualizado.getCodigoPostal());
        profesor.setLocalidad(profesorActualizado.getLocalidad());
        profesor.setTelefono(profesorActualizado.getTelefono());
        profesor.setDireccion(profesorActualizado.getDireccion());
        profesor.setCuerpo(profesorActualizado.getCuerpo());
        profesor.setRol(profesorActualizado.getRol());
        profesor.setEspecialidad(profesorActualizado.getEspecialidad());
        profesor.setDepartamento(profesorActualizado.getDepartamento());
        profesor.setGrupoAlumnado(profesorActualizado.getGrupoAlumnado());

        return profesorRepository.save(profesorActualizado);
    }

    @Override
    public void delete(Integer id) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado"));
        profesorRepository.delete(profesor);
    }
}
