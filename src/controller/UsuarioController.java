package controller;

import java.util.List;
import model.*;
import util.Validador;

/**
 * Controlador de usuarios. Gestiona el login, logout y CRUD de usuarios.
 * Verifica permisos según el rol del usuario activo antes de cada operación
 * y registra todas las acciones en el log de auditoría.
 */

public class UsuarioController {

    private final UsuarioModel modelo;
    private final LogModel logModelo;
    private Usuario usuarioActual;

    public UsuarioController(UsuarioModel modelo, LogModel logModelo) {
        this.modelo = modelo;
        this.logModelo = logModelo;
    }

    // ─── Autenticación ──────────────────────────────────────────────────────

    public Resultado<Usuario> login(String id, String password) {
        if (id == null || id.isBlank() || password == null || password.isBlank())
            return Resultado.error("Credenciales vacías.");

        Usuario u = modelo.autenticar(id.trim(), password);
        if (u == null) {
            logModelo.registrar(id, "LOGIN_FALLIDO", "Credenciales incorrectas");
            return Resultado.error("ID o contraseña incorrectos.");
        }
        usuarioActual = u;
        logModelo.registrar(u.getId(), "LOGIN_OK", "Sesión iniciada");
        return Resultado.ok(u, "Bienvenido, " + u.getNombre() + " [" + u.getRol() + "]");
    }

    public void logout() {
        if (usuarioActual != null) {
            logModelo.registrar(usuarioActual.getId(), "LOGOUT", "Sesión cerrada");
            usuarioActual = null;
        }
    }

    public Usuario getUsuarioActual() { return usuarioActual; }

    public boolean hayUsuarioLogueado() { return usuarioActual != null; }

    // ─── CRUD Usuarios ───────────────────────────────────────────────────────

    public Resultado<Void> crearUsuario(String id, String nombre, String email,
                                        String password, String rol) {
        if (!verificarPermiso("CREAR_USUARIO")) return Resultado.error("Acceso denegado.");

        if (!Validador.esIdValido(id))        return Resultado.error("ID inválido (solo letras, números, _ -).");
        if (!Validador.esNombreValido(nombre)) return Resultado.error("Nombre demasiado corto.");
        if (!Validador.esEmailValido(email))   return Resultado.error("Email inválido.");
        if (!Validador.esPasswordValida(password)) return Resultado.error("Contraseña muy corta (mín. 4 chars).");

        Usuario.TipoRol tipoRol = Usuario.TipoRol.fromString(rol);
        String hash = Usuario.hashSimple(password);
        Usuario nuevo = new Usuario(id.trim(), nombre.trim(), email.trim(), hash, tipoRol, true);

        if (!modelo.crear(nuevo)) return Resultado.error("Ya existe un usuario con ese ID.");

        logModelo.registrar(usuarioActual.getId(), "CREAR_USUARIO", "ID=" + id);
        return Resultado.ok(null, "Usuario creado exitosamente.");
    }

    public Resultado<Void> eliminarUsuario(String id) {
        if (!verificarPermiso("ELIMINAR_USUARIO")) return Resultado.error("Acceso denegado.");
        if (usuarioActual.getId().equalsIgnoreCase(id))
            return Resultado.error("No puedes eliminar tu propia cuenta.");

        if (!modelo.eliminar(id.trim())) return Resultado.error("Usuario no encontrado.");
        logModelo.registrar(usuarioActual.getId(), "ELIMINAR_USUARIO", "ID=" + id);
        return Resultado.ok(null, "Usuario eliminado.");
    }

    public Resultado<List<Usuario>> listarUsuarios() {
        if (!verificarPermiso("LISTAR_USUARIOS")) return Resultado.error("Acceso denegado.");
        return Resultado.ok(modelo.listarTodos(), "");
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private boolean verificarPermiso(String accion) {
        return usuarioActual != null && usuarioActual.tienePermiso(accion);
    }

    /** Verifica si ya existe algún administrador en el sistema */
    public boolean existeAdmin() {
        return modelo.existeAlgunAdmin();
    }

    /** Creación inicial del primer admin (sin autenticación) */
    public boolean crearAdminInicial(String id, String nombre, String email, String password) {
        if (modelo.existeAlgunAdmin()) return false;
        String hash = Usuario.hashSimple(password);
        Usuario admin = new Usuario(id, nombre, email, hash, Usuario.TipoRol.ADMINISTRADOR, true);
        return modelo.crear(admin);
    }
}
