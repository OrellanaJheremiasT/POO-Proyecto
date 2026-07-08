package model;

/**
 * Clase abstracta base para cualquier tipo de producto.
 * Define los atributos comunes (id, nombre, precio, categoría) y obliga
 * a las subclases a implementar calcularValorTotal() y getTipoProducto().
 */


public abstract class ProductoBase {
    protected String id;
    protected String nombre;
    protected double precio;
    protected String categoriaId;

    public ProductoBase(String id, String nombre, double precio, String categoriaId) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoriaId = categoriaId;
    }

    // Método abstracto: cada subclase define cómo calcular su valor total
    public abstract double calcularValorTotal();

    // Método abstracto: tipo de producto
    public abstract String getTipoProducto();

    // Getters y Setters (Encapsulamiento)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) {
        if (precio < 0) throw new IllegalArgumentException("El precio no puede ser negativo.");
        this.precio = precio;
    }

    public String getCategoriaId() { return categoriaId; }
    public void setCategoriaId(String categoriaId) { this.categoriaId = categoriaId; }
}
