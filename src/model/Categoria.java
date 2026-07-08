package model;

/**
 * Representa una categoría de productos con id, nombre y descripción.
 * Incluye métodos para serializar y deserializar sus datos en formato CSV.
 */


public class Categoria {
    private String id;
    private String nombre;
    private String descripcion;

    public Categoria(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String toCSV() {
        return String.join(",", id, nombre, descripcion.replace(",", ";"));
    }

    public static Categoria fromCSV(String linea) {
        String[] p = linea.split(",", 3);
        if (p.length < 3) return null;
        return new Categoria(p[0].trim(), p[1].trim(), p[2].trim());
    }

    @Override
    public String toString() {
        return String.format("%-8s %-20s %s", id, nombre, descripcion);
    }
}
