package gui;

import datos.jugador;
import datos.registrar;
import java.awt.*;
import javax.swing.*;

public class miPerfil extends JPanel {

    private final registrar reg;
    private final jugador actual;

    private final JLabel lblPassword = estiloSets.valor("", 21);
    private final botonImagen btnMostrar = new botonImagen("Show", "btn_peq.png", 90, 34, 18f);
    private boolean mostrandoPassword = false;

    public miPerfil(registrar reg, jugador actual) {
        this.reg = reg;
        this.actual = actual;

        setOpaque(false);
        setLayout(null);

        JLabel titulo = estiloSets.titulo("MY PROFILE");
        titulo.setBounds(0, 18, 530, 45);
        add(titulo);

        estiloSets.Tarjeta tarjeta = estiloSets.tarjeta(estiloSets.BLANCO_SUAVE);
        tarjeta.setBounds(45, 88, 440, 320);
        add(tarjeta);

        fila(tarjeta, "Username:", actual.getUsername(), 35);
        fila(tarjeta, "Points:", String.valueOf(actual.getPuntos()), 87);
        fila(tarjeta, "Wins:", String.valueOf(actual.getVictorias()), 139);
        fila(tarjeta, "Ranking:", posicionRanking(), 191);

        JLabel lblPass = estiloSets.etiqueta("Password:", 21);
        lblPass.setBounds(30, 243, 150, 30);
        tarjeta.add(lblPass);

        lblPassword.setBounds(200, 243, 115, 30);
        tarjeta.add(lblPassword);

        btnMostrar.setBounds(320, 241, 90, 34);
        btnMostrar.addActionListener(e -> alternarPassword());
        tarjeta.add(btnMostrar);

        actualizarPassword();
    }

    private void fila(Container tarjeta, String nombre, String valor, int y) {
        JLabel lbl = estiloSets.etiqueta(nombre, 21);
        lbl.setBounds(30, y, 150, 30);
        tarjeta.add(lbl);

        JLabel val = estiloSets.valor(valor, 21);
        val.setBounds(200, y, 210, 30);
        tarjeta.add(val);
    }

    private String posicionRanking() {
        int posicion = reg.getRanking().indexOf(actual) + 1;
        return posicion > 0 ? "#" + posicion : "-";
    }

    private void alternarPassword() {
        mostrandoPassword = !mostrandoPassword;
        btnMostrar.setText(mostrandoPassword ? "Hide" : "Show");
        actualizarPassword();
    }

    private void actualizarPassword() {
        if (mostrandoPassword) {
            lblPassword.setText(actual.getPassword());
        } else {
            lblPassword.setText("\u2022".repeat(actual.getPassword().length()));
        }
    }
}
