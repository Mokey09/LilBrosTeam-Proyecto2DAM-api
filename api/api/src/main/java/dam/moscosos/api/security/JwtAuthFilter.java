package dam.moscosos.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * NUEVO: se ejecuta una vez por petición, ANTES de llegar a tus controllers.
 * Si la petición trae "Authorization: Bearer <token>" y el token es válido,
 * marca al usuario como autenticado. Si no, la petición sigue sin autenticar
 * y SecurityConfig decidirá si la ruta lo permite (normalmente devolverá 401).
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ProfesorUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtService.isValid(token)) {
                try {
                    // Se vuelve a cargar el usuario de la BD en cada petición, así que si un
                    // profesor se borra o cambia de rol, el efecto es inmediato (no hay que esperar a que caduque el token)
                    UserDetails user = userDetailsService.loadUserByUsername(jwtService.extractEmail(token));
                    var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (UsernameNotFoundException ignored) {
                    // El profesor ya no existe: se trata como no autenticado
                }
            }
        }

        chain.doFilter(request, response);
    }
}
