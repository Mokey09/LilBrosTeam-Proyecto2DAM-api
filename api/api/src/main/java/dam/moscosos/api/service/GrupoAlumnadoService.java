package dam.moscosos.api.service;

import dam.moscosos.api.domain.GrupoAlumnado;
import dam.moscosos.api.domain.Profesor;

import java.util.List;

public interface GrupoAlumnadoService {
    List<GrupoAlumnado> findAll();
    GrupoAlumnado findById(Integer codGrupo);
    GrupoAlumnado findByNombre(String nombreGrupo);
    List<Profesor> findProfesores(Integer codGrupo);
    GrupoAlumnado crear(GrupoAlumnado grupo);
    GrupoAlumnado actualizar(Integer codGrupo, GrupoAlumnado datos);
    void eliminar(Integer codGrupo);
}