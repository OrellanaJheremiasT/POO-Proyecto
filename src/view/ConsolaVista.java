package view;

import java.util.Scanner;

/**
 * Vista de consola que centraliza toda la entrada y salida del sistema.
 * Gestiona la lectura de datos del usuario, la validación de opciones
 * numéricas y la presentación de menús y mensajes con colores ANSI.
 */

public class ConsolaVista {

    private static final String RESET  = "\033[0m";
    private static final String VERDE  = "\033[0;32m";
    private static final String ROJO   = "\033[0;31m";
    private static final String CYAN   = "\033[0;36m";
    private static final String AMARILLO = "\033[0;33m";
    private static final String BOLD   = "\033[1m";

    private final Scanner scanner;

    public ConsolaVista() {
        this.scanner = new Scanner(System.in);
    }

    // ─── Entrada ────────────────────────────────────────────────────────────

    public String leerLinea(String prompt) {
        System.out.print(CYAN + prompt + RESET);
        return scanner.nextLine().trim();
    }

    public String leerPassword(String prompt) {
        System.out.print(CYAN + prompt + RESET);
        // Si hay consola real, ocultamos; si es redirección, leemos normal
        if (System.console() != null) {
            char[] pwd = System.console().readPassword();
            return pwd != null ? new String(pwd) : "";
        }
        return scanner.nextLine().trim();
    }

    public int leerOpcion(int min, int max) {
        while (true) {
            String entrada = leerLinea("  Opción [" + min + "-" + max + "]: ");
            try {
                int op = Integer.parseInt(entrada);
                if (op >= min && op <= max) return op;
            } catch (NumberFormatException ignored) {}
            mostrarError("Ingresa un número entre " + min + " y " + max + ".");
        }
    }

    // ─── Salida ─────────────────────────────────────────────────────────────

    public void mostrarExito(String msg) {
        System.out.println(VERDE + "✔ " + msg + RESET);
    }

    public void mostrarError(String msg) {
        System.out.println(ROJO + "✘ " + msg + RESET);
    }

    public void mostrarInfo(String msg) {
        System.out.println(AMARILLO + "ℹ " + msg + RESET);
    }

    public void mostrarLinea(String msg) {
        System.out.println(msg);
    }

    public void saltoLinea() {
        System.out.println();
    }

    public void mostrarSeparador() {
        System.out.println(CYAN + "─".repeat(70) + RESET);
    }

    public void mostrarTitulo(String titulo) {
        saltoLinea();
        System.out.println(BOLD + CYAN + "╔══ " + titulo.toUpperCase() + " ══╗" + RESET);
        mostrarSeparador();
    }

    public void mostrarMenu(String titulo, String[] opciones) {
        mostrarTitulo(titulo);
        for (int i = 0; i < opciones.length; i++) {
            System.out.printf("  %s%d%s. %s%n", BOLD, i + 1, RESET, opciones[i]);
        }
        mostrarSeparador();
    }

    public void mostrarEncabezadoTabla(String encabezado) {
        mostrarSeparador();
        System.out.println(BOLD + encabezado + RESET);
        mostrarSeparador();
    }

    public void cerrar() {
        scanner.close();
    }
}
