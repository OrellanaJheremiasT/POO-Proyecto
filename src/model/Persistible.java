package model;

import java.util.List;

/**
 * Interface genérica que define el contrato CRUD para cualquier entidad.
 * Toda clase Model debe implementarla garantizando las operaciones
 * de crear, eliminar, actualizar, listar y buscar por ID.
 */

public interface Persistible<T> {
    boolean crear(T entidad);
    boolean eliminar(String id);
    boolean actualizar(T entidad);
    List<T> listarTodos();
    T buscarPorId(String id);
}
