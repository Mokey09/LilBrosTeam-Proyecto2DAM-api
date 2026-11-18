package dam.moscosos.api.controller;

import dam.moscosos.api.domain.Profesor;
import dam.moscosos.api.enums.Rol;
import dam.moscosos.api.security.JwtService;
import dam.moscosos.api.service.ProfesorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticación", description = "Login con email y contraseña. Devuelve un token JWT que se envía en la cabecera Authorization: Bearer <token>")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ProfesorService profesorService;

    // Records aparte (como hiciste con BloqueoRequest) para no exponer la entidad Profesor
    @Schema(description = "Credenciales de acceso")
    public record LoginRequest(
            @Schema(description = "Email del profesor", example = "admin@test.com", requiredMode = Schema.RequiredMode.REQUIRED)
            String email,
            @Schema(description = "Contraseña en texto plano", example = "admin1234", requiredMode = Schema.RequiredMode.REQUIRED)
            String contrasena) {}

    @Schema(description = "Resultado de un login correcto")
    public record LoginResponse(
            @Schema(description = "Token JWT para las siguientes peticiones", example = "eyJhbGciOiJIUzI1NiJ9...")
            String token,
            @Schema(description = "ID del profesor", example = "1")
            Integer id,
            @Schema(description = "Nombre del profesor", example = "Admin")
            String nombre,
            @Schema(description = "Rol del profesor", example = "DIRECTOR")
            Rol rol) {}

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
    @Operation(summary = "Iniciar sesión",
            description = "Comprueba email y contraseña y devuelve un token JWT. Es el único endpoint público de la API.")
    @SecurityRequirements // endpoint público: no exige token
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login correcto"),
            @ApiResponse(responseCode = "400", description = "Falta el email o la contraseña", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content)
    })
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
    @Operation(summary = "Perfil del usuario autenticado",
            description = "Devuelve los datos del profesor dueño del token. Cualquier rol puede usarlo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos del profesor autenticado"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido o caducado", content = @Content)
    })
    @GetMapping("/me")
    public Profesor me(Authentication authentication) {
        return profesorService.findByEmail(authentication.getName()).orElseThrow();
    }
}
