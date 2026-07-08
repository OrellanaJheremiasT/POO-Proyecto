package controller;

import java.util.List;
import model.*;
import util.Validador;

/**
 * Controlador de categorías. Gestiona la creación, eliminación y listado.
 * Valida que no se pueda eliminar una categoría que tenga productos
 * asociados, protegiendo la integridad referencial de los datos.
 */

public class CategoriaController {

    private final CategoriaModel categoriaModelo;
    private final ProductoModel productoModelo;
    private final LogModel logModelo;
    private final UsuarioController usuarioCtrl;

    public CategoriaController(CategoriaModel categoriaModelo, ProductoModel productoModelo,
                                LogModel logModelo, UsuarioController usuarioCtrl) {
        this.categoriaModelo = categoriaModelo;
        this.productoModelo  = productoModelo;
        this.logModelo       = logModelo;
        this.usuarioCtrl     = usuarioCtrl;
    }

    public Resultado<Void> crearCategoria(String id, String nombre, String descripcion) {
        if (!puedeAcceder("CREAR_CATEGORIA")) return Resultado.error("Acceso denegado.");
        if (!Validador.esIdValido(id))         return Resultado.error("ID inválido.");
        if (!Validador.esNombreValido(nombre))  return Resultado.error("Nombre inválido (mín. 2 chars).");

        Categoria nueva = new Categoria(id.trim(), nombre.trim(),
                descripcion == null ? "" : descripcion.trim());
        if (!categoriaModelo.crear(nueva)) return Resultado.error("Ya existe una categoría con ese ID.");

        logear("CREAR_CATEGORIA", "ID=" + id + " Nombre=" + nombre);
        return Resultado.ok(null, "Categoría '" + nombre + "' creada exitosamente.");
    }

    public Resultado<Void> eliminarCategoria(String id) {
        if (!puedeAcceder("ELIMINAR_CATEGORIA")) return Resultado.error("Acceso denegado.");
        if (categoriaModelo.buscarPorId(id.trim()) == null)
            return Resultado.error("Categoría no encontrada.");
        if (productoModelo.tieneProductosEnCategoria(id.trim()))
            return Resultado.error("No se puede eliminar: existen productos en esta categoría.");

        categoriaModelo.eliminar(id.trim());
        logear("ELIMINAR_CATEGORIA", "ID=" + id);
        return Resultado.ok(null, "Categoría eliminada.");
    }

    public Resultado<List<Categoria>> listarCategorias() {
        if (!puedeAcceder("LISTAR_CATEGORIAS")) return Resultado.error("Acceso denegado.");
        return Resultado.ok(categoriaModelo.listarTodos(), "");
    }

    public Categoria buscarPorId(String id) {
        return categoriaModelo.buscarPorId(id);
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
