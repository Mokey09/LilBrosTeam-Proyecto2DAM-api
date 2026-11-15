package dam.moscosos.api.config;

import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.enums.Rol;
import dam.moscosos.api.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * NUEVO (OPCIONAL): crea el primer DIRECTOR al arrancar.
 * Hace falta porque /profesores/** exige rol DIRECTOR: sin ningún director en la BD
 * nadie podría entrar para crear al resto. Solo actúa si la tabla está vacía y si
 * se han definido ADMIN_EMAIL y ADMIN_PASSWORD como variables de entorno.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (profesorRepository.count() > 0 || adminEmail.isBlank() || adminPassword.isBlank()) {
            return;
        }
        Profesor director = new Profesor();
        director.setNombre("Admin");
        director.setApellidos("Director");
        director.setEmail(adminEmail);
        director.setContrasena(passwordEncoder.encode(adminPassword));
        director.setRol(Rol.DIRECTOR);
        profesorRepository.save(director);
        System.out.println("Creado el primer DIRECTOR: " + adminEmail);
    }
}
