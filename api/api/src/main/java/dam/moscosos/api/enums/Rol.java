package dam.moscosos.api.enums;

public enum Rol {
    // yo creo que estos 3 van bien
    USUARIO,
    GESTOR,
    DIRECTOR;

    public static boolean validarRol(String valor) {
        try {
            Rol rol = Rol.valueOf(valor.toUpperCase().trim());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
