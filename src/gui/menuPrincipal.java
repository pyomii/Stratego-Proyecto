package gui;

import datos.jugador;
import datos.registrar;
import enums.bando;
import enums.modoJuego;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.*;
import modelo.formacion;
import modelo.partida;
import modelo.tablero;

public class menuPrincipal extends JFrame {
 
    private static final int SETTINGS_X = 222, SETTINGS_Y = 330;
    private static final int NEWGAME_X = 185, NEWGAME_Y = 440;
    private static final int BACK_W = 114, BACK_H = 40, MARGEN = 24;
    private static final int BACK_X = MARGEN, BACK_Y = 720 - BACK_H - MARGEN;
 
    private final registrar reg;
    private final jugador actual;
    private final botonImagen btnSettings = new botonImagen("Settings", "btn_gde.png", 250, 75, 38f);
    private final botonImagen btnNewGame = new botonImagen("New Game", "btn_gde.png", 250, 75, 38f);
    private final botonImagen btnBack = new botonImagen("Back", "btn_peq.png", BACK_W, BACK_H, 22f);
 
    public menuPrincipal(registrar reg, jugador actual) {
        this.reg = reg;
        this.actual = actual;
 
        setTitle("Marvel Heroes - Stratego");
        setIconImage(recursos.icono());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(1280, 720));
        setContentPane(crearPanel());
        pack();
        setLocationRelativeTo(null);
    }
 
    private JPanel crearPanel() {
        panelFondo panel = new panelFondo("bg_menuin.jpg", 1280, 720);
 
        btnSettings.setLocation(SETTINGS_X, SETTINGS_Y);
        btnSettings.addActionListener(e -> abrirSettings());
 
        btnNewGame.setLocation(NEWGAME_X, NEWGAME_Y);
        btnNewGame.addActionListener(e -> nuevaPartida());
        if (!reg.puedeIniciarPartida()) {
            btnNewGame.setEnabled(false);
            btnNewGame.setToolTipText("You need at least 2 registered players.");
        }
 
        btnBack.setLocation(BACK_X, BACK_Y);
        btnBack.addActionListener(e -> volver());
 
        panel.add(btnSettings);
        panel.add(btnNewGame);
        panel.add(btnBack);
        return panel;
    }
 
    private void volver() {
        cerrarSesion();
    }

    void cerrarSesion() {
        dispose();
        new menuInicial(reg).setVisible(true);
    }
 
    private void abrirSettings() {
        new ajustes(this, reg, actual).setVisible(true);
    }
 
    private void nuevaPartida() {
        new partidaNueva(this, reg, actual).setVisible(true);
    }

    void iniciarPartida(bando lado, jugador rival, modoJuego modo) {
        Random rnd = new Random();
        tablero t = new tablero();
        formacion.generarAleatoria(t, bando.HEROES, rnd);
        formacion.generarAleatoria(t, bando.VILLANOS, rnd);
 
        jugador jHeroes = (lado == bando.HEROES) ? actual : rival;
        jugador jVillanos = (lado == bando.HEROES) ? rival : actual;
        partida p = new partida(reg, t, jHeroes, jVillanos, modo);

        dispose();
        new tableroGui(p, () -> new menuPrincipal(reg, actual).setVisible(true)).setVisible(true);
    }
}