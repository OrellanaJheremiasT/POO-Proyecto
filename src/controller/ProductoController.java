package controller;

import java.util.List;
import model.*;
import util.Validador;

/**
 * Controlador de productos. Gestiona el CRUD, búsquedas y ordenamiento.
 * Valida que el precio sea positivo, el ID único y que la categoría
 * asignada exista antes de registrar o editar un producto.
 */

public class ProductoController {

    private final ProductoModel productoModelo;
    private final CategoriaModel categoriaModelo;
    private final LogModel logModelo;
    private final UsuarioController usuarioCtrl;

    public ProductoController(ProductoModel productoModelo, CategoriaModel categoriaModelo,
                               LogModel logModelo, UsuarioController usuarioCtrl) {
        this.productoModelo  = productoModelo;
        this.categoriaModelo = categoriaModelo;
        this.logModelo       = logModelo;
        this.usuarioCtrl     = usuarioCtrl;
    }

    public Resultado<Void> crearProducto(String id, String nombre, String precioStr,
                                          String categoriaId, String stockStr) {
        if (!puedeAcceder("CREAR_PRODUCTO")) return Resultado.error("Acceso denegado.");

        if (!Validador.esIdValido(id))        return Resultado.error("ID inválido.");
        if (!Validador.esNombreValido(nombre)) return Resultado.error("Nombre inválido.");

        Double precio = Validador.parseDouble(precioStr);
        if (precio == null || !Validador.esPrecioValido(precio))
            return Resultado.error("Precio inválido (debe ser un número positivo).");

        Integer stock = Validador.parseInt(stockStr);
        if (stock == null || !Validador.esStockValido(stock))
            return Resultado.error("Stock inválido (debe ser entero ≥ 0).");

        if (categoriaModelo.buscarPorId(categoriaId.trim()) == null)
            return Resultado.error("Categoría '" + categoriaId + "' no existe.");

        Producto nuevo = new Producto(id.trim(), nombre.trim(), precio, categoriaId.trim(), stock);
        if (!productoModelo.crear(nuevo))
            return Resultado.error("Ya existe un producto con el ID '" + id + "'.");

        logear("CREAR_PRODUCTO", "ID=" + id + " Nombre=" + nombre);
        return Resultado.ok(null, "Producto '" + nombre + "' registrado exitosamente.");
    }

    public Resultado<Void> editarProducto(String id, String nombre, String precioStr,
                                           String categoriaId, String stockStr) {
        if (!puedeAcceder("EDITAR_PRODUCTO")) return Resultado.error("Acceso denegado.");

        Producto existente = productoModelo.buscarPorId(id.trim());
        if (existente == null) return Resultado.error("Producto no encontrado.");

        // Permite actualizar solo campos provistos (no vacíos)
        String nuevoNombre   = (nombre != null && !nombre.isBlank()) ? nombre.trim() : existente.getNombre();
        double nuevoPrecio   = existente.getPrecio();
        String nuevaCatId    = (categoriaId != null && !categoriaId.isBlank()) ? categoriaId.trim() : existente.getCategoriaId();
        int nuevoStock       = existente.getStock();

        if (precioStr != null && !precioStr.isBlank()) {
            Double p = Validador.parseDouble(precioStr);
            if (p == null || !Validador.esPrecioValido(p)) return Resultado.error("Precio inválido.");
            nuevoPrecio = p;
        }
        if (stockStr != null && !stockStr.isBlank()) {
            Integer s = Validador.parseInt(stockStr);
            if (s == null || !Validador.esStockValido(s)) return Resultado.error("Stock inválido.");
            nuevoStock = s;
        }
        if (!nuevaCatId.equals(existente.getCategoriaId())
                && categoriaModelo.buscarPorId(nuevaCatId) == null)
            return Resultado.error("Categoría '" + nuevaCatId + "' no existe.");

        Producto actualizado = new Producto(id.trim(), nuevoNombre, nuevoPrecio, nuevaCatId, nuevoStock);
        productoModelo.actualizar(actualizado);
        logear("EDITAR_PRODUCTO", "ID=" + id);
        return Resultado.ok(null, "Producto actualizado exitosamente.");
    }

    public Resultado<Void> eliminarProducto(String id) {
        if (!puedeAcceder("ELIMINAR_PRODUCTO")) return Resultado.error("Acceso denegado.");
        if (productoModelo.buscarPorId(id.trim()) == null)
            return Resultado.error("Producto no encontrado.");
        productoModelo.eliminar(id.trim());
        logear("ELIMINAR_PRODUCTO", "ID=" + id);
        return Resultado.ok(null, "Producto eliminado.");
    }

    public Resultado<List<Producto>> listarProductos() {
        if (!puedeAcceder("LISTAR_PRODUCTOS")) return Resultado.error("Acceso denegado.");
        return Resultado.ok(productoModelo.listarTodos(), "");
    }

    public Resultado<List<Producto>> buscarPorNombre(String nombre) {
        if (!puedeAcceder("BUSCAR_PRODUCTO")) return Resultado.error("Acceso denegado.");
        List<Producto> resultado = productoModelo.buscarPorNombre(nombre.trim());
        return Resultado.ok(resultado, resultado.size() + " resultado(s) encontrado(s).");
    }

    public Resultado<List<Producto>> buscarPorCategoria(String categoriaId) {
        if (!puedeAcceder("BUSCAR_PRODUCTO")) return Resultado.error("Acceso denegado.");
        List<Producto> resultado = productoModelo.buscarPorCategoria(categoriaId.trim());
        return Resultado.ok(resultado, resultado.size() + " producto(s) en categoría '" + categoriaId + "'.");
    }

    public Resultado<List<Producto>> ordenarPor(String criterio) {
        if (!puedeAcceder("LISTAR_PRODUCTOS")) return Resultado.error("Acceso denegado.");
        List<Producto> lista;
        switch (criterio.toLowerCase()) {
            case "precio_asc":  lista = productoModelo.ordenarPorPrecioAsc();  break;
            case "precio_desc": lista = productoModelo.ordenarPorPrecioDesc(); break;
            case "nombre":      lista = productoModelo.ordenarPorNombre();     break;
            default: return Resultado.error("Criterio desconocido. Use: precio_asc, precio_desc, nombre");
        }
        return Resultado.ok(lista, "Ordenado por " + criterio);
    }

    private boolean puedeAcceder(String accion) {
        Usuario u = usuarioCtrl.getUsuarioActual();
        return u != null && u.tienePermiso(accion);
    }

    private void logear(String accion, String detalle) {
        Usuario u = usuarioCtrl.getUsuarioActual();
        if (u != null) logModelo.registrar(u.getId(), accion, detalle);
    }
}
