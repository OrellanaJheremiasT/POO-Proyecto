package util;

/**
 * Clase utilitaria con métodos estáticos de validación reutilizables.
 * Centraliza las reglas de validación para ID, nombre, precio,
 * stock, email y contraseña usadas en toda la aplicación.
 */

public class Validador {

    public static boolean esIdValido(String id) {
        return id != null && !id.trim().isEmpty() && id.matches("[A-Za-z0-9_-]+");
    }

    public static boolean esPrecioValido(double precio) {
        return precio > 0;
    }

    public static boolean esStockValido(int stock) {
        return stock >= 0;
    }

    public static boolean esNombreValido(String nombre) {
        return nombre != null && nombre.trim().length() >= 2;
    }

    public static boolean esEmailValido(String email) {
        return email != null && email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean esPasswordValida(String password) {
        return password != null && password.length() >= 4;
    }

    /** Convierte cadena a double de forma segura */
    public static Double parseDouble(String s) {
        if (s == null) return null;
        try { return Double.parseDouble(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    /** Convierte cadena a int de forma segura */
    public static Integer parseInt(String s) {
        if (s == null) return null;
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }
}
