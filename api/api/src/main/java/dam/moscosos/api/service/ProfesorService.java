package dam.moscosos.api.service;

import dam.moscosos.api.domain.Profesor;

import java.util.List;
import java.util.Optional;

public interface ProfesorService {
        List<Profesor> findAll();
        Optional<Profesor> findById(Integer id);
        Optional<Profesor> findByEmail(String email);
        Profesor save(Profesor profesor);
        Profesor update(Integer id, Profesor profesor);
        void delete(Integer id);

}
