
package modelo;

import enums.resultado;
import java.util.ArrayList;
import piezas.pieza;

public class jugada {

    private final pieza atacante;
    private final pieza defensora;
    private final resultado resultado;

    private jugada(pieza atacante, pieza defensora, resultado resultado) {
        this.atacante = atacante;
        this.defensora = defensora;
        this.resultado = resultado;
    }

    public static jugada movimiento(pieza p) {
        return new jugada(p, null, null);
    }

    public static jugada combate(pieza atacante, pieza defensora, resultado r) {
        return new jugada(atacante, defensora, r);
    }

    public boolean esCombate() {
        return defensora != null;
    }

    public pieza getAtacante() {
        return atacante;
    }

    public pieza getDefensora() {
        return defensora;
    }

    public resultado getResultado() {
        return resultado;
    }

    public boolean isCapturaTierra() {
        return esCombate() && resultado == enums.resultado.GANA
                && defensora.getTipo() == enums.tipoPieza.TIERRA;
    }

    public ArrayList<pieza> getEliminadas() {
        ArrayList<pieza> lista = new ArrayList<>();
        if (!esCombate()) {
            return lista;
        }
        switch (resultado) {
            case GANA -> lista.add(defensora);
            case PIERDE -> lista.add(atacante);
            case EMPATE -> {
                lista.add(atacante);
                lista.add(defensora);
            }
        }
        return lista;
    }
}
