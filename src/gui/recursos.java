
package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class recursos {

    private static final String CARPETA = "/imagenes/";
    private static final String ARCHIVO_FUENTE = "Avengeance.otf";
    private static final HashMap<String, BufferedImage> cache = new HashMap<>();
    private static Font fuenteBase = null;
    
    private static URL buscar(String nombre) {
        int p = nombre.lastIndexOf('.');
        String base = nombre.substring(0, p);
        String ext = nombre.substring(p);
        String[] candidatos = {nombre, base + ext.toLowerCase(), base + ext.toUpperCase()};
        for (String c : candidatos) {
            URL url = recursos.class.getResource(CARPETA + c);
            if (url != null) {
                return url;
            }
        }
        return null;
    }

    public static BufferedImage imagen(String nombre) {
        BufferedImage img = cache.get(nombre);
        if (img != null) {
            return img;
        }
        try {
            URL url = buscar(nombre);
            if (url != null) {
                img = ImageIO.read(url);
            }
        } catch (Exception e) {
            img = null;
        }
        if (img == null) {
            img = new BufferedImage(120, 40, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g = img.createGraphics();
            g.setColor(new Color(20, 60, 140));
            g.fillRect(0, 0, 120, 40);
            g.dispose();
            System.err.println("No se encontro la imagen: " + nombre);
        }
        cache.put(nombre, img);
        return img;
    }

    public static java.awt.Image icono() {
        return imagen("loguito.png");
    }

    public static Font fuente(float tam) {
        if (fuenteBase == null) {
            try (InputStream in = recursos.class.getResourceAsStream(CARPETA + ARCHIVO_FUENTE)) {
                fuenteBase = Font.createFont(Font.TRUETYPE_FONT, in);
            } catch (Exception e) {
                fuenteBase = new Font("SansSerif", Font.BOLD, 12);
                System.err.println("No se pudo cargar la fuente " + ARCHIVO_FUENTE);
            }
        }
        return fuenteBase.deriveFont(Font.PLAIN, tam);
    }
}
