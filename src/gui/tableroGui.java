package gui;

import datos.log;
import enums.bando;
import enums.modoJuego;
import excepciones.movimientoInvalido;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;
import modelo.jugada;
import modelo.partida;
import modelo.posicion;
import modelo.tablero;
import piezas.pieza;

public class tableroGui extends JFrame {

    private static final int[] XS = {93, 155, 217, 280, 342, 406, 471, 534, 599, 662, 722};
    private static final int[] YS = {54, 114, 175, 236, 297, 360, 423, 485, 545, 606, 665};
    private static final int TAM_PIEZA = 52;
    private static final int TAM_VENCIDA = 38;

    private static final int VENC_X = 812, VENC_W = 424, VENC_H = 244;
    private static final int VENC_VILLANOS_Y = 100, VENC_HEROES_Y = 420;
 
    private static final int TURNO_X = 93, TURNO_Y = 6, TURNO_W = 629, TURNO_H = 42;
    private static final int RETIRO_X = 1131, RETIRO_Y = 7;
 
    private final partida juego;
    private final Runnable alCerrar;
    private final panelFondo fondo;
    private final JButton[][] celdas = new JButton[tablero.TAMANO][tablero.TAMANO];
    private final etiquetaTurno lblTurno = new etiquetaTurno();
    private final JPanel panelVencHeroes = nuevoPanelVencidas(VENC_HEROES_Y);
    private final JPanel panelVencVillanos = nuevoPanelVencidas(VENC_VILLANOS_Y);
 
    private posicion seleccion = null;
    private boolean cubierto = false;
 
    public tableroGui(partida juego, Runnable alCerrar) {
        this.juego = juego;
        this.alCerrar = alCerrar;
 
        setTitle("Heroes: " + juego.getJugador(bando.HEROES).getUsername()
                + "  vs  Villanos: " + juego.getJugador(bando.VILLANOS).getUsername()
                + "  [" + juego.getModo().getNombre() + "]");
        setIconImage(recursos.icono());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(1280, 720));
 
        fondo = new panelFondo("bg_juego.JPG", 1280, 720);
 
        for (int f = 0; f < tablero.TAMANO; f++) {
            for (int c = 0; c < tablero.TAMANO; c++) {
                JButton b = new JButton();
                b.setBounds(XS[c] + 2, YS[f] + 2, XS[c + 1] - XS[c] - 3, YS[f + 1] - YS[f] - 3);
                b.setMargin(new Insets(0, 0, 0, 0));
                b.setOpaque(false);
                b.setContentAreaFilled(false);
                b.setFocusPainted(false);
                b.setBorder(BorderFactory.createEmptyBorder());
                final int ff = f, cc = c;
                b.addActionListener(e -> clic(ff, cc));
                celdas[f][c] = b;
                fondo.add(b);
            }
        }
 
        lblTurno.setBounds(TURNO_X, TURNO_Y, TURNO_W, TURNO_H);
        fondo.add(lblTurno);
 
        botonImagen btnRetiro = new botonImagen("Retiro", "btn_peq.png", 114, 40, 22f);
        btnRetiro.setLocation(RETIRO_X, RETIRO_Y);
        btnRetiro.addActionListener(e -> retiro());
        fondo.add(btnRetiro);
 
        fondo.add(panelVencHeroes);
        fondo.add(panelVencVillanos);
 
