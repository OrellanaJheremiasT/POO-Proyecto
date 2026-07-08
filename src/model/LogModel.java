package model;

import java.util.ArrayList;
import java.util.List;
import util.CSVUtil;
import util.Rutas;

/**
 * Gestiona el registro de auditoría del sistema.
 * Escribe cada acción del usuario como una nueva línea en logs.csv
 * y permite listar todas las entradas registradas.
 */

public class LogModel {

    public void registrar(String usuarioId, String accion, String detalle) {
        LogEntry entry = new LogEntry(usuarioId, accion, detalle);
        CSVUtil.agregarLinea(Rutas.LOGS, Rutas.ENC_LOGS, entry.toCSV());
    }

    public List<LogEntry> listarTodos() {
        List<LogEntry> logs = new ArrayList<>();
        for (String linea : CSVUtil.leerLineas(Rutas.LOGS)) {
            LogEntry e = LogEntry.fromCSV(linea);
            if (e != null) logs.add(e);
        }
        return logs;
    }
}
