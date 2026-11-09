
package gui;

import datos.registrar;
import excepciones.passwordInvalido;
import excepciones.usuarioDuplicado;
import java.awt.*;
import javax.swing.*;

public class crearPlayer extends JDialog {

    private static final Color ROJO = new Color(190, 30, 40);
    private static final Color GRIS = new Color(90, 110, 150);

    public crearPlayer(menuInicial inicial, registrar reg) {
        super(inicial, "Crear Player", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(800, 500));
        setContentPane(crearPanel(this, reg));
        pack();
        setLocationRelativeTo(inicial);
    }

    static JPanel crearPanel(JDialog dialogo, registrar reg) {
        panelFondo panel = new panelFondo("bg_crcuenta.jpg", 800, 500);

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        logIn.estilo(txtUser, 339, 170);
        logIn.estilo(txtPass, 339, 250);

        JLabel aviso = new JLabel("Password: exactly " + registrar.LARGO_PASSWORD + " characters.");
        aviso.setBounds(339, 288, 420, 22);
        aviso.setFont(new Font("SansSerif", Font.BOLD, 14));
        aviso.setForeground(GRIS);

        botonImagen btnAccept = new botonImagen("Accept", "btn_peq.png", 114, 40, 22f);
        btnAccept.setLocation(230, 343);
        botonImagen btnBack = new botonImagen("Back", "btn_peq.png", 114, 40, 22f);
        btnBack.setLocation(450, 343);

        btnAccept.addActionListener(e -> {
            try {
                reg.crearJugador(txtUser.getText(), new String(txtPass.getPassword()));
                dialogo.dispose();
            } catch (usuarioDuplicado ex) {
                aviso.setForeground(ROJO);
                aviso.setText(ex.getMessage());
                txtUser.setText("");
                txtUser.requestFocus();
            } catch (passwordInvalido ex) {
                aviso.setForeground(ROJO);
                aviso.setText(ex.getMessage());
                txtPass.setText("");
                txtPass.requestFocus();
            } catch (IllegalArgumentException ex) {
                aviso.setForeground(ROJO);
                aviso.setText(ex.getMessage());
            }
        });
        btnBack.addActionListener(e -> dialogo.dispose());
        txtUser.addActionListener(e -> txtPass.requestFocus());

        panel.add(txtUser);
        panel.add(txtPass);
        panel.add(aviso);
        panel.add(btnAccept);
        panel.add(btnBack);
        if (dialogo != null) {
            dialogo.getRootPane().setDefaultButton(btnAccept);
        }
        return panel;
    }
}
