package dam.moscosos.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Moscosos",
                version = "0.0.1",
                description = "API REST para la gestión de profesores, grupos, horarios y calendario de días bloqueados (proyecto 2DAM). "
                        + "Para probar los endpoints protegidos: haz login en /auth/login, copia el token y pulsa «Authorize»."),
        // Por defecto todos los endpoints exigen el token JWT (los públicos lo desactivan con @SecurityRequirements())
        security = @SecurityRequirement(name = OpenApiConfig.BEARER)
)
@SecurityScheme(
        name = OpenApiConfig.BEARER,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Token JWT obtenido en POST /auth/login (se envía como «Authorization: Bearer <token>»)"
)
public class OpenApiConfig {

    public static final String BEARER = "bearerAuth";
}
