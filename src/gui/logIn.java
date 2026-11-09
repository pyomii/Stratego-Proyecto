
package gui;

import datos.jugador;
import datos.registrar;
import excepciones.credencialesInvalidas;
import java.awt.*;
import javax.swing.*;

public class logIn extends JDialog {

    public logIn(menuInicial inicial, registrar reg) {
        super(inicial, "Log In", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(800, 500));
        setContentPane(crearPanel(this, inicial, reg));
        pack();
        setLocationRelativeTo(inicial);
    }

    static void estilo(JTextField campo, int x, int y) {
        campo.setBounds(x, y, 286, 36);
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        campo.setFont(new Font("SansSerif", Font.BOLD, 18));
        campo.setForeground(new Color(15, 40, 100));
        campo.setCaretColor(new Color(15, 40, 100));
    }

    static JPanel crearPanel(JDialog dialogo, menuInicial inicial, registrar reg) {
        panelFondo panel = new panelFondo("bg_login.jpg", 800, 500);

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        estilo(txtUser, 339, 170);
        estilo(txtPass, 339, 250);

        botonImagen btnAccept = new botonImagen("Accept", "btn_peq.png", 114, 40, 22f);
        btnAccept.setLocation(230, 343);
        botonImagen btnBack = new botonImagen("Back", "btn_peq.png", 114, 40, 22f);
        btnBack.setLocation(450, 343);

        JLabel estado = new JLabel("", SwingConstants.CENTER);
        estado.setBounds(170, 300, 460, 26);
        estado.setFont(new Font("SansSerif", Font.BOLD, 15));
        estado.setForeground(new Color(190, 30, 40));

        btnAccept.addActionListener(e -> {
            try {
                jugador j = reg.login(txtUser.getText(), new String(txtPass.getPassword()));
                new menuPrincipal(reg, j).setVisible(true);
                dialogo.dispose();
                inicial.dispose();
            } catch (credencialesInvalidas ex) {
                estado.setText(ex.getMessage());
                txtPass.setText("");
                txtPass.requestFocus();
            }
        });
        btnBack.addActionListener(e -> dialogo.dispose());
        txtUser.addActionListener(e -> txtPass.requestFocus());

        panel.add(txtUser);
        panel.add(txtPass);
        panel.add(btnAccept);
        panel.add(btnBack);
        panel.add(estado);
        if (dialogo != null) {
            dialogo.getRootPane().setDefaultButton(btnAccept);
        }
        return panel;
    }
}