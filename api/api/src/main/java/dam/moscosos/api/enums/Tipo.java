package dam.moscosos.api.enums;

public enum Tipo {
    FESTIVO_NACIONAL,
    FESTIVO_AUTONOMICO,
    FESTIVO_LOCAL,
    VACACIONES,
    DIA_NO_LECTIVO,
    EVALUACION,
    PERIODO_EXAMENES,
    OTRO;

    public static boolean validarTipo(String valor) {
        try {
            Tipo tipo = Tipo.valueOf(valor.toUpperCase().trim());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
