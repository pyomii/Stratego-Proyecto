
package gui;

import datos.jugador;
import datos.registrar;
import excepciones.credencialesInvalidas;
import excepciones.passwordInvalido;
import java.awt.*;
import javax.swing.*;

public class cambiarClave extends JPanel {

    private static final Color ROJO = new Color(190, 30, 40);
    private static final Color VERDE = new Color(25, 115, 60);

    private static final int LABEL_W = 190;
    private static final int FIELD_W = 280;
    private static final int ROW_H = 42;
    private static final int SEPARACION = 30;
    private static final int BTN_W = 114, BTN_H = 40;

    private final registrar reg;
    private final jugador actual;

    private final JLabel lblActual = etiqueta("Old password:");
    private final JLabel lblNueva = etiqueta("New password:");
    private final JLabel pista = new JLabel();
    private final JLabel estado = new JLabel("", SwingConstants.CENTER);
    private final JPasswordField txtActual = campo();
    private final JPasswordField txtNueva = campo();
    private final botonImagen btnAccept = new botonImagen("Accept", "btn_peq.png", BTN_W, BTN_H, 22f);

    public cambiarClave(registrar reg, jugador actual) {
        this.reg = reg;
        this.actual = actual;

        setOpaque(false);
        setLayout(null);

        pista.setText("Exactly " + registrar.LARGO_PASSWORD + " characters.");
        pista.setFont(new Font("SansSerif", Font.BOLD, 13));
        pista.setForeground(new Color(90, 110, 150));

        estado.setFont(new Font("SansSerif", Font.BOLD, 15));

        btnAccept.addActionListener(e -> aceptar());
        txtActual.addActionListener(e -> txtNueva.requestFocus());
        txtNueva.addActionListener(e -> aceptar());

        add(lblActual);
        add(lblNueva);
        add(pista);
        add(estado);
        add(txtActual);
        add(txtNueva);
        add(btnAccept);
    }

    private static JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.RIGHT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        lbl.setForeground(new Color(15, 40, 100));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        return lbl;
    }

    private static JPasswordField campo() {
        JPasswordField campo = new JPasswordField();
        campo.setOpaque(true);
        campo.setBackground(new Color(240, 247, 255));
        campo.setForeground(new Color(15, 40, 100));
        campo.setCaretColor(new Color(15, 40, 100));
        campo.setFont(new Font("SansSerif", Font.BOLD, 18));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 130, 210), 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)));
        return campo;
    }

    @Override
    public void doLayout() {
        int anchoBloque = LABEL_W + FIELD_W;
        int altoCampos = ROW_H * 2 + SEPARACION;

        int x = (getWidth() - anchoBloque) / 2;
        int y = (getHeight() - altoCampos) / 2;

        lblActual.setBounds(x, y + (ROW_H - 30) / 2, LABEL_W, 30);
        txtActual.setBounds(x + LABEL_W, y, FIELD_W, ROW_H);

        int yNueva = y + ROW_H + SEPARACION;
        lblNueva.setBounds(x, yNueva + (ROW_H - 30) / 2, LABEL_W, 30);
        txtNueva.setBounds(x + LABEL_W, yNueva, FIELD_W, ROW_H);
        pista.setBounds(x + LABEL_W, yNueva + ROW_H + 4, FIELD_W, 18);

        estado.setBounds(0, getHeight() - 56, getWidth(), 28);

        int yBoton = yNueva + ROW_H + 36;
        btnAccept.setBounds(x + (anchoBloque - BTN_W) / 2, yBoton, BTN_W, BTN_H);
    }

    private void aceptar() {
        try {
            reg.cambiarPassword(actual,new String(txtActual.getPassword()),new String(txtNueva.getPassword()));
            estado.setForeground(VERDE);
            estado.setText("Password changed successfully!");
            txtActual.setText("");
            txtNueva.setText("");
            txtActual.requestFocus();
        } catch (credencialesInvalidas ex) {
            estado.setForeground(ROJO);
            estado.setText(ex.getMessage());
            txtActual.setText("");
            txtActual.requestFocus();
        } catch (passwordInvalido ex) {
            estado.setForeground(ROJO);
            estado.setText(ex.getMessage());
            txtNueva.setText("");
            txtNueva.requestFocus();
        }
    }
}
