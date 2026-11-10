package dam.moscosos.api.service;

import dam.moscosos.api.domain.GrupoAlumnado;
import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.repository.GrupoAlumnadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GrupoAlumnadoServiceImpl implements GrupoAlumnadoService {

    @Autowired
    private GrupoAlumnadoRepository grupoAlumnadoRepository;

    @Override
    public List<GrupoAlumnado> findAll() {
        return grupoAlumnadoRepository.findAll();
    }

    @Override
    public GrupoAlumnado findById(Integer codGrupo) {
        return grupoAlumnadoRepository.findById(codGrupo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe el grupo con código " + codGrupo));
    }

    @Override
    public GrupoAlumnado findByNombre(String nombreGrupo) {
        return grupoAlumnadoRepository.findByNombreGrupoIgnoreCase(nombreGrupo.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe el grupo '" + nombreGrupo + "'"));
    }

    @Override
    @Transactional
    public List<Profesor> findProfesores(Integer codGrupo) {
        // Dentro de la transacción para poder cargar la colección lazy
        return List.copyOf(findById(codGrupo).getProfesores());
    }

    @Override
    @Transactional
    public GrupoAlumnado crear(GrupoAlumnado grupo) {
        validarNombre(grupo.getNombreGrupo());
        String nombre = grupo.getNombreGrupo().trim();

        if (grupoAlumnadoRepository.existsByNombreGrupoIgnoreCase(nombre)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un grupo llamado '" + nombre + "'");
        }

        grupo.setCodGrupo(null); // siempre se crea uno nuevo, aunque el body traiga id
        grupo.setNombreGrupo(nombre);
        return grupoAlumnadoRepository.save(grupo);
    }

    @Override
    @Transactional
    public GrupoAlumnado actualizar(Integer codGrupo, GrupoAlumnado datos) {
        GrupoAlumnado existente = findById(codGrupo);

        validarNombre(datos.getNombreGrupo());
        String nombre = datos.getNombreGrupo().trim();

        // El nombre no puede coincidir con el de OTRO grupo
        grupoAlumnadoRepository.findByNombreGrupoIgnoreCase(nombre)
                .filter(otro -> !otro.getCodGrupo().equals(codGrupo))
                .ifPresent(otro -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un grupo llamado '" + nombre + "'");
                });

        existente.setNombreGrupo(nombre);
        existente.setIdProfesor(datos.getIdProfesor());
        return grupoAlumnadoRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer codGrupo) {
        grupoAlumnadoRepository.delete(findById(codGrupo));
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'nombreGrupo' es obligatorio");
        }
    }
}