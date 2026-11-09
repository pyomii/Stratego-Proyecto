
package gui;

import datos.registrar;
import java.awt.Dimension;
import javax.swing.*;

public class menuInicial extends JFrame {

    private static final int LOGIN_X = 222, LOGIN_Y = 330;
    private static final int REGISTER_X = 185, REGISTER_Y = 440;

    public menuInicial(registrar reg) {
        setTitle("Marvel Heroes - Stratego");
        setIconImage(recursos.icono());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(1280, 720));
        setContentPane(crearPanel(this, reg));
        pack();
        setLocationRelativeTo(null);
    }

    static JPanel crearPanel(menuInicial ventana, registrar reg) {
        panelFondo panel = new panelFondo("bg_menuin.jpg", 1280, 720);

        botonImagen btnLogin = new botonImagen("Log In", "btn_gde.png", 250, 75, 38f);
        btnLogin.setLocation(LOGIN_X, LOGIN_Y);
        btnLogin.addActionListener(e -> new logIn(ventana, reg).setVisible(true));

        botonImagen btnRegister = new botonImagen("Register", "btn_gde.png", 250, 75, 38f);
        btnRegister.setLocation(REGISTER_X, REGISTER_Y);
        btnRegister.addActionListener(e -> new crearPlayer(ventana, reg).setVisible(true));

        panel.add(btnLogin);
        panel.add(btnRegister);
        return panel;
    }
}