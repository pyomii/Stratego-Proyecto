
package gui;
 
import enums.bando;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import piezas.pieza;

public class imagenes {
 
    private static final String CARPETA = "/imagenes/";
    private static final HashMap<String, ImageIcon> cache = new HashMap<>();
 
    private static String prefijo(bando b) {
        return (b == bando.HEROES) ? "h_" : "v_";
    }
 
    public static String archivoDe(pieza p) {
        String pre = prefijo(p.getBando());
        return switch (p.getTipo()) {
            case BOMBA -> pre + "bomba.png";
            case TIERRA -> pre + "tierra.png";
            case NORMAL -> pre + normalizar(p.getNombre()) + ".png";
        };
    }
 
    public static String archivoOculta(bando b) {
        return prefijo(b) + "oc.png";
    }
 
    static String normalizar(String texto) {
        String s = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        return s;
    }
 
    public static ImageIcon iconoPieza(pieza p, boolean visible, int tam) {
        if (!visible) {
            return icono(archivoOculta(p.getBando()), tam, p.getBando(), "?");
        }
        String etiqueta = switch (p.getTipo()) {
            case BOMBA -> "B";
            case TIERRA -> "T";
            case NORMAL -> String.valueOf(p.getRango());
        };
        return icono(archivoDe(p), tam, p.getBando(), etiqueta);
    }
 
    public static ImageIcon icono(String archivo, int tam, bando b, String etiquetaRespaldo) {
        String clave = archivo + "@" + tam;
        ImageIcon guardado = cache.get(clave);
        if (guardado != null) {
            return guardado;
        }
        ImageIcon icono = null;
        try {
            URL url = imagenes.class.getResource(CARPETA + archivo);
            if (url != null) {
                ImageIcon original = new ImageIcon(url);
                if (original.getIconWidth() > 0) {
                    Image esc = original.getImage().getScaledInstance(tam, tam, Image.SCALE_SMOOTH);
                    icono = new ImageIcon(esc);
                }
            }
        } catch (Exception e) {
            icono = null; 
        }
        if (icono == null) {
            icono = new ImageIcon(dibujarRespaldo(tam, b, etiquetaRespaldo));
        }
        cache.put(clave, icono);
        return icono;
    }
 
    private static BufferedImage dibujarRespaldo(int tam, bando b, String texto) {
        BufferedImage img = new BufferedImage(tam, tam, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(b == bando.HEROES ? new Color(30, 80, 170) : new Color(40, 120, 50));
        g.fillRoundRect(1, 1, tam - 2, tam - 2, 10, 10);
        g.setColor(Color.WHITE);
        g.drawRoundRect(1, 1, tam - 3, tam - 3, 10, 10);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(10, tam / 3)));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(texto, (tam - fm.stringWidth(texto)) / 2,
                (tam - fm.getHeight()) / 2 + fm.getAscent());
        g.dispose();
        return img;
    }
}
