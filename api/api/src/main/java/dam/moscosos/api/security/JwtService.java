package dam.moscosos.api.security;

import dam.moscosos.api.domain.Profesor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * NUEVO: crea y valida los tokens JWT.
 * El token lleva el email (subject), el rol y la fecha de caducidad, y va firmado
 * con la clave secreta: si alguien lo modifica, la validación falla.
 */
@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    // La clave y la duración se leen de application.properties (la clave viene de una variable de entorno)
    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMs = expirationMs;
    }

    /** Genera un token firmado para el profesor que acaba de hacer login. */
    public String generateToken(Profesor profesor) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(profesor.getEmail())
                .claim("rol", profesor.getRol().name())
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }

    /** Devuelve el email guardado en el token. Solo llamar si isValid() es true. */
    public String extractEmail(String token) {
        return parse(token).getSubject();
    }

    /** true si la firma es correcta y el token no ha caducado. */
    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // parseSignedClaims lanza excepción si la firma no coincide o el token ha caducado
    private Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
