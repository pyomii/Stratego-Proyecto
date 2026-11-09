package gui;

import datos.jugador;
import datos.registrar;
import java.awt.*;
import javax.swing.*;

public class ajustes extends JDialog {

    static final String FONDO = "bg_sets.jpg";
    static final int ANCHO = 1280, ALTO = 720;
    private static final int BTN_W = 250, BTN_H = 60;
    private static final int BTN_Y0 = 120, BTN_PASO = 80;
    private static final double LINEA_X0 = 452.0;
    private static final double LINEA_PENDIENTE = -0.2914;
    private static final int BTN_MARGEN = 12;
    private static final int BACK_W = 170, BACK_H = 50;
    private static final int BACK_Y = 620;
    private static final int CONTENIDO_X = 665, CONTENIDO_Y = 105;
    private static final int CONTENIDO_W = 530, CONTENIDO_H = 510;

    private final menuPrincipal menu;
    private final registrar reg;
    private final jugador actual;

    private final JPanel contenido = new JPanel(null);

    public ajustes(menuPrincipal menu, registrar reg, jugador actual) {
        super(menu, "Settings", true);
        this.menu = menu;
        this.reg = reg;
        this.actual = actual;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(ANCHO, ALTO));
        setContentPane(crearPanel());
        pack();
        setLocationRelativeTo(menu);
    }

    private JPanel crearPanel() {
        panelFondo panel = new panelFondo(FONDO, ANCHO, ALTO);

        contenido.setOpaque(false);
        contenido.setBounds(CONTENIDO_X, CONTENIDO_Y, CONTENIDO_W, CONTENIDO_H);

        botonImagen btnClave = opcion("Change Password", 0);
        btnClave.addActionListener(e -> mostrar(new cambiarClave(reg, actual)));

        botonImagen btnEliminar = opcion("Delete Account", 1);
        btnEliminar.addActionListener(e ->
                mostrar(new eliminarCuenta(reg, actual, menu, this)));

        botonImagen btnPerfil = opcion("My Profile", 2);
        btnPerfil.addActionListener(e -> mostrar(new miPerfil(reg, actual)));

        botonImagen btnRanking = opcion("Ranking", 3);
        btnRanking.addActionListener(e -> mostrar(new rankingGui(reg, actual)));

        botonImagen btnStats = opcion("Statistics", 4);
        btnStats.addActionListener(e -> mostrar(new estadisticasGui(reg)));

        botonImagen btnBack = new botonImagen("Back", "btn_peq.png", BACK_W, BACK_H, 24f);
        btnBack.setLocation(xParaLinea(BACK_Y + BACK_H / 2, BACK_W), BACK_Y);
        btnBack.addActionListener(e -> dispose());

        panel.add(contenido);
        panel.add(btnClave);
        panel.add(btnEliminar);
        panel.add(btnPerfil);
        panel.add(btnRanking);
        panel.add(btnStats);
        panel.add(btnBack);
        return panel;
    }

    private void mostrar(JComponent vista) {
        contenido.removeAll();
        vista.setBounds(0, 0, CONTENIDO_W, CONTENIDO_H);
        contenido.add(vista);
        contenido.revalidate();
        contenido.repaint();
    }

    private botonImagen opcion(String texto, int fila) {
        botonImagen b = new botonImagen(texto, "btn_gde.png", BTN_W, BTN_H, 24f);
        int y = BTN_Y0 + fila * BTN_PASO;
        b.setLocation(xParaLinea(y + BTN_H / 2, BTN_W), y);
        return b;
    }

    private static int xParaLinea(int centroY, int ancho) {
        int bordeDerecho = (int) Math.round(LINEA_X0 + LINEA_PENDIENTE * centroY);
        return bordeDerecho - BTN_MARGEN - ancho;
    }
}
