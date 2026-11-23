package dam.moscosos.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * NUEVO: configuración central de seguridad. Aquí se decide qué rol puede acceder a qué ruta.
 */
@Configuration
@EnableMethodSecurity // permite usar @PreAuthorize en métodos si más adelante quieres reglas más finas
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtFilter) throws Exception {
        http
                // CSRF protege sesiones con cookies; esta API es sin sesión y usa tokens, así que se desactiva
                .csrf(csrf -> csrf.disable())
                // No se crea sesión en el servidor: la identidad viaja en el token de cada petición
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // IMPORTANTE: Spring aplica la PRIMERA regla que coincida, así que el orden importa

                        // Único endpoint público
                        .requestMatchers("/auth/login").permitAll()

                        // Spring reenvía los errores (400, 404, 500...) a /error. Si no se permite, cualquier
                        // error se convierte en 401 y la app cree que el token ha caducado
                        .requestMatchers("/error").permitAll()

                        // Cambiar la propia contraseña: cualquier usuario autenticado (solo afecta a su cuenta, se saca del token)
                        .requestMatchers(HttpMethod.PUT, "/auth/contrasena").authenticated()

                        // Swagger UI y documentación OpenAPI (públicos para poder abrir la página; los endpoints siguen protegidos)
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Calendario: leer lo puede hacer cualquier usuario autenticado...
                        .requestMatchers(HttpMethod.GET, "/bloqueados/**").authenticated()
                        .requestMatchers("/bloqueados/**").hasAnyRole("GESTOR", "DIRECTOR")

                        // Sesiones: ver cualquiera autenticado, modificar solo GESTOR y DIRECTOR
                        .requestMatchers(HttpMethod.GET, "/sesiones/**").authenticated()
                        .requestMatchers("/sesiones/**").hasAnyRole("GESTOR", "DIRECTOR")

                        // Grupos: ver cualquiera autenticado, modificar solo GESTOR y DIRECTOR
                        .requestMatchers(HttpMethod.GET, "/grupos/**").authenticated()
                        .requestMatchers("/grupos/**").hasAnyRole("GESTOR", "DIRECTOR")

                        // Gestión de profesores: solo DIRECTOR
                        .requestMatchers("/profesores/**").hasRole("DIRECTOR")

                        // Cualquier otra ruta que añadas en el futuro exige estar autenticado
                        .anyRequest().authenticated())
                // Sin token o token inválido: responde 401 limpio (por defecto sería 403)
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                // Nuestro filtro JWT va antes del filtro estándar de usuario/contraseña
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** BCrypt añade sal automáticamente y es lento a propósito para dificultar ataques de fuerza bruta. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Lo usa AuthController para comprobar email + contraseña (usa ProfesorUserDetailsService y PasswordEncoder). */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}