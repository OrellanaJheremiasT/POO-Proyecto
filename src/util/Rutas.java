package util;

/**
 * Centraliza las rutas absolutas de los archivos CSV del sistema
 * y los encabezados de cada archivo. Si se necesita cambiar la
 * ubicación de los datos, solo se modifica esta clase.
 */

public class Rutas {
    // Directorio base de datos — relativo al directorio de ejecución
    public static final String DIR_DATOS = "src\\data\\";

    public static final String USUARIOS    = DIR_DATOS + "usuarios.csv";
    public static final String CATEGORIAS  = DIR_DATOS + "categorias.csv";
    public static final String PRODUCTOS   = DIR_DATOS + "productos.csv";
    public static final String LOGS        = DIR_DATOS + "logs.csv";

    // Encabezados CSV
    public static final String ENC_USUARIOS   = "id,nombre,email,passwordHash,rol,activo";
    public static final String ENC_CATEGORIAS = "id,nombre,descripcion";
    public static final String ENC_PRODUCTOS  = "id,nombre,precio,categoriaId,stock";
    public static final String ENC_LOGS       = "timestamp|usuarioId|accion|detalle";
}
