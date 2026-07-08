package model;

import java.util.*;
import java.util.stream.Collectors;
import util.CSVUtil;
import util.Rutas;
import util.Validador;

/**
 * Capa de acceso a datos para productos.
 * Además del CRUD básico, ofrece búsqueda por nombre y categoría,
 * y ordenamiento por precio o nombre usando Streams y Comparators.
 */

public class ProductoModel implements Persistible<Producto> {

    private List<Producto> cache;

    public ProductoModel() {
        this.cache = new ArrayList<>();
        cargarDesdeCSV();
    }

    private void cargarDesdeCSV() {
        cache.clear();
        for (String linea : CSVUtil.leerLineas(Rutas.PRODUCTOS)) {
            Producto p = Producto.fromCSV(linea);
            if (p != null) cache.add(p);
        }
    }

    private boolean guardarTodos() {
        List<String> lineas = cache.stream().map(Producto::toCSV).collect(Collectors.toList());
        return CSVUtil.escribirLineas(Rutas.PRODUCTOS, Rutas.ENC_PRODUCTOS, lineas);
    }

    @Override
    public boolean crear(Producto producto) {
        if (!Validador.esIdValido(producto.getId())) return false;
        if (!Validador.esNombreValido(producto.getNombre())) return false;
        if (!Validador.esPrecioValido(producto.getPrecio())) return false;
        if (!Validador.esStockValido(producto.getStock())) return false;
        if (buscarPorId(producto.getId()) != null) return false; // ID único
        cache.add(producto);
        return guardarTodos();
    }

    @Override
    public boolean eliminar(String id) {
        boolean removido = cache.removeIf(p -> p.getId().equalsIgnoreCase(id));
        if (removido) guardarTodos();
        return removido;
    }

    @Override
    public boolean actualizar(Producto producto) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equalsIgnoreCase(producto.getId())) {
                cache.set(i, producto);
                return guardarTodos();
            }
        }
        return false;
    }

    @Override
    public List<Producto> listarTodos() {
        return new ArrayList<>(cache);
    }

    @Override
    public Producto buscarPorId(String id) {
        return cache.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    /** Búsqueda por nombre (parcial, insensible a mayúsculas) */
    public List<Producto> buscarPorNombre(String nombre) {
        String lower = nombre.toLowerCase();
        return cache.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    /** Búsqueda por categoría */
    public List<Producto> buscarPorCategoria(String categoriaId) {
        return cache.stream()
                .filter(p -> p.getCategoriaId().equalsIgnoreCase(categoriaId))
                .collect(Collectors.toList());
    }

    /** ¿Existe algún producto asociado a esta categoría? */
    public boolean tieneProductosEnCategoria(String categoriaId) {
        return cache.stream().anyMatch(p -> p.getCategoriaId().equalsIgnoreCase(categoriaId));
    }

    /** Ordenamiento por precio ascendente */
    public List<Producto> ordenarPorPrecioAsc() {
        return cache.stream()
                .sorted(Comparator.comparingDouble(Producto::getPrecio))
                .collect(Collectors.toList());
    }

    /** Ordenamiento por precio descendente */
    public List<Producto> ordenarPorPrecioDesc() {
        return cache.stream()
                .sorted(Comparator.comparingDouble(Producto::getPrecio).reversed())
                .collect(Collectors.toList());
    }

    /** Ordenamiento por nombre alfabético */
    public List<Producto> ordenarPorNombre() {
        return cache.stream()
                .sorted(Comparator.comparing(p -> p.getNombre().toLowerCase()))
                .collect(Collectors.toList());
    }
}
