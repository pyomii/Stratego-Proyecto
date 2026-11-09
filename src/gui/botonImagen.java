package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import javax.swing.ButtonModel;
import javax.swing.JButton;

public class botonImagen extends JButton {

    private final BufferedImage normal;
    private final BufferedImage hover;
    private final BufferedImage presionado;
    private final Font fuente;
    private final float tamFuente;
    private final int altoBase;

    public botonImagen(String texto, String archivoImagen, int ancho, int alto, float tamFuente) {
        super(texto);
        this.normal = escalar(recursos.imagen(archivoImagen), ancho, alto);
        this.hover = iluminar(normal, 1.35f);
        this.presionado = iluminar(normal, 0.6f);
        this.fuente = recursos.fuente(tamFuente);
        this.tamFuente = tamFuente;
        this.altoBase = alto;

        setPreferredSize(new Dimension(ancho, alto));
        setSize(ancho, alto);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setMargin(new Insets(0, 0, 0, 0));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static BufferedImage escalar(BufferedImage src, int w, int h) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return out;
    }

    private static BufferedImage iluminar(BufferedImage src, float factor) {
        BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        new RescaleOp(new float[]{factor, factor, factor, 1f}, new float[]{0, 0, 0, 0}, null).filter(src, out);
        return out;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        ButtonModel m = getModel();
        int desplazamiento = m.isPressed() ? 2 : 0;
        BufferedImage img;
        if (m.isPressed()) {
            img = presionado;
        } else if (m.isRollover() || m.isArmed()) {
            img = hover;
        } else {
            img = normal;
        }
        if (!isEnabled()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        g2.drawImage(img, 0, desplazamiento, getWidth(), getHeight(), null);

        float escala = altoBase > 0 ? getHeight() / (float) altoBase : 1f;
        g2.setFont(escala == 1f ? fuente : fuente.deriveFont(tamFuente * escala));
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent() + desplazamiento;
        g2.setColor(new Color(5, 20, 70, 160));
        g2.drawString(getText(), x + 2, y + 2);
        g2.setColor(new Color(190, 220, 255));
        g2.drawString(getText(), x, y);
        g2.dispose();
    }
}
