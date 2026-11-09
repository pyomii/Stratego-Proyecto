package gui;

import java.awt.*;
import javax.swing.*;

final class estiloSets {

    static final Color TEXTO = new Color(14, 42, 102);
    static final Color TEXTO_SUAVE = new Color(48, 84, 145);
    static final Color HEROE = new Color(24, 94, 200);
    static final Color VILLANO = new Color(168, 45, 62);
    static final Color ORO = new Color(190, 140, 15);
    static final Color PLATA = new Color(120, 138, 158);
    static final Color BRONCE = new Color(168, 102, 52);
    static final Color BLANCO_SUAVE = new Color(255, 255, 255, 110);
    static final Color CELESTE_SUAVE = new Color(220, 240, 255, 120);

    private estiloSets() {
    }

    static JLabel titulo(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(recursos.fuente(30f));
        lbl.setForeground(TEXTO);
        return lbl;
    }

    static JLabel etiqueta(String texto, int tam) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, tam));
        lbl.setForeground(TEXTO_SUAVE);
        return lbl;
    }

    static JLabel valor(String texto, int tam) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, tam));
        lbl.setForeground(TEXTO);
        return lbl;
    }

    static Tarjeta tarjeta(Color fondo) {
        return new Tarjeta(fondo, 22);
    }

    static Tarjeta tarjeta(Color fondo, int radio) {
        return new Tarjeta(fondo, radio);
    }

    static class Tarjeta extends JPanel {

        private final Color fondo;
        private final int radio;

        Tarjeta(Color fondo, int radio) {
            this.fondo = fondo;
            this.radio = radio;
            setOpaque(false);
            setLayout(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
            g2.setColor(new Color(255, 255, 255, 95));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
            g2.dispose();
        }
    }
}
