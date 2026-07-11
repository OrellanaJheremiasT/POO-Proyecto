package view;

import controller.CategoriaController;
import controller.ProductoController;
import controller.Resultado;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Categoria;
import model.Producto;
import model.Usuario;

public class ProductoFrame extends JFrame {

    private final ProductoController productoCtrl;
    private final CategoriaController categoriaCtrl;
    private final Usuario usuarioActual;
    private final MenuFrame menuFrame;

    private JLabel lblTitulo;
    private JLabel lblId;
    private JLabel lblNombre;
    private JLabel lblPrecio;
    private JLabel lblCategoria;
    private JLabel lblStock;
    private JLabel lblBuscar;
    private JLabel lblOrdenar;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtBuscar;

    private JComboBox<String> cmbCategoria;
    private JComboBox<String> cmbOrdenar;

    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;
    private JButton btnMostrarTodos;
    private JButton btnVolver;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    public ProductoFrame(
            ProductoController productoCtrl,
            CategoriaController categoriaCtrl,
            Usuario usuarioActual,
            MenuFrame menuFrame) {

        this.productoCtrl = productoCtrl;
        this.categoriaCtrl = categoriaCtrl;
        this.usuarioActual = usuarioActual;
        this.menuFrame = menuFrame;

        configurarVentana();
        inicializarComponentes();
        agregarEventos();
        cargarCategorias();
        cargarProductos();
        configurarPermisos();
    }

