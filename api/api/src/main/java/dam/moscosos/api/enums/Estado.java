package dam.moscosos.api.enums;

public enum Estado {
    ACEPTADO,
    RECHAZADO;

    public static boolean validarEstado(String valor) {
        try {
            Estado estado = Estado.valueOf(valor.toUpperCase().trim());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
