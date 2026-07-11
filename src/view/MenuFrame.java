package view;

import controller.CategoriaController;
import controller.ProductoController;
import controller.UsuarioController;
import javax.swing.*;
import model.LogModel;
import model.Usuario;

public class MenuFrame extends JFrame{
    
    private final UsuarioController usuarioCtrl;
    private final CategoriaController categoriaCtrl;
    private final ProductoController productoCtrl;
    private final LogModel logModelo;

    private JLabel lblTitulo;
    private JLabel lblBienvenida;
    private JLabel lblRol;

    private JButton btnUsuarios;
    private JButton btnCategorias;
    private JButton btnProductos;
    private JButton btnLogs;
    private JButton btnCerrarSesion;
    
    public MenuFrame(
            UsuarioController usuarioCtrl,
            CategoriaController categoriaCtrl,
            ProductoController productoCtrl,
            LogModel logModelo) {

        this.usuarioCtrl = usuarioCtrl;
        this.categoriaCtrl = categoriaCtrl;
        this.productoCtrl = productoCtrl;
        this.logModelo = logModelo;

        configurarVentana();
        inicializarComponentes();
        agregarEventos();
    }
            
    private void configurarVentana() {
        setTitle("Menú principal");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
    }
    
    private void inicializarComponentes() {
        Usuario usuario = usuarioCtrl.getUsuarioActual();

        lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE PRODUCTOS");
        lblTitulo.setBounds(165, 30, 300, 30);
        add(lblTitulo);

        lblBienvenida = new JLabel(
                "Bienvenido: " + usuario.getNombre()
        );
        lblBienvenida.setBounds(180, 75, 300, 25);
        add(lblBienvenida);

        lblRol = new JLabel(
                "Rol: " + usuario.getRol()
        );
        lblRol.setBounds(230, 105, 200, 25);
        add(lblRol);

        btnUsuarios = new JButton("Gestión de usuarios");
        btnUsuarios.setBounds(180, 155, 230, 40);
        add(btnUsuarios);

        btnCategorias = new JButton("Gestión de categorías");
        btnCategorias.setBounds(180, 210, 230, 40);
        add(btnCategorias);

        btnProductos = new JButton("Gestión de productos");
        btnProductos.setBounds(180, 265, 230, 40);
        add(btnProductos);

        btnLogs = new JButton("Ver logs");
        btnLogs.setBounds(180, 320, 230, 40);
        add(btnLogs);

        btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setBounds(180, 385, 230, 40);
        add(btnCerrarSesion);

        configurarPermisos(usuario);
    }
    
    private void configurarPermisos(Usuario usuario) {
        boolean esAdministrador =
                usuario.getTipoRol() == Usuario.TipoRol.ADMINISTRADOR;

        btnUsuarios.setEnabled(esAdministrador);
        btnLogs.setEnabled(esAdministrador);
    }
    
    private void agregarEventos() {

        btnUsuarios.addActionListener(e -> {
            UsuarioFrame usuarioFrame = new UsuarioFrame(
                    usuarioCtrl,
                    this
            );

            usuarioFrame.setVisible(true);
            setVisible(false);
        });

        btnCategorias.addActionListener(e -> {
            CategoriaFrame categoriaFrame = new CategoriaFrame(
                    categoriaCtrl,
                    this
            );

            categoriaFrame.setVisible(true);
            setVisible(false);
        });

        btnProductos.addActionListener(e -> {
            ProductoFrame productoFrame = new ProductoFrame(
                    productoCtrl,
                    categoriaCtrl,
                    usuarioCtrl.getUsuarioActual(),
                    this
            );

            productoFrame.setVisible(true);
            setVisible(false);
        });

        btnLogs.addActionListener(e -> {
            LogFrame logFrame = new LogFrame(
                    logModelo,
                    this
            );

            logFrame.setVisible(true);
            setVisible(false);
        });

        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }
    
    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar la sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            usuarioCtrl.logout();

            LoginFrame login = new LoginFrame(
                    usuarioCtrl,
                    categoriaCtrl,
                    productoCtrl,
                    logModelo
            );
            login.setVisible(true);

            dispose();
        }
    }
    
}
