package view;

import controller.CategoriaController;
import controller.Resultado;
import java.awt.Font;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Categoria;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CategoriaFrame extends JFrame {

    private final CategoriaController categoriaCtrl;
    private final MenuFrame menuFrame;

    private JLabel lblTitulo;
    private JLabel lblId;
    private JLabel lblNombre;
    private JLabel lblDescripcion;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtDescripcion;

    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnVolver;

    private JTable tablaCategorias;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    public CategoriaFrame(
            CategoriaController categoriaCtrl,
            MenuFrame menuFrame) {

        this.categoriaCtrl = categoriaCtrl;
        this.menuFrame = menuFrame;

        configurarVentana();
        inicializarComponentes();
        agregarEventos();
        cargarCategorias();
    }

    private void configurarVentana() {
        setTitle("Gestión de categorías");
        setSize(760, 550);
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
        lblTitulo = new JLabel("GESTIÓN DE CATEGORÍAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBounds(250, 20, 300, 35);
        add(lblTitulo);

        lblId = new JLabel("ID:");
        lblId.setBounds(60, 80, 100, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(170, 80, 200, 25);
        add(txtId);

        lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(60, 120, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(170, 120, 200, 25);
        add(txtNombre);

        lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBounds(60, 160, 100, 25);
        add(lblDescripcion);

        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(170, 160, 430, 25);
        add(txtDescripcion);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(60, 210, 130, 35);
        add(btnGuardar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(210, 210, 130, 35);
        add(btnEliminar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(360, 210, 130, 35);
        add(btnLimpiar);

        btnVolver = new JButton("Volver");
        btnVolver.setBounds(510, 210, 130, 35);
        add(btnVolver);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Descripción"},
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaCategorias = new JTable(modeloTabla);
        tablaCategorias.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        scrollTabla = new JScrollPane(tablaCategorias);
        scrollTabla.setBounds(60, 280, 620, 190);
        add(scrollTabla);
    }

    private void agregarEventos() {
        btnGuardar.addActionListener(e -> guardarCategoria());

        btnEliminar.addActionListener(e -> eliminarCategoria());

        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnVolver.addActionListener(e -> volverAlMenu());

        tablaCategorias
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarFilaSeleccionada();
                    }
                });
    }

    private void guardarCategoria() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        Resultado<Void> resultado =
                categoriaCtrl.crearCategoria(
                        id,
                        nombre,
                        descripcion
                );

        if (resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Categoría registrada",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();
            cargarCategorias();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarCategoria() {
        int filaSeleccionada =
                tablaCategorias.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una categoría de la tabla.",
                    "Categoría no seleccionada",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String id = modeloTabla
                .getValueAt(filaSeleccionada, 0)
                .toString();

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar la categoría con ID " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        Resultado<Void> resultado =
                categoriaCtrl.eliminarCategoria(id);

        if (resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Categoría eliminada",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();
            cargarCategorias();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarCategorias() {
        modeloTabla.setRowCount(0);

        Resultado<List<Categoria>> resultado =
                categoriaCtrl.listarCategorias();

        if (!resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        List<Categoria> categorias =
                resultado.getDatos();

        for (Categoria categoria : categorias) {
            modeloTabla.addRow(
                    new Object[]{
                        categoria.getId(),
                        categoria.getNombre(),
                        categoria.getDescripcion()
                    }
            );
        }
    }

    private void cargarFilaSeleccionada() {
        int filaSeleccionada =
                tablaCategorias.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        txtId.setText(
                modeloTabla
                        .getValueAt(filaSeleccionada, 0)
                        .toString()
        );

        txtNombre.setText(
                modeloTabla
                        .getValueAt(filaSeleccionada, 1)
                        .toString()
        );

        txtDescripcion.setText(
                modeloTabla
                        .getValueAt(filaSeleccionada, 2)
                        .toString()
        );

        txtId.setEditable(false);
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");

        txtId.setEditable(true);

        tablaCategorias.clearSelection();

        txtId.requestFocusInWindow();
    }

    private void volverAlMenu() {
        menuFrame.setVisible(true);
        dispose();
        
        menuFrame.setVisible(true);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.toFront();
        menuFrame.requestFocus();
    }
}