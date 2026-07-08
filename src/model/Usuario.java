package model;

/**
 * Representa a un usuario del sistema. Extiende Persona e implementa Autenticable.
 * Gestiona el rol (ADMINISTRADOR o ESTANDAR), la contraseña hasheada
 * y el control de acceso mediante una tabla de permisos por acción.
 */

public class Usuario extends Persona implements Autenticable {
    private String passwordHash;
    private TipoRol rol;
    private boolean activo;

    public enum TipoRol {                               
        ADMINISTRADOR, ESTANDAR;

        public static TipoRol fromString(String s) {
            try { return TipoRol.valueOf(s.toUpperCase()); }
            catch (Exception e) { return ESTANDAR; }
        }
    }

    // Permisos por acción y rol
    private static final java.util.Map<String, java.util.List<TipoRol>> PERMISOS;
    static {
        PERMISOS = new java.util.HashMap<>();
        PERMISOS.put("CREAR_USUARIO",     java.util.Arrays.asList(TipoRol.ADMINISTRADOR));
        PERMISOS.put("ELIMINAR_USUARIO",  java.util.Arrays.asList(TipoRol.ADMINISTRADOR));
        PERMISOS.put("LISTAR_USUARIOS",   java.util.Arrays.asList(TipoRol.ADMINISTRADOR));
        PERMISOS.put("CREAR_CATEGORIA",   java.util.Arrays.asList(TipoRol.ADMINISTRADOR));
        PERMISOS.put("ELIMINAR_CATEGORIA",java.util.Arrays.asList(TipoRol.ADMINISTRADOR));
        PERMISOS.put("LISTAR_CATEGORIAS", java.util.Arrays.asList(TipoRol.ADMINISTRADOR, TipoRol.ESTANDAR));
        PERMISOS.put("CREAR_PRODUCTO",    java.util.Arrays.asList(TipoRol.ADMINISTRADOR));
        PERMISOS.put("EDITAR_PRODUCTO",   java.util.Arrays.asList(TipoRol.ADMINISTRADOR));
        PERMISOS.put("ELIMINAR_PRODUCTO", java.util.Arrays.asList(TipoRol.ADMINISTRADOR));
        PERMISOS.put("LISTAR_PRODUCTOS",  java.util.Arrays.asList(TipoRol.ADMINISTRADOR, TipoRol.ESTANDAR));
        PERMISOS.put("BUSCAR_PRODUCTO",   java.util.Arrays.asList(TipoRol.ADMINISTRADOR, TipoRol.ESTANDAR));
    }

    public Usuario(String id, String nombre, String email, String passwordHash, TipoRol rol, boolean activo) {
        super(id, nombre, email);
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = activo;
    }

    // Implementación de Persona
    @Override
    public String getRol() { return rol.name(); }

    @Override
    public boolean tienePermiso(String accion) {
        java.util.List<TipoRol> rolesPermitidos = PERMISOS.get(accion.toUpperCase());
        return rolesPermitidos != null && rolesPermitidos.contains(this.rol);
    }

    // Implementación de Autenticable
    @Override
    public boolean autenticar(String password) {
        return activo && passwordHash.equals(hashSimple(password));
    }

    @Override
    public String getPasswordHash() { return passwordHash; }

    @Override
    public boolean estaActivo() { return activo; }

    /** Hash simple (suma de chars × posición) — sin dependencias externas */
        public static String hashSimple(String input) {
            long hash = 5381L;
            for (int i = 0; i < input.length(); i++) {
                hash = ((hash << 5) + hash) + input.charAt(i);
            }
            return Long.toHexString(hash & 0xFFFFFFFFFFFFFFFFL);
        }

    // Getters/Setters
    public TipoRol getTipoRol() { return rol; }
    public void setRol(TipoRol rol) { this.rol = rol; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /** Serializa a línea CSV */
    public String toCSV() {
        return String.join(",", id, nombre, email, passwordHash, rol.name(), String.valueOf(activo));
    }

    /** Deserializa desde línea CSV */
    public static Usuario fromCSV(String linea) {
        String[] p = linea.split(",", -1);
        if (p.length < 6) return null;
        return new Usuario(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                TipoRol.fromString(p[4].trim()), Boolean.parseBoolean(p[5].trim()));
    }

    @Override
    public String toString() {
        return String.format("%-8s %-20s %-25s %-15s %s",
                id, nombre, email, rol.name(), activo ? "Activo" : "Inactivo");
    }
}
