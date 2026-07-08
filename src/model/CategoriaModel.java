package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import util.CSVUtil;
import util.Rutas;
import util.Validador;

/**
 * Capa de acceso a datos para categorías.
 * Mantiene la lista de categorías en memoria y la sincroniza
 * con el archivo categorias.csv ante cada modificación.
 */

public class CategoriaModel implements Persistible<Categoria> {

    private List<Categoria> cache;

    public CategoriaModel() {
        this.cache = new ArrayList<>();
        cargarDesdeCSV();
    }

    private void cargarDesdeCSV() {
        cache.clear();
        for (String linea : CSVUtil.leerLineas(Rutas.CATEGORIAS)) {
            Categoria c = Categoria.fromCSV(linea);
            if (c != null) cache.add(c);
        }
    }

    private boolean guardarTodos() {
        List<String> lineas = cache.stream().map(Categoria::toCSV).collect(Collectors.toList());
        return CSVUtil.escribirLineas(Rutas.CATEGORIAS, Rutas.ENC_CATEGORIAS, lineas);
    }

    @Override
    public boolean crear(Categoria categoria) {
        if (!Validador.esIdValido(categoria.getId())) return false;
        if (!Validador.esNombreValido(categoria.getNombre())) return false;
        if (buscarPorId(categoria.getId()) != null) return false;
        cache.add(categoria);
        return guardarTodos();
    }

    @Override
    public boolean eliminar(String id) {
        boolean removido = cache.removeIf(c -> c.getId().equalsIgnoreCase(id));
        if (removido) guardarTodos();
        return removido;
    }

    @Override
    public boolean actualizar(Categoria categoria) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equalsIgnoreCase(categoria.getId())) {
                cache.set(i, categoria);
                return guardarTodos();
            }
        }
        return false;
    }

    @Override
    public List<Categoria> listarTodos() {
        return new ArrayList<>(cache);
    }

    @Override
    public Categoria buscarPorId(String id) {
        return cache.stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    public Categoria buscarPorNombre(String nombre) {
        return cache.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst().orElse(null);
    }
}
