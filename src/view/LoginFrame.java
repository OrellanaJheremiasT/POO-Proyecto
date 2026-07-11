package view;

import javax.swing.*;
import controller.UsuarioController;
import model.LogModel;
import controller.Resultado;
import model.Usuario;
import controller.CategoriaController;
import controller.ProductoController;

public class LoginFrame extends JFrame{
    
    private JLabel lblTitulo;
    private JLabel lblUsuario;
    private JLabel lblPassword;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private UsuarioController usuarioCtrl;
    private final CategoriaController categoriaCtrl;
    private final ProductoController productoCtrl;
    private final LogModel logModelo;

    public LoginFrame(UsuarioController usuarioCtrl,
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
        setTitle("Sistema de Gestión de Productos");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
    }
    
    private void inicializarComponentes() {
        lblTitulo = new JLabel("Sistema de Gestión de Productos");
        lblTitulo.setBounds(150, 30, 320, 30);
        add(lblTitulo);
        
        lblUsuario = new JLabel("Usuario: ");
        lblUsuario.setBounds(80, 100, 80, 25);
        add(lblUsuario);
        
        txtUsuario = new JTextField();
        txtUsuario.setBounds(180, 100, 180, 25);
        add(txtUsuario);
        
        lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(80, 150, 80, 25);
        add(lblPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setBounds(180, 150, 180, 25);
        add(txtPassword);
        
        btnIngresar = new JButton("Iniciar sesion");
        btnIngresar.setBounds(180, 200, 120, 25);
        add(btnIngresar);
    }
    
    private void agregarEventos() {
        btnIngresar.addActionListener(e -> iniciarSesion());

        txtPassword.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String id = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        Resultado<Usuario> resultado =
                usuarioCtrl.login(id, password);

        if (resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Inicio de sesión correcto",
                    JOptionPane.INFORMATION_MESSAGE
            );

            MenuFrame menu = new MenuFrame(
                    usuarioCtrl,
                    categoriaCtrl,
                    productoCtrl,
                    logModelo
            );

            menu.setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error de inicio de sesión",
                    JOptionPane.ERROR_MESSAGE
            );

            txtPassword.setText("");
            txtPassword.requestFocusInWindow();
        }
    }
    
}