    private void configurarVentana() {
        setTitle("Gestión de productos");
        setSize(1050, 700);
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
        lblTitulo = new JLabel("GESTIÓN DE PRODUCTOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBounds(390, 20, 300, 35);
        add(lblTitulo);

        lblId = new JLabel("ID:");
        lblId.setBounds(40, 75, 100, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(140, 75, 190, 25);
        add(txtId);

        lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(40, 115, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(140, 115, 190, 25);
        add(txtNombre);

        lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(370, 75, 100, 25);
        add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(470, 75, 190, 25);
        add(txtPrecio);

        lblStock = new JLabel("Stock:");
        lblStock.setBounds(370, 115, 100, 25);
        add(lblStock);

        txtStock = new JTextField();
        txtStock.setBounds(470, 115, 190, 25);
        add(txtStock);

        lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(700, 75, 100, 25);
        add(lblCategoria);

        cmbCategoria = new JComboBox<>();
        cmbCategoria.setBounds(790, 75, 190, 25);
        add(cmbCategoria);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(80, 170, 120, 35);
        add(btnGuardar);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(220, 170, 120, 35);
        add(btnEditar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(360, 170, 120, 35);
        add(btnEliminar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(500, 170, 120, 35);
        add(btnLimpiar);

        btnMostrarTodos = new JButton("Mostrar todos");
        btnMostrarTodos.setBounds(640, 170, 140, 35);
        add(btnMostrarTodos);

        btnVolver = new JButton("Volver");
        btnVolver.setBounds(800, 170, 120, 35);
        add(btnVolver);

        lblBuscar = new JLabel("Buscar por nombre:");
        lblBuscar.setBounds(40, 235, 140, 25);
        add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(180, 235, 220, 25);
        add(txtBuscar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(420, 232, 110, 30);
        add(btnBuscar);

        lblOrdenar = new JLabel("Ordenar:");
        lblOrdenar.setBounds(580, 235, 80, 25);
        add(lblOrdenar);

        cmbOrdenar = new JComboBox<>(
                new String[]{
                    "Sin ordenar",
                    "Precio menor a mayor",
                    "Precio mayor a menor",
                    "Nombre A-Z"
                }
        );
        cmbOrdenar.setBounds(660, 235, 220, 25);
        add(cmbOrdenar);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Nombre",
                    "Precio",
                    "Categoría",
                    "Stock",
                    "Valor total"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBounds(40, 295, 940, 300);
        add(scrollTabla);
    }

    private void agregarEventos() {
        btnGuardar.addActionListener(e -> guardarProducto());

        btnEditar.addActionListener(e -> editarProducto());

        btnEliminar.addActionListener(e -> eliminarProducto());

        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnBuscar.addActionListener(e -> buscarPorNombre());

        btnMostrarTodos.addActionListener(e -> cargarProductos());

        btnVolver.addActionListener(e -> volverAlMenu());

        txtBuscar.addActionListener(e -> buscarPorNombre());

        cmbOrdenar.addActionListener(e -> ordenarProductos());

        tablaProductos
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarFilaSeleccionada();
                    }
                });
    }

    private void cargarCategorias() {
        cmbCategoria.removeAllItems();

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

        for (Categoria categoria : resultado.getDatos()) {
            cmbCategoria.addItem(
                    categoria.getId() + " - " + categoria.getNombre()
            );
        }
    }

    private String obtenerIdCategoriaSeleccionada() {
        Object seleccion = cmbCategoria.getSelectedItem();

        if (seleccion == null) {
            return "";
        }

        String texto = seleccion.toString();
        int posicionSeparador = texto.indexOf(" - ");

        if (posicionSeparador == -1) {
            return texto.trim();
        }

        return texto
                .substring(0, posicionSeparador)
                .trim();
    }

    private void guardarProducto() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String precio = txtPrecio.getText().trim();
        String categoriaId =
                obtenerIdCategoriaSeleccionada();
        String stock = txtStock.getText().trim();

        Resultado<Void> resultado =
                productoCtrl.crearProducto(
                        id,
                        nombre,
                        precio,
                        categoriaId,
                        stock
                );

        if (resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Producto registrado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();
            cargarProductos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void editarProducto() {
        int filaSeleccionada =
                tablaProductos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un producto de la tabla.",
                    "Producto no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String precio = txtPrecio.getText().trim();
        String categoriaId =
                obtenerIdCategoriaSeleccionada();
        String stock = txtStock.getText().trim();

        Resultado<Void> resultado =
                productoCtrl.editarProducto(
                        id,
                        nombre,
                        precio,
                        categoriaId,
                        stock
                );

        if (resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Producto actualizado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();
            cargarProductos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarProducto() {
        int filaSeleccionada =
                tablaProductos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un producto de la tabla.",
                    "Producto no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String id = modeloTabla
                .getValueAt(filaSeleccionada, 0)
                .toString();

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar el producto con ID " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        Resultado<Void> resultado =
                productoCtrl.eliminarProducto(id);

        if (resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Producto eliminado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();
            cargarProductos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarProductos() {
        Resultado<List<Producto>> resultado =
                productoCtrl.listarProductos();

        mostrarProductos(resultado);
    }

    private void buscarPorNombre() {
        String nombre = txtBuscar.getText().trim();

        if (nombre.isBlank()) {
            cargarProductos();
            return;
        }

        Resultado<List<Producto>> resultado =
                productoCtrl.buscarPorNombre(nombre);

        mostrarProductos(resultado);
    }

    private void ordenarProductos() {
        int opcion = cmbOrdenar.getSelectedIndex();

        if (opcion == 0) {
            cargarProductos();
            return;
        }

        String criterio;

        switch (opcion) {
            case 1:
                criterio = "precio_asc";
                break;

            case 2:
                criterio = "precio_desc";
                break;

            case 3:
                criterio = "nombre";
                break;

            default:
                return;
        }

        Resultado<List<Producto>> resultado =
                productoCtrl.ordenarPor(criterio);

        mostrarProductos(resultado);
    }

    private void mostrarProductos(
            Resultado<List<Producto>> resultado) {

        modeloTabla.setRowCount(0);

        if (!resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        for (Producto producto : resultado.getDatos()) {
            modeloTabla.addRow(
                    new Object[]{
                        producto.getId(),
                        producto.getNombre(),
                        String.format(
                                "S/ %.2f",
                                producto.getPrecio()
                        ),
                        producto.getCategoriaId(),
                        producto.getStock(),
                        String.format(
                                "S/ %.2f",
                                producto.calcularValorTotal()
                        )
                    }
            );
        }
    }

    private void cargarFilaSeleccionada() {
        int filaSeleccionada =
                tablaProductos.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        String id = modeloTabla
                .getValueAt(filaSeleccionada, 0)
                .toString();

        String nombre = modeloTabla
                .getValueAt(filaSeleccionada, 1)
                .toString();

        String precioTexto = modeloTabla
                .getValueAt(filaSeleccionada, 2)
                .toString()
                .replace("S/", "")
                .trim();

        String categoriaId = modeloTabla
                .getValueAt(filaSeleccionada, 3)
                .toString();

        String stock = modeloTabla
                .getValueAt(filaSeleccionada, 4)
                .toString();

        txtId.setText(id);
        txtNombre.setText(nombre);
        txtPrecio.setText(precioTexto);
        txtStock.setText(stock);

        seleccionarCategoriaPorId(categoriaId);

        txtId.setEditable(false);
    }

    private void seleccionarCategoriaPorId(
            String categoriaId) {

        for (int i = 0;
                i < cmbCategoria.getItemCount();
                i++) {

            String elemento =
                    cmbCategoria.getItemAt(i);

            if (elemento.startsWith(
                    categoriaId + " - ")) {

                cmbCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtBuscar.setText("");

        txtId.setEditable(true);

        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }

        cmbOrdenar.setSelectedIndex(0);
        tablaProductos.clearSelection();

        txtId.requestFocusInWindow();
    }

    private void configurarPermisos() {
        boolean esAdministrador =
                usuarioActual.getTipoRol()
                == Usuario.TipoRol.ADMINISTRADOR;

        btnGuardar.setEnabled(esAdministrador);
        btnEditar.setEnabled(esAdministrador);
        btnEliminar.setEnabled(esAdministrador);

        txtId.setEnabled(esAdministrador);
        txtNombre.setEnabled(esAdministrador);
        txtPrecio.setEnabled(esAdministrador);
        txtStock.setEnabled(esAdministrador);
        cmbCategoria.setEnabled(esAdministrador);
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
