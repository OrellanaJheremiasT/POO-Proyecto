package model;

/**
 * Clase abstracta que representa a cualquier persona dentro del sistema.
 * Aplica abstracción definiendo atributos y comportamientos comunes
 * que deben implementar todas las subclases (Usuario, etc.).
 */

public abstract class Persona {
    protected String id;
    protected String nombre;
    protected String email;

    public Persona(String id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    // Métodos abstractos que cada subclase debe implementar
    public abstract String getRol();
    public abstract boolean tienePermiso(String accion);

    // Getters y setters (Encapsulamiento)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + nombre + " | Rol: " + getRol();
    }
}
