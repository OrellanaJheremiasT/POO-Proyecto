package view;

import controller.*;
import java.util.List;
import model.*;

/**
 * Punto de entrada principal del sistema (contiene el método main).
 * Instancia todos los modelos, controladores y la vista, y orquesta
 * la navegación entre menús siguiendo el patrón MVC.
 */

public class App {

    private final ConsolaVista vista;
    private final UsuarioController usuarioCtrl;
    private final CategoriaController categoriaCtrl;
    private final ProductoController productoCtrl;
    private final LogModel logModelo;

    public App() {
        // ── Instanciar Modelos ──────────────────────────────────────────────
        UsuarioModel  usuarioModelo  = new UsuarioModel();
        CategoriaModel catModelo     = new CategoriaModel();
        ProductoModel  prodModelo    = new ProductoModel();
        logModelo = new LogModel();

        // ── Instanciar Controladores ────────────────────────────────────────
        usuarioCtrl   = new UsuarioController(usuarioModelo, logModelo);
        categoriaCtrl = new CategoriaController(catModelo, prodModelo, logModelo, usuarioCtrl);
        productoCtrl  = new ProductoController(prodModelo, catModelo, logModelo, usuarioCtrl);

        // ── Vista ───────────────────────────────────────────────────────────
        vista = new ConsolaVista();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  INICIO
    // ════════════════════════════════════════════════════════════════════════

    public void iniciar() {
        mostrarBienvenida();
        configurarSiEsNecesario();

        boolean corriendo = true;
        while (corriendo) {
            if (!usuarioCtrl.hayUsuarioLogueado()) {
                corriendo = menuLogin();
            } else {
                menuPrincipal();
            }
        }
        vista.mostrarInfo("Sistema cerrado. ¡Hasta luego!");
        vista.cerrar();
    }

    private void mostrarBienvenida() {
        System.out.println("\033[1;35m");
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║    SISTEMA DE GESTIÓN DE PRODUCTOS           ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");
        System.out.println("\033[0m");
    }

    private void configurarSiEsNecesario() {
        if (!usuarioCtrl.existeAdmin()) {
            vista.mostrarInfo("Primera ejecución detectada. Configura el administrador inicial.");
            vista.mostrarSeparador();
            String id     = vista.leerLinea("ID de administrador: ");
            String nombre = vista.leerLinea("Nombre completo   : ");
            String email  = vista.leerLinea("Email             : ");
            String pass   = vista.leerPassword("Contraseña        : ");

            if (usuarioCtrl.crearAdminInicial(id, nombre, email, pass)) {
                vista.mostrarExito("Administrador creado. Ahora inicia sesión.");
            } else {
                vista.mostrarError("Error al crear administrador. Verifica el archivo CSV.");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  MENÚ LOGIN
    // ════════════════════════════════════════════════════════════════════════

    private boolean menuLogin() {
        vista.mostrarMenu("INICIO DE SESIÓN", new String[]{"Iniciar sesión", "Salir del sistema"});
        int op = vista.leerOpcion(1, 2);
        if (op == 2) return false;

        String id   = vista.leerLinea("ID de usuario: ");
        String pass = vista.leerPassword("Contraseña   : ");

        Resultado<Usuario> res = usuarioCtrl.login(id, pass);
        if (res.isExito()) {
            vista.mostrarExito(res.getMensaje());
        } else {
            vista.mostrarError(res.getMensaje());
        }
        return true;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  MENÚ PRINCIPAL
    // ════════════════════════════════════════════════════════════════════════

    private void menuPrincipal() {
        Usuario u = usuarioCtrl.getUsuarioActual();
        vista.mostrarTitulo("Menú Principal — " + u.getNombre() + " [" + u.getRol() + "]");
        System.out.println("  1. Gestión de Usuarios");
        System.out.println("  2. Gestión de Categorías");
        System.out.println("  3. Gestión de Productos");
        System.out.println("  4. Ver Logs del Sistema");
        System.out.println("  5. Cerrar Sesión");
        vista.mostrarSeparador();

        int op = vista.leerOpcion(1, 5);
        switch (op) {
            case 1: menuUsuarios();    break;
            case 2: menuCategorias();  break;
            case 3: menuProductos();   break;
            case 4: verLogs();         break;
            case 5: usuarioCtrl.logout();
                    vista.mostrarInfo("Sesión cerrada."); break;
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  GESTIÓN USUARIOS
    // ════════════════════════════════════════════════════════════════════════

    private void menuUsuarios() {
        vista.mostrarMenu("GESTIÓN DE USUARIOS",
                new String[]{"Crear usuario", "Eliminar usuario", "Listar usuarios", "Volver"});
        int op = vista.leerOpcion(1, 4);
        switch (op) {
            case 1: crearUsuario();   break;
            case 2: eliminarUsuario();break;
            case 3: listarUsuarios(); break;
            case 4: break;
        }
    }

    private void crearUsuario() {
        vista.mostrarTitulo("Crear Usuario");
        String id     = vista.leerLinea("ID de usuario  : ");
        String nombre = vista.leerLinea("Nombre completo: ");
        String email  = vista.leerLinea("Email          : ");
        String pass   = vista.leerPassword("Contraseña     : ");
        vista.mostrarInfo("Roles disponibles: ADMINISTRADOR, ESTANDAR");
        String rol    = vista.leerLinea("Rol            : ");

        Resultado<Void> res = usuarioCtrl.crearUsuario(id, nombre, email, pass, rol);
        if (res.isExito()) vista.mostrarExito(res.getMensaje());
        else               vista.mostrarError(res.getMensaje());
    }

    private void eliminarUsuario() {
        vista.mostrarTitulo("Eliminar Usuario");
        listarUsuarios();
        String id = vista.leerLinea("ID del usuario a eliminar: ");
        Resultado<Void> res = usuarioCtrl.eliminarUsuario(id);
        if (res.isExito()) vista.mostrarExito(res.getMensaje());
        else               vista.mostrarError(res.getMensaje());
    }

    private void listarUsuarios() {
        Resultado<List<Usuario>> res = usuarioCtrl.listarUsuarios();
        if (!res.isExito()) { vista.mostrarError(res.getMensaje()); return; }
        List<Usuario> lista = res.getDatos();
        vista.mostrarEncabezadoTabla(
                String.format("%-8s %-20s %-25s %-15s %s", "ID", "NOMBRE", "EMAIL", "ROL", "ESTADO"));
        if (lista.isEmpty()) { vista.mostrarInfo("No hay usuarios registrados."); return; }
        lista.forEach(u -> vista.mostrarLinea(u.toString()));
        vista.mostrarSeparador();
        vista.mostrarInfo("Total: " + lista.size() + " usuario(s).");
    }

    // ════════════════════════════════════════════════════════════════════════
    //  GESTIÓN CATEGORÍAS
    // ════════════════════════════════════════════════════════════════════════

    private void menuCategorias() {
        vista.mostrarMenu("GESTIÓN DE CATEGORÍAS",
                new String[]{"Crear categoría", "Eliminar categoría", "Listar categorías", "Volver"});
        int op = vista.leerOpcion(1, 4);
        switch (op) {
            case 1: crearCategoria();   break;
            case 2: eliminarCategoria();break;
            case 3: listarCategorias(); break;
            case 4: break;
        }
    }

    private void crearCategoria() {
        vista.mostrarTitulo("Crear Categoría");
        String id   = vista.leerLinea("ID de categoría  : ");
        String nom  = vista.leerLinea("Nombre           : ");
        String desc = vista.leerLinea("Descripción      : ");
        Resultado<Void> res = categoriaCtrl.crearCategoria(id, nom, desc);
        if (res.isExito()) vista.mostrarExito(res.getMensaje());
        else               vista.mostrarError(res.getMensaje());
    }

    private void eliminarCategoria() {
        vista.mostrarTitulo("Eliminar Categoría");
        listarCategorias();
        String id = vista.leerLinea("ID de categoría a eliminar: ");
        Resultado<Void> res = categoriaCtrl.eliminarCategoria(id);
        if (res.isExito()) vista.mostrarExito(res.getMensaje());
        else               vista.mostrarError(res.getMensaje());
    }

    private void listarCategorias() {
        Resultado<List<Categoria>> res = categoriaCtrl.listarCategorias();
        if (!res.isExito()) { vista.mostrarError(res.getMensaje()); return; }
        List<Categoria> lista = res.getDatos();
        vista.mostrarEncabezadoTabla(String.format("%-8s %-20s %s", "ID", "NOMBRE", "DESCRIPCIÓN"));
        if (lista.isEmpty()) { vista.mostrarInfo("No hay categorías registradas."); return; }
        lista.forEach(c -> vista.mostrarLinea(c.toString()));
        vista.mostrarSeparador();
        vista.mostrarInfo("Total: " + lista.size() + " categoría(s).");
    }

    // ════════════════════════════════════════════════════════════════════════
    //  GESTIÓN PRODUCTOS
    // ════════════════════════════════════════════════════════════════════════

    private void menuProductos() {
        vista.mostrarMenu("GESTIÓN DE PRODUCTOS", new String[]{
                "Registrar producto", "Editar producto", "Eliminar producto",
                "Listar productos", "Buscar por nombre", "Buscar por categoría",
                "Ordenar productos", "Volver"
        });
        int op = vista.leerOpcion(1, 8);
        switch (op) {
            case 1: crearProducto();       break;
            case 2: editarProducto();      break;
            case 3: eliminarProducto();    break;
            case 4: listarProductos(productoCtrl.listarProductos()); break;
            case 5: buscarPorNombre();     break;
            case 6: buscarPorCategoria();  break;
            case 7: ordenarProductos();    break;
            case 8: break;
        }
    }

    private void crearProducto() {
        vista.mostrarTitulo("Registrar Producto");
        listarCategorias();
        String id    = vista.leerLinea("ID del producto: ");
        String nom   = vista.leerLinea("Nombre         : ");
        String precio= vista.leerLinea("Precio (S/)    : ");
        String catId = vista.leerLinea("ID Categoría   : ");
        String stock = vista.leerLinea("Stock          : ");

        Resultado<Void> res = productoCtrl.crearProducto(id, nom, precio, catId, stock);
        if (res.isExito()) vista.mostrarExito(res.getMensaje());
        else               vista.mostrarError(res.getMensaje());
    }

    private void editarProducto() {
        vista.mostrarTitulo("Editar Producto");
        listarProductos(productoCtrl.listarProductos());
        String id = vista.leerLinea("ID del producto a editar: ");
        vista.mostrarInfo("Deja en blanco los campos que no desees modificar.");
        String nom   = vista.leerLinea("Nuevo nombre    : ");
        String precio= vista.leerLinea("Nuevo precio    : ");
        String catId = vista.leerLinea("Nueva categoría : ");
        String stock = vista.leerLinea("Nuevo stock     : ");

        Resultado<Void> res = productoCtrl.editarProducto(
                id,
                nom.isBlank()   ? null : nom,
                precio.isBlank()? null : precio,
                catId.isBlank() ? null : catId,
                stock.isBlank() ? null : stock);
        if (res.isExito()) vista.mostrarExito(res.getMensaje());
        else               vista.mostrarError(res.getMensaje());
    }

    private void eliminarProducto() {
        vista.mostrarTitulo("Eliminar Producto");
        listarProductos(productoCtrl.listarProductos());
        String id = vista.leerLinea("ID del producto a eliminar: ");
        Resultado<Void> res = productoCtrl.eliminarProducto(id);
        if (res.isExito()) vista.mostrarExito(res.getMensaje());
        else               vista.mostrarError(res.getMensaje());
    }

    private void listarProductos(Resultado<List<Producto>> res) {
        if (!res.isExito()) { vista.mostrarError(res.getMensaje()); return; }
        List<Producto> lista = res.getDatos();
        vista.mostrarEncabezadoTabla(String.format(
                "%-8s %-25s %-12s %-10s %-8s %s", "ID", "NOMBRE", "PRECIO", "CATEGORÍA", "STOCK", "VALOR TOTAL"));
        if (lista.isEmpty()) { vista.mostrarInfo("No hay productos registrados."); return; }
        lista.forEach(p -> vista.mostrarLinea(p.toString()));
        vista.mostrarSeparador();
        vista.mostrarInfo("Total: " + lista.size() + " producto(s).");
        if (!res.getMensaje().isBlank()) vista.mostrarInfo(res.getMensaje());
    }

    private void buscarPorNombre() {
        String termino = vista.leerLinea("Buscar por nombre: ");
        listarProductos(productoCtrl.buscarPorNombre(termino));
    }

    private void buscarPorCategoria() {
        listarCategorias();
        String catId = vista.leerLinea("ID de categoría: ");
        listarProductos(productoCtrl.buscarPorCategoria(catId));
    }

    private void ordenarProductos() {
        vista.mostrarMenu("ORDENAR PRODUCTOS",
                new String[]{"Precio ↑ (menor a mayor)", "Precio ↓ (mayor a menor)",
                        "Nombre (A-Z)", "Volver"});
        int op = vista.leerOpcion(1, 4);
        String criterio;
        switch (op) {
            case 1: criterio = "precio_asc";  break;
            case 2: criterio = "precio_desc"; break;
            case 3: criterio = "nombre";      break;
            default: return;
        }
        listarProductos(productoCtrl.ordenarPor(criterio));
    }

    // ════════════════════════════════════════════════════════════════════════
    //  LOGS
    // ════════════════════════════════════════════════════════════════════════

    private void verLogs() {
        Usuario u = usuarioCtrl.getUsuarioActual();
        if (u == null || !u.tienePermiso("LISTAR_USUARIOS")) {
            vista.mostrarError("Acceso denegado. Solo administradores pueden ver los logs.");
            return;
        }
        vista.mostrarTitulo("Registro de Actividad (últimas 30 entradas)");
        List<model.LogEntry> logs = logModelo.listarTodos();
        if (logs.isEmpty()) { vista.mostrarInfo("No hay registros de actividad."); return; }
        int inicio = Math.max(0, logs.size() - 30);
        for (int i = inicio; i < logs.size(); i++) {
            vista.mostrarLinea(logs.get(i).toString());
        }
        vista.mostrarSeparador();
        vista.mostrarInfo("Total de entradas en log: " + logs.size());
    }

    // ════════════════════════════════════════════════════════════════════════
    //  MAIN
    // ════════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        new App().iniciar();
    }
}
