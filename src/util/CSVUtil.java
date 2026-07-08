package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad para operaciones de lectura y escritura sobre archivos CSV.
 * Implementa escritura atómica mediante archivo temporal para evitar
 * corrupción de datos si el programa se interrumpe durante el guardado.
 */


public class CSVUtil {

    /**
     * Lee todas las líneas de un archivo CSV (omite encabezado y líneas vacías).
     */
    public static List<String> leerLineas(String rutaArchivo) {
        List<String> lineas = new ArrayList<>();
        Path path = Paths.get(rutaArchivo);
        if (!Files.exists(path)) return lineas;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (primeraLinea) { primeraLinea = false; continue; } // saltar encabezado
                if (!linea.isEmpty()) lineas.add(linea);
            }
        } catch (IOException e) {
            System.err.println("[CSVUtil] Error leyendo " + rutaArchivo + ": " + e.getMessage());
        }
        return lineas;
    }

    /**
     * Escribe todas las líneas en un archivo CSV con encabezado.
     * Operación atómica: escribe en temporal y renombra.
     */
    public static boolean escribirLineas(String rutaArchivo, String encabezado, List<String> lineas) {
        Path path = Paths.get(rutaArchivo);
        Path temp = Paths.get(rutaArchivo + ".tmp");

        try {
            // Asegurar que exista el directorio
            if (path.getParent() != null) Files.createDirectories(path.getParent());

            try (BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(temp.toString()), StandardCharsets.UTF_8))) {
                bw.write(encabezado);
                bw.newLine();
                for (String linea : lineas) {
                    bw.write(linea);
                    bw.newLine();
                }
            }
            // Renombramiento atómico para evitar corrupción
            Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            return true;
        } catch (IOException e) {
            System.err.println("[CSVUtil] Error escribiendo " + rutaArchivo + ": " + e.getMessage());
            try { Files.deleteIfExists(temp); } catch (IOException ignored) {}
            return false;
        }
    }

    /**
     * Agrega una línea al final de un archivo CSV (crea el archivo si no existe).
     */
    public static boolean agregarLinea(String rutaArchivo, String encabezado, String nuevaLinea) {
        Path path = Paths.get(rutaArchivo);
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            boolean archivoNuevo = !Files.exists(path);

            try (BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(rutaArchivo, true), StandardCharsets.UTF_8))) {
                if (archivoNuevo) { bw.write(encabezado); bw.newLine(); }
                bw.write(nuevaLinea);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("[CSVUtil] Error agregando línea en " + rutaArchivo + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si existe un valor en la primera columna (ID) de un CSV.
     */
    public static boolean existeId(String rutaArchivo, String id) {
        for (String linea : leerLineas(rutaArchivo)) {
            String[] partes = linea.split(",", 2);
            if (partes.length > 0 && partes[0].trim().equalsIgnoreCase(id)) return true;
        }
        return false;
    }
}
