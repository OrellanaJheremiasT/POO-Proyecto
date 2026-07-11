package view;

import controller.Resultado;
import controller.UsuarioController;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Usuario;

public class UsuarioFrame extends JFrame {

    private final UsuarioController usuarioCtrl;
    private final MenuFrame menuFrame;

    private JLabel lblTitulo;
    private JLabel lblId;
    private JLabel lblNombre;
    private JLabel lblEmail;
    private JLabel lblPassword;
    private JLabel lblRol;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRol;

    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnVolver;

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    public UsuarioFrame(
        UsuarioController usuarioCtrl,
        MenuFrame menuFrame) {

        this.usuarioCtrl = usuarioCtrl;
        this.menuFrame = menuFrame;

        configurarVentana();
        inicializarComponentes();
        agregarEventos();
        cargarUsuarios();
    }

    private void configurarVentana() {
        setTitle("Gestión de usuarios");
        setSize(880, 630);
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
        lblTitulo = new JLabel("GESTIÓN DE USUARIOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBounds(320, 20, 280, 35);
        add(lblTitulo);

        lblId = new JLabel("ID:");
        lblId.setBounds(50, 80, 100, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(160, 80, 220, 25);
        add(txtId);

        lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 120, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(160, 120, 220, 25);
        add(txtNombre);

        lblEmail = new JLabel("Correo:");
        lblEmail.setBounds(450, 80, 100, 25);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(550, 80, 250, 25);
        add(txtEmail);

        lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(450, 120, 100, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(550, 120, 250, 25);
        add(txtPassword);

        lblRol = new JLabel("Rol:");
        lblRol.setBounds(50, 160, 100, 25);
        add(lblRol);

        cmbRol = new JComboBox<>(
                new String[]{"ADMINISTRADOR", "ESTANDAR"}
        );
        cmbRol.setBounds(160, 160, 220, 25);
        add(cmbRol);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(100, 220, 140, 35);
        add(btnGuardar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(270, 220, 140, 35);
        add(btnEliminar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(440, 220, 140, 35);
        add(btnLimpiar);

        btnVolver = new JButton("Volver");
        btnVolver.setBounds(610, 220, 140, 35);
        add(btnVolver);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Nombre",
                    "Correo",
                    "Rol",
                    "Estado"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        scrollTabla = new JScrollPane(tablaUsuarios);
        scrollTabla.setBounds(50, 300, 750, 240);
        add(scrollTabla);
    }

    private void agregarEventos() {
        btnGuardar.addActionListener(e -> guardarUsuario());

        btnEliminar.addActionListener(e -> eliminarUsuario());

        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnVolver.addActionListener(e -> volverAlMenu());

        tablaUsuarios
        .getSelectionModel()
        .addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
            cargarFilaSeleccionada();
            }
        });
    }

    private void guardarUsuario() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password =
                new String(txtPassword.getPassword());

        String rol =
                cmbRol.getSelectedItem().toString();

        Resultado<Void> resultado =
                usuarioCtrl.crearUsuario(
                    id,
                    nombre,
                    email,
                    password,
                    rol
                );

        if (resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Usuario registrado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();
            cargarUsuarios();

        } else {
            JOptionPane.showMessageDialog(
                this,
                resultado.getMensaje(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarUsuario() {
        int filaSeleccionada =
                tablaUsuarios.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un usuario de la tabla.",
                    "Usuario no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String id = modeloTabla
                .getValueAt(filaSeleccionada, 0)
                .toString();

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar al usuario con ID " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        Resultado<Void> resultado =
                usuarioCtrl.eliminarUsuario(id);

        if (resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Usuario eliminado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();
            cargarUsuarios();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);

        Resultado<List<Usuario>> resultado =
                usuarioCtrl.listarUsuarios();

        if (!resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        List<Usuario> usuarios =
                resultado.getDatos();

        for (Usuario usuario : usuarios) {
            modeloTabla.addRow(
                    new Object[]{
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getRol(),
                        usuario.estaActivo()
                                ? "Activo"
                                : "Inactivo"
                    }
            );
        }
    }

    private void cargarFilaSeleccionada() {
        int filaSeleccionada =
                tablaUsuarios.getSelectedRow();

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

        txtEmail.setText(
                modeloTabla
                        .getValueAt(filaSeleccionada, 2)
                        .toString()
        );

        cmbRol.setSelectedItem(
                modeloTabla
                        .getValueAt(filaSeleccionada, 3)
                        .toString()
        );

        txtId.setEditable(false);
        txtNombre.setEditable(false);
        txtEmail.setEditable(false);
        txtPassword.setEditable(false);
        cmbRol.setEnabled(false);
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtEmail.setText("");
        txtPassword.setText("");

        cmbRol.setSelectedIndex(0);

        txtId.setEditable(true);
        txtNombre.setEditable(true);
        txtEmail.setEditable(true);
        txtPassword.setEditable(true);
        cmbRol.setEnabled(true);

        tablaUsuarios.clearSelection();

        txtId.requestFocusInWindow();
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