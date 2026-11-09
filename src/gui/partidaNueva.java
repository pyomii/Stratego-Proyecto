
package gui;

import datos.jugador;
import datos.registrar;
import enums.bando;
import enums.modoJuego;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class partidaNueva extends JDialog {

    private static final int COL1_X = 300, COL2_X = 430;
    private static final int LADO_Y = 136;
    private static final int MODO_Y = 217;
    private static final int RIVAL_Y = 301;
    private static final int ETIQUETA_X = 190;
    private static final int ACCEPT_X = 230, BACK_X = 450, BOTONES_Y = 372;
 
    private final menuPrincipal menu;
    private final jugador actual;
    private final registrar reg;
 
    private bando lado = bando.HEROES;
    private modoJuego modo = modoJuego.TUTORIAL;
 
    private final botonOpcion btnHeroes = new botonOpcion("Heroes", COL1_X, LADO_Y);
    private final botonOpcion btnVillanos = new botonOpcion("Villains", COL2_X, LADO_Y);
    private final botonOpcion btnTutorial = new botonOpcion("Tutorial", COL1_X, MODO_Y);
    private final botonOpcion btnClasico = new botonOpcion("Classic", COL2_X, MODO_Y);
    private final JComboBox<jugador> cmbRival = new JComboBox<>();
    private final JLabel estado = new JLabel("", SwingConstants.CENTER);
 
    public partidaNueva(menuPrincipal menu, registrar reg, jugador actual) {
        super(menu, "New Game", true);
        this.menu = menu;
        this.reg = reg;
        this.actual = actual;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(800, 500));
        setContentPane(crearPanel());
        pack();
        setLocationRelativeTo(menu);
    }
 
    private JPanel crearPanel() {
        panelFondo panel = new panelFondo("bg_pnueva.jpg", 800, 500);
 
        btnHeroes.addActionListener(e -> elegirLado(bando.HEROES));
        btnVillanos.addActionListener(e -> elegirLado(bando.VILLANOS));
 
        btnTutorial.setToolTipText("All pieces are visible.");
        btnClasico.setToolTipText("The opponent's pieces stay hidden.");
        btnTutorial.addActionListener(e -> elegirModo(modoJuego.TUTORIAL));
        btnClasico.addActionListener(e -> elegirModo(modoJuego.CLASICO));
 
        panel.add(new etiquetaFondo("Opponent:", ETIQUETA_X, RIVAL_Y, 110, 34));
        ArrayList<jugador> rivales = reg.getOponentesDe(actual);
        for (jugador j : rivales) {
            cmbRival.addItem(j);
        }
        cmbRival.setBounds(COL1_X, RIVAL_Y, 250, 34);
        cmbRival.setFont(new Font("SansSerif", Font.BOLD, 16));
        cmbRival.setBackground(new Color(200, 225, 255));
        cmbRival.setForeground(new Color(15, 40, 100));

        estado.setBounds(COL1_X, RIVAL_Y + 40, 250, 24);
        estado.setFont(new Font("SansSerif", Font.BOLD, 14));
        estado.setForeground(new Color(190, 30, 40));

        botonImagen btnStart = new botonImagen("Start", "btn_peq.png", 114, 40, 22f);
        btnStart.setLocation(ACCEPT_X, BOTONES_Y);
        btnStart.addActionListener(e -> iniciar());
        botonImagen btnBack = new botonImagen("Back", "btn_peq.png", 114, 40, 22f);
        btnBack.setLocation(BACK_X, BOTONES_Y);
        btnBack.addActionListener(e -> dispose());
 
        panel.add(btnHeroes);
        panel.add(btnVillanos);
        panel.add(btnTutorial);
        panel.add(btnClasico);
        panel.add(cmbRival);
        panel.add(estado);
        panel.add(btnStart);
        panel.add(btnBack);
        getRootPane().setDefaultButton(btnStart);
 
        elegirLado(lado);
        elegirModo(modo);
        return panel;
    }
 
    private void elegirLado(bando b) {
        lado = b;
        btnHeroes.setMarcado(b == bando.HEROES);
        btnVillanos.setMarcado(b == bando.VILLANOS);
    }
 
    private void elegirModo(modoJuego m) {
        modo = m;
        btnTutorial.setMarcado(m == modoJuego.TUTORIAL);
        btnClasico.setMarcado(m == modoJuego.CLASICO);
    }
 
    private void iniciar() {
        jugador rival = (jugador) cmbRival.getSelectedItem();
        if (rival == null) {
            estado.setText("Pick an opponent first.");
            return;
        }
        dispose();
        menu.iniciarPartida(lado, rival, modo);
    }
 
    private static class botonOpcion extends botonImagen {
 
        private boolean marcado = false;
 
        botonOpcion(String texto, int x, int y) {
            super(texto, "btn_peq.png", 114, 40, 22f);
            setLocation(x, y);
        }
 
        void setMarcado(boolean m) {
            marcado = m;
            repaint();
        }
 
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (marcado) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(5, 15, 45, 130));
                g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                g2.setColor(new Color(255, 255, 255, 140));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);
                g2.dispose();
            }
        }
    }
 
    private static class etiquetaFondo extends JComponent {
 
        private final String texto;
 
        etiquetaFondo(String texto, int x, int y, int ancho, int alto) {
            this.texto = texto;
            setBounds(x, y, ancho, alto);
        }
 
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(recursos.fuente(20f));
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(new Color(5, 20, 70, 160));
            g2.drawString(texto, 2, y + 2);
            g2.setColor(new Color(150, 185, 240));
            g2.drawString(texto, 0, y);
            g2.dispose();
        }
    }
}