package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa una entrada de auditoría del sistema.
 * Registra el timestamp, el usuario que ejecutó la acción,
 * el tipo de acción y su detalle. Se persiste en logs.csv.
 */

public class LogEntry {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String timestamp;
    private String usuarioId;
    private String accion;
    private String detalle;

    public LogEntry(String usuarioId, String accion, String detalle) {
        this.timestamp = LocalDateTime.now().format(FMT);
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.detalle = detalle;
    }

    public LogEntry(String timestamp, String usuarioId, String accion, String detalle) {
        this.timestamp = timestamp;
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.detalle = detalle;
    }

    public String toCSV() {
        return String.join("|", timestamp, usuarioId, accion, detalle.replace("|", "-"));
    }

    public static LogEntry fromCSV(String linea) {
        String[] p = linea.split("\\|", 4);
        if (p.length < 4) return null;
        return new LogEntry(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim());
    }

    @Override
    public String toString() {
        return String.format("[%s] Usuario:%-8s %-25s %s", timestamp, usuarioId, accion, detalle);
    }

    public String getTimestamp() { return timestamp; }
    public String getUsuarioId() { return usuarioId; }
    public String getAccion() { return accion; }
    public String getDetalle() { return detalle; }
}
