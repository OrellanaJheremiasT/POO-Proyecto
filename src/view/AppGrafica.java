package view;

import controller.CategoriaController;
import controller.ProductoController;
import controller.UsuarioController;
import javax.swing.SwingUtilities;
import model.CategoriaModel;
import model.LogModel;
import model.ProductoModel;
import model.UsuarioModel;


public class AppGrafica {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            UsuarioModel usuarioModelo = new UsuarioModel();
            CategoriaModel categoriaModelo = new CategoriaModel();
            ProductoModel productoModelo = new ProductoModel();
            LogModel logModelo = new LogModel();

            UsuarioController usuarioCtrl =
                    new UsuarioController(usuarioModelo, logModelo);

            CategoriaController categoriaCtrl =
                    new CategoriaController(
                            categoriaModelo,
                            productoModelo,
                            logModelo,
                            usuarioCtrl
                    );

            ProductoController productoCtrl =
                    new ProductoController(
                            productoModelo,
                            categoriaModelo,
                            logModelo,
                            usuarioCtrl
                    );

            LoginFrame login = new LoginFrame(
                    usuarioCtrl,
                    categoriaCtrl,
                    productoCtrl,
                    logModelo
            );

            login.setVisible(true);
        });
    }
    
}
