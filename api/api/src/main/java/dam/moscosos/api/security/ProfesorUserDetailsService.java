package dam.moscosos.api.security;

import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * NUEVO: le explica a Spring Security cómo buscar un usuario.
 * Usamos el email como "username" y reutilizamos tu ProfesorRepository existente.
 */
@Service
public class ProfesorUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profesor p = profesorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(p.getEmail())
                .password(p.getContrasena())      // ya hasheada con BCrypt
                .roles(p.getRol().name())         // USUARIO -> ROLE_USUARIO, DIRECTOR -> ROLE_DIRECTOR...
                .build();
    }
}
