package controller;

/**
 * Clase genérica que encapsula la respuesta de cualquier operación del controlador.
 * Contiene un indicador de éxito, un mensaje descriptivo y los datos retornados,
 * facilitando el manejo de errores entre controlador y vista.
 */

public class Resultado<T> {
    private final boolean exito;
    private final String mensaje;
    private final T datos;

    private Resultado(boolean exito, String mensaje, T datos) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public static <T> Resultado<T> ok(T datos, String mensaje) {
        return new Resultado<>(true, mensaje, datos);
    }

    public static <T> Resultado<T> error(String mensaje) {
        return new Resultado<>(false, mensaje, null);
    }

    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }
    public T getDatos() { return datos; }

    @Override
    public String toString() {
        return (exito ? "[OK] " : "[ERROR] ") + mensaje;
    }
}
