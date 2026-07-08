package model;

/**
 * Producto concreto que extiende ProductoBase agregando el atributo stock.
 * Implementa calcularValorTotal() como precio × stock y gestiona
 * su propia serialización y deserialización desde archivo CSV.
 */


public class Producto extends ProductoBase {
    private int stock;

    public Producto(String id, String nombre, double precio, String categoriaId, int stock) {
        super(id, nombre, precio, categoriaId);
        this.stock = stock;
    }

    @Override
    public double calcularValorTotal() {
        return precio * stock;
    }

    @Override
    public String getTipoProducto() {
        return "ESTANDAR";
    }

    public int getStock() { return stock; }
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
        this.stock = stock;
    }

    public String toCSV() {
        return String.join(",", id, nombre.replace(",", ";"),
                String.format("%.2f", precio), categoriaId, String.valueOf(stock));
    }

    public static Producto fromCSV(String linea) {
        String[] p = linea.split(",", -1);
        if (p.length < 5) return null;
        try {
            return new Producto(p[0].trim(), p[1].trim(),
                    Double.parseDouble(p[2].trim()), p[3].trim(),
                    Integer.parseInt(p[4].trim()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("%-8s %-25s S/%-10.2f %-10s Stock:%-5d ValorTotal:S/%.2f",
                id, nombre, precio, categoriaId, stock, calcularValorTotal());
    }
}
