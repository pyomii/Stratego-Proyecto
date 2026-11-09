package gui;

import java.awt.*;
import java.util.function.IntConsumer;
import javax.swing.*;

public final class aviso {

    public static void mostrar(JFrame ventana, String titulo, String mensaje, String[] botones, IntConsumer alElegir) {
        JPanel capa = new JPanel(new GridBagLayout());
        capa.setOpaque(false);

        estiloSets.Tarjeta tarjeta = estiloSets.tarjeta(new Color(238, 245, 255, 250), 22);
        tarjeta.setPreferredSize(new Dimension(470, 250));
        tarjeta.setLayout(new BorderLayout(0, 8));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(18, 24, 16, 24));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(estiloSets.TEXTO);
        tarjeta.add(lblTitulo, BorderLayout.NORTH);

        String html = "<html><center>" + mensaje.replace("\n", "<br>") + "</center></html>";
        JLabel lblMensaje = new JLabel(html, SwingConstants.CENTER);
        lblMensaje.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblMensaje.setForeground(estiloSets.TEXTO_SUAVE);
        tarjeta.add(lblMensaje, BorderLayout.CENTER);

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        filaBotones.setOpaque(false);
        for (int i = 0; i < botones.length; i++) {
            botonImagen boton = new botonImagen(botones[i], "btn_peq.png", 114, 40, 20f);
            int indice = i;
            boton.addActionListener(e -> {
                quitar(ventana);
                alElegir.accept(indice);
            });
            filaBotones.add(boton);
            if (i == 0) {
                SwingUtilities.invokeLater(boton::requestFocusInWindow);
            }
        }
        tarjeta.add(filaBotones, BorderLayout.SOUTH);

        capa.add(tarjeta);
        ventana.setGlassPane(capa);
        capa.setVisible(true);
    }

    private static void quitar(JFrame ventana) {
        JPanel vacia = new JPanel();
        vacia.setOpaque(false);
        ventana.setGlassPane(vacia);
        vacia.setVisible(false);
    }
}
