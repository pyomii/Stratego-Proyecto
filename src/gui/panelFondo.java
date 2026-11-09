
package gui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.JPanel;

public class panelFondo extends JPanel {

    private final BufferedImage fondo;
    private final Dimension base;
    private HashMap<Component, Rectangle> boundsOriginales;
    private HashMap<Component, Font> fuentesOriginales;
    private double escala = 1;

    public panelFondo(String archivo, int ancho, int alto) {
        super(null);
        fondo = recursos.imagen(archivo);
        base = new Dimension(ancho, alto);
        setPreferredSize(new Dimension(ancho, alto));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                escalar();
            }
        });
    }

    public double getEscala() {
        return escala;
    }

    private void escalar() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        double sx = getWidth() / (double) base.width;
        double sy = getHeight() / (double) base.height;
        escala = Math.min(sx, sy);
        if (boundsOriginales == null) {
            boundsOriginales = new HashMap<>();
            fuentesOriginales = new HashMap<>();
            for (Component c : getComponents()) {
                boundsOriginales.put(c, c.getBounds());
                fuentesOriginales.put(c, c.getFont());
            }
        }
        for (Component c : getComponents()) {
            Rectangle r = boundsOriginales.get(c);
            if (r != null) {
                c.setBounds((int) Math.round(r.x * sx), (int) Math.round(r.y * sy),
                        (int) Math.round(r.width * sx), (int) Math.round(r.height * sy));
            }
            Font f = fuentesOriginales.get(c);
            if (f != null) {
                c.setFont(f.deriveFont((float) (f.getSize2D() * escala)));
            }
        }
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }
}