        setContentPane(fondo);
 
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                retiro();
            }
 
            @Override
            public void windowClosed(WindowEvent e) {
                alCerrar.run();
            }
        });
 
        refrescar();
        pack();
        setLocationRelativeTo(null);
    }
 
    private static JPanel nuevoPanelVencidas(int y) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
        p.setOpaque(false);
        p.setBounds(VENC_X, y, VENC_W, VENC_H);
        return p;
    }
 
    private void clic(int f, int c) {
        if (cubierto || !juego.estaEnCurso() || tablero.esZonaProhibida(f, c)) {
            return;
        }
        pieza p = juego.getTablero().getPieza(f, c);
        boolean esMia = p != null && p.getBando() == juego.getTurno();
 
        if (seleccion == null) {
            if (esMia) {
                seleccion = new posicion(f, c);
            }
        } else if (seleccion.equals(new posicion(f, c))) {
            seleccion = null;
        } else if (esMia) {
            seleccion = new posicion(f, c);
        } else {
            posicion origen = seleccion;
            seleccion = null;
            jugar(origen, new posicion(f, c));
            return;
        }
        refrescar();
    }
 
    private void jugar(posicion origen, posicion destino) {
        bando quien = juego.getTurno();
        try {
            jugada j = juego.jugar(origen, destino);
            refrescar();

            if (j.esCombate()) {
                aviso.mostrar(this, "Combate!", textoCombate(j, quien),
                        new String[]{"OK"}, i -> despuesDelTurno());
            } else {
                despuesDelTurno();
            }
        } catch (movimientoInvalido ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void despuesDelTurno() {
        if (!juego.estaEnCurso()) {
            fin();
            return;
        }
        if (juego.getModo() == modoJuego.CLASICO) {
            cubierto = true;
            refrescar();
            String texto = "Turno de " + juego.getJugadorEnTurno().getUsername()
                    + " (" + juego.getTurno().getNombre() + ")."
                    + "\nPasa la computadora y presiona Continuar.";
            aviso.mostrar(this, "Cambio de turno", texto,
                    new String[]{"Continuar"}, i -> {
                        cubierto = false;
                        refrescar();
                    });
        }
    }

    private void mostrarError(String mensaje) {
        lblTurno.set(mensaje, new Color(255, 120, 120));
    }
 
    private String textoCombate(jugada j, bando atacante) {
        String quien = j.getAtacante().getEtiqueta() + " (" + atacante.getNombre() + ")";
        String rival = j.getDefensora().getEtiqueta() + " (" + atacante.opuesto().getNombre() + ")";
        String res = switch (j.getResultado()) {
            case GANA -> "Gana " + quien + ".\n" + rival + " es eliminada.";
            case PIERDE -> "Gana " + rival + ".\n" + quien + " es eliminada.";
            case EMPATE -> "Empate: ambas piezas son eliminadas.";
        };
        return quien + "\nataca a\n" + rival + "\n\n" + res;
    }
 
    private void retiro() {
        if (!juego.estaEnCurso()) {
            dispose();
            return;
        }
        aviso.mostrar(this, "Retiro",
                "Seguro que quieres retirarte? Tu oponente gana 3 puntos.",
                new String[]{"Si", "No"}, i -> {
                    if (i == 0) {
                        juego.retirar();
                        fin();
                    }
                });
    }
 
    private void fin() {
        refrescar();
        log l = juego.getLogFinal();
        aviso.mostrar(this, "Fin de la partida", l.toString(),
                new String[]{"Cerrar"}, i -> dispose());
    }
 
    private void refrescar() {
        tablero t = juego.getTablero();
        for (int f = 0; f < tablero.TAMANO; f++) {
            for (int c = 0; c < tablero.TAMANO; c++) {
                JButton b = celdas[f][c];
                b.setBorder(BorderFactory.createEmptyBorder());
                b.setToolTipText(null);
 
                if (tablero.esZonaProhibida(f, c)) {
                    b.setIcon(null);
                    continue;
                }
                pieza p = t.getPieza(f, c);
                if (p == null) {
                    b.setIcon(null);
                } else {
                    boolean visible = !cubierto && juego.esVisible(p);
                    int tam = (int) Math.round(TAM_PIEZA * fondo.getEscala());
                    b.setIcon(imagenes.iconoPieza(p, visible, tam));
                    if (visible) {
                        b.setToolTipText(p.getEtiqueta() + " - " + p.getBando().getNombre());
                    }
                }
                if (seleccion != null && seleccion.getFila() == f && seleccion.getColumna() == c) {
                    b.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                }
            }
        }
 
        if (juego.estaEnCurso()) {
            bando turno = juego.getTurno();
            String lado = (turno == bando.HEROES) ? "Heroes" : "Villanos";
            lblTurno.set("Turno: " + lado + " - " + juego.getJugadorEnTurno().getUsername(),
                    turno == bando.HEROES ? new Color(120, 190, 255) : new Color(140, 230, 140));
        } else {
            lblTurno.set("Partida terminada", Color.WHITE);
        }
 
        llenarVencidas(panelVencHeroes, juego.getVencidas(bando.HEROES));
        llenarVencidas(panelVencVillanos, juego.getVencidas(bando.VILLANOS));
        repaint();
    }
 
    private void llenarVencidas(JPanel panel, ArrayList<pieza> lista) {
        panel.removeAll();
        int tam = (int) Math.round(TAM_VENCIDA * fondo.getEscala());
        for (pieza p : lista) {
            JLabel l = new JLabel(imagenes.iconoPieza(p, true, tam));
            l.setToolTipText(p.getEtiqueta());
            panel.add(l);
        }
        panel.revalidate();
        panel.repaint();
    }
 
    private static class etiquetaTurno extends JComponent {
 
        private String texto = "";
        private Color color = Color.WHITE;
 
        void set(String texto, Color color) {
            this.texto = texto;
            this.color = color;
            repaint();
        }
 
        private static boolean fuenteSoportaTodo(String t) {
            for (char c : t.toCharArray()) {
                boolean ok = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
                        || (c >= '0' && c <= '9') || c == ' ' || c == ':' || c == '-';
                if (!ok) {
                    return false;
                }
            }
            return true;
        }
 
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
 
            Font f = recursos.fuente(28f);
            if (!fuenteSoportaTodo(texto)) {
                f = new Font("SansSerif", Font.BOLD, 22);
            }
            FontMetrics fm = g2.getFontMetrics(f);
            while (fm.stringWidth(texto) > getWidth() && f.getSize2D() > 12f) {
                f = f.deriveFont(f.getSize2D() - 2f);
                fm = g2.getFontMetrics(f);
            }
            g2.setFont(f);
            int x = (getWidth() - fm.stringWidth(texto)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(new Color(5, 20, 70, 160));
            g2.drawString(texto, x + 2, y + 2);
            g2.setColor(color);
            g2.drawString(texto, x, y);
            g2.dispose();
        }
    }
}
