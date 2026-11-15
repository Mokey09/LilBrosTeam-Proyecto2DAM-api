package dam.moscosos.api.controller;

import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.enums.Rol;
import dam.moscosos.api.security.JwtService;
import dam.moscosos.api.service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * NUEVO: endpoints de autenticación.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ProfesorService profesorService;

    // Records aparte (como hiciste con BloqueoRequest) para no exponer la entidad Profesor
    public record LoginRequest(String email, String contrasena) {}
    public record LoginResponse(String token, Integer id, String nombre, Rol rol) {}

    /* POST http://localhost:8080/auth/login
        BODY
        {
          "email": "admin@test.com",
          "contrasena": "admin1234"
        }
        RESPUESTA 200: {
                            "token": "eyJhbGciOiJIUzI1NiJ9...........",
                            "id": 1,
                            "nombre": "Admin",
                            "rol": "DIRECTOR"
                        }
        RESPUESTA 401: credenciales incorrectas
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        if (request.email() == null || request.contrasena() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email y contraseña son obligatorios");
        }

        try {
            // Spring busca el profesor por email y compara la contraseña con el hash BCrypt
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.contrasena()));
        } catch (AuthenticationException e) {
            // Mismo mensaje si falla el email o la contraseña, para no revelar qué emails existen
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        Profesor profesor = profesorService.findByEmail(request.email()).orElseThrow();
        return new LoginResponse(
                jwtService.generateToken(profesor),
                profesor.getId(),
                profesor.getNombre(),
                profesor.getRol());
    }

    // GET http://localhost:8080/auth/me   (header: Authorization: Bearer <token>)
    // Devuelve los datos del profesor dueño del token. Sirve para que la app cargue su perfil
    // sin necesitar permisos de DIRECTOR (que son los que exige /profesores/**).
    @GetMapping("/me")
    public Profesor me(Authentication authentication) {
        return profesorService.findByEmail(authentication.getName()).orElseThrow();
    }
}
