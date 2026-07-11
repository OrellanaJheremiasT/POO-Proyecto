package view;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.LogEntry;
import model.LogModel;

public class LogFrame extends JFrame {

    private final LogModel logModelo;
    private final MenuFrame menuFrame;

    private JLabel lblTitulo;
    private JLabel lblCantidad;

    private JButton btnActualizar;
    private JButton btnVolver;

    private JTable tablaLogs;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    public LogFrame(
            LogModel logModelo,
            MenuFrame menuFrame) {

        this.logModelo = logModelo;
        this.menuFrame = menuFrame;

        configurarVentana();
        inicializarComponentes();
        agregarEventos();
        cargarLogs();
    }

    private void configurarVentana() {
        setTitle("Registro de actividad");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                volverAlMenu();
            }
        });
    }

    private void inicializarComponentes() {
        lblTitulo = new JLabel("REGISTRO DE ACTIVIDAD DEL SISTEMA");
        lblTitulo.setFont(
                new Font("Arial", Font.BOLD, 20)
        );
        lblTitulo.setBounds(275, 20, 420, 35);
        add(lblTitulo);

        lblCantidad = new JLabel("Total de registros: 0");
        lblCantidad.setBounds(50, 75, 250, 25);
        add(lblCantidad);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(650, 70, 120, 35);
        add(btnActualizar);

        btnVolver = new JButton("Volver");
        btnVolver.setBounds(790, 70, 100, 35);
        add(btnVolver);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "Fecha y hora",
                    "Usuario",
                    "Acción",
                    "Detalle"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;
            }
        };

        tablaLogs = new JTable(modeloTabla);

        tablaLogs.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaLogs.setAutoCreateRowSorter(true);

        tablaLogs.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(140);

        tablaLogs.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(90);

        tablaLogs.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(170);

        tablaLogs.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(400);

        scrollTabla = new JScrollPane(tablaLogs);
        scrollTabla.setBounds(40, 130, 850, 370);
        add(scrollTabla);
    }

    private void agregarEventos() {
        btnActualizar.addActionListener(e -> cargarLogs());

        btnVolver.addActionListener(e -> volverAlMenu());
    }

    private void cargarLogs() {
        modeloTabla.setRowCount(0);

        List<LogEntry> logs =
                logModelo.listarTodos();

        int inicio = Math.max(0, logs.size() - 30);

        for (int i = inicio; i < logs.size(); i++) {
            LogEntry log = logs.get(i);

            modeloTabla.addRow(
                    new Object[]{
                        log.getTimestamp(),
                        log.getUsuarioId(),
                        log.getAccion(),
                        log.getDetalle()
                    }
            );
        }

        lblCantidad.setText(
                "Total de registros: "
                + logs.size()
                + " | Mostrando: "
                + (logs.size() - inicio)
        );
    }

    private void volverAlMenu() {
        setVisible(false);
        dispose();

        menuFrame.setVisible(true);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.toFront();
        menuFrame.requestFocus();
    }
}
