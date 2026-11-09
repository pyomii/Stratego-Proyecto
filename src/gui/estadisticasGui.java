package gui;

import datos.jugador;
import datos.registrar;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import javax.swing.*;

public class estadisticasGui extends JPanel {

    public estadisticasGui(registrar reg) {
        setOpaque(false);
        setLayout(null);

        JLabel titulo = estiloSets.titulo("STATISTICS");
        titulo.setBounds(0, 14, 530, 45);
        add(titulo);

        JLabel subtitulo = estiloSets.etiqueta("Global game stats", 15);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        subtitulo.setBounds(0, 54, 530, 22);
        add(subtitulo);

        int[] e = reg.getEstadisticas();
        int activos = reg.getUsuariosActivos();
        int historicos = reg.getUsuariosHistoricos();
        int puntosTotales = puntosTotales(reg);

        tarjeta(20, 84, new Color(255, 255, 255, 135), String.valueOf(e[0]),
                "GAMES PLAYED", estiloSets.TEXTO);
        tarjeta(190, 84, new Color(210, 232, 255, 160), String.valueOf(e[1]),
                "HEROES WINS", estiloSets.HEROE);
        tarjeta(360, 84, new Color(255, 218, 222, 160), String.valueOf(e[2]),
                "VILLAINS WINS", estiloSets.VILLANO);

        JLabel lblBarra = etiquetaBarra("WIN RATE");
        lblBarra.setBounds(40, 200, 450, 22);
        add(lblBarra);

        BarraVictorias barra = new BarraVictorias(e[1], e[2]);
        barra.setBounds(40, 226, 450, 46);
        add(barra);

        tarjeta(20, 312, new Color(220, 240, 255, 145), String.valueOf(activos),
                "ACTIVE PLAYERS", estiloSets.TEXTO);
        tarjeta(190, 312, new Color(235, 240, 250, 145), String.valueOf(historicos),
                "ALL-TIME PLAYERS", estiloSets.TEXTO);
        tarjeta(360, 312, new Color(255, 242, 205, 155), String.valueOf(puntosTotales),
                "TOTAL POINTS", estiloSets.ORO);

        JLabel nota = estiloSets.etiqueta("All-time players includes deleted accounts.", 13);
        nota.setHorizontalAlignment(SwingConstants.CENTER);
        nota.setBounds(0, 432, 530, 22);
        add(nota);
    }

    private void tarjeta(int x, int y, Color fondo, String valor, String texto, Color colorValor) {
        estiloSets.Tarjeta tarjeta = estiloSets.tarjeta(fondo, 18);
        tarjeta.setBounds(x, y, 150, 100);

        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(recursos.fuente(38f));
        lblValor.setForeground(colorValor);
        lblValor.setBounds(0, 8, 150, 58);
        tarjeta.add(lblValor);

        JLabel lblTexto = new JLabel(texto, SwingConstants.CENTER);
        lblTexto.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTexto.setForeground(estiloSets.TEXTO_SUAVE);
        lblTexto.setBounds(4, 64, 142, 24);
        tarjeta.add(lblTexto);

        add(tarjeta);
    }

    private static JLabel etiquetaBarra(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 17));
        lbl.setForeground(estiloSets.TEXTO_SUAVE);
        return lbl;
    }

    private static int puntosTotales(registrar reg) {
        ArrayList<jugador> ranking = reg.getRanking();
        int total = 0;
        for (jugador j : ranking) {
            total += j.getPuntos();
        }
        return total;
    }

    private static class BarraVictorias extends JPanel {

        private final int heroes;
        private final int villanos;

        BarraVictorias(int heroes, int villanos) {
            this.heroes = heroes;
            this.villanos = villanos;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int total = heroes + villanos;
            RoundRectangle2D forma = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, 18, 18);

            g2.setColor(new Color(255, 255, 255, 135));
            g2.fill(forma);

            String texto;
            if (total == 0) {
                texto = "No games played yet.";
            } else {
                int pctHeroes = Math.round(heroes * 100f / total);
                int pctVillanos = 100 - pctHeroes;
                int anchoHeroes = Math.round(w * (heroes / (float) total));

                Shape anterior = g2.getClip();
                g2.clip(forma);
                g2.setColor(estiloSets.HEROE);
                g2.fillRect(0, 0, anchoHeroes, h);
                g2.setColor(estiloSets.VILLANO);
                g2.fillRect(anchoHeroes, 0, w - anchoHeroes, h);
                g2.setClip(anterior);

                texto = "HEROES " + pctHeroes + "%   |   VILLAINS " + pctVillanos + "%";
            }

            g2.setColor(new Color(255, 255, 255, 150));
            g2.draw(forma);

            Font fuente = recursos.fuente(17f);
            g2.setFont(fuente);
            FontMetrics fm = g2.getFontMetrics();
            int x = (w - fm.stringWidth(texto)) / 2;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();

            if (total == 0) {
                g2.setColor(estiloSets.TEXTO_SUAVE);
                g2.drawString(texto, x, y);
            } else {
                g2.setColor(new Color(0, 0, 0, 95));
                g2.drawString(texto, x + 1, y + 1);
                g2.setColor(Color.WHITE);
                g2.drawString(texto, x, y);
            }
            g2.dispose();
        }
    }
}
