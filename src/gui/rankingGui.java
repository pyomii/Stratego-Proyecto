package gui;

import datos.jugador;
import datos.registrar;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class rankingGui extends JPanel {

    private static final int ANCHO_LISTA = 450;
    private static final int ALTO_FILA = 54;
    private static final int ESPACIO_FILA = 6;

    public rankingGui(registrar reg, jugador actual) {
        setOpaque(false);
        setLayout(null);

        JLabel titulo = estiloSets.titulo("RANKING");
        titulo.setBounds(0, 16, 530, 45);
        add(titulo);

        JLabel subtitulo = estiloSets.etiqueta("Players ordered by points", 15);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        subtitulo.setBounds(0, 58, 530, 22);
        add(subtitulo);

        JLabel cabeceraJugador = etiquetaRanking("PLAYER", 16f, SwingConstants.LEFT);
        cabeceraJugador.setBounds(45, 86, 280, 22);
        add(cabeceraJugador);

        JLabel cabeceraPuntos = etiquetaRanking("POINTS", 16f, SwingConstants.RIGHT);
        cabeceraPuntos.setBounds(325, 86, 120, 22);
        add(cabeceraPuntos);

        ArrayList<jugador> ranking = reg.getRanking();
        if (ranking.isEmpty()) {
            JLabel vacio = estiloSets.valor("No players registered yet.", 20);
            vacio.setHorizontalAlignment(SwingConstants.CENTER);
            vacio.setBounds(0, 240, 530, 30);
            add(vacio);
            return;
        }

        JPanel lista = new JPanel(null);
        lista.setOpaque(false);
        int altoTotal = ranking.size() * (ALTO_FILA + ESPACIO_FILA);
        lista.setPreferredSize(new Dimension(ANCHO_LISTA, altoTotal));

        for (int i = 0; i < ranking.size(); i++) {
            jugador j = ranking.get(i);
            int y = i * (ALTO_FILA + ESPACIO_FILA);

            estiloSets.Tarjeta fila = estiloSets.tarjeta(colorFila(i, j == actual), 16);
            fila.setBounds(0, y, ANCHO_LISTA, ALTO_FILA);

            JLabel pos = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER);
            pos.setFont(recursos.fuente(27f));
            pos.setForeground(colorPosicion(i));
            pos.setBounds(6, 0, 52, ALTO_FILA);
            fila.add(pos);

            String nombre = j.getUsername() + (j == actual ? "" : "");
            JLabel usuario = new JLabel(nombre);
            usuario.setFont(recursos.fuente(22f));
            usuario.setForeground(estiloSets.TEXTO);
            usuario.setBounds(68, 0, 260, ALTO_FILA);
            fila.add(usuario);

            JLabel puntos = new JLabel(j.getPuntos() + " pts", SwingConstants.RIGHT);
            puntos.setFont(recursos.fuente(20f));
            puntos.setForeground(estiloSets.TEXTO_SUAVE);
            puntos.setBounds(322, 0, 112, ALTO_FILA);
            fila.add(puntos);

            lista.add(fila);
        }

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBounds(40, 112, ANCHO_LISTA, 370);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);
    }

    private static JLabel etiquetaRanking(String texto, float tam, int alineacion) {
        JLabel lbl = new JLabel(texto, alineacion);
        lbl.setFont(recursos.fuente(tam));
        lbl.setForeground(estiloSets.TEXTO_SUAVE);
        return lbl;
    }

    private static Color colorFila(int posicion, boolean esActual) {
        if (esActual) {
            return new Color(205, 232, 255, 205);
        }
        return switch (posicion) {
            case 0 -> new Color(255, 234, 145, 205);
            case 1 -> new Color(228, 236, 246, 205);
            case 2 -> new Color(242, 207, 170, 205);
            default -> (posicion % 2 == 0) ? estiloSets.BLANCO_SUAVE : estiloSets.CELESTE_SUAVE;
        };
    }

    private static Color colorPosicion(int posicion) {
        return switch (posicion) {
            case 0 -> estiloSets.ORO;
            case 1 -> estiloSets.PLATA;
            case 2 -> estiloSets.BRONCE;
            default -> estiloSets.TEXTO;
        };
    }
}
