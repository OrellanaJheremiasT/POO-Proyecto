package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import util.CSVUtil;
import util.Rutas;
import util.Validador;

/**
 * Capa de acceso a datos para usuarios.
 * Mantiene la lista de usuarios en memoria y la sincroniza
 * con el archivo usuarios.csv ante cada modificación.
 */

public class UsuarioModel implements Persistible<Usuario> {

    private List<Usuario> cache;

    public UsuarioModel() {
        this.cache = new ArrayList<>();
        cargarDesdeCSV();
    }

    private void cargarDesdeCSV() {
        cache.clear();
        for (String linea : CSVUtil.leerLineas(Rutas.USUARIOS)) {
            Usuario u = Usuario.fromCSV(linea);
            if (u != null) cache.add(u);
        }
    }

    private boolean guardarTodos() {
        List<String> lineas = cache.stream().map(Usuario::toCSV).collect(Collectors.toList());
        return CSVUtil.escribirLineas(Rutas.USUARIOS, Rutas.ENC_USUARIOS, lineas);
    }

    @Override
    public boolean crear(Usuario usuario) {
        if (!Validador.esIdValido(usuario.getId())) return false;
        if (!Validador.esNombreValido(usuario.getNombre())) return false;
        if (buscarPorId(usuario.getId()) != null) return false; // ID duplicado
        cache.add(usuario);
        return guardarTodos();
    }

    @Override
    public boolean eliminar(String id) {
        boolean removido = cache.removeIf(u -> u.getId().equalsIgnoreCase(id));
        if (removido) guardarTodos();
        return removido;
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equalsIgnoreCase(usuario.getId())) {
                cache.set(i, usuario);
                return guardarTodos();
            }
        }
        return false;
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(cache);
    }

    @Override
    public Usuario buscarPorId(String id) {
        return cache.stream()
                .filter(u -> u.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    public Usuario autenticar(String id, String password) {
        Usuario u = buscarPorId(id);
        if (u != null && u.autenticar(password)) return u;
        return null;
    }

    public boolean existeAlgunAdmin() {
        return cache.stream().anyMatch(u ->
                u.getTipoRol() == Usuario.TipoRol.ADMINISTRADOR && u.estaActivo());
    }
}
