
package piezas;

import enums.bando;
import enums.resultado;
import enums.tipoPieza;
import modelo.mover;
import modelo.posicion;

public class piezaMovil extends pieza implements mover {
    public piezaMovil(String nombre, int rango, bando bando) {
        super(nombre, rango, bando, tipoPieza.NORMAL);
    }

    @Override
    public resultado atacar(pieza defensora) {
        if (defensora.getTipo() == tipoPieza.BOMBA) {
            return resultado.PIERDE;
        }
        if (defensora.getTipo() == tipoPieza.TIERRA) {
            return resultado.GANA;
        }
        if (rango > defensora.getRango()) {
            return resultado.GANA;
        }
        if (rango < defensora.getRango()) {
            return resultado.PIERDE;
        }
        return resultado.EMPATE;
    }

    @Override
    public boolean movimientoPermitido(posicion origen, posicion destino) {
        int df = Math.abs(origen.getFila() - destino.getFila());
        int dc = Math.abs(origen.getColumna() - destino.getColumna());
        return df + dc == 1;
    }

    @Override
    public String getEtiqueta() {
        return nombre + " (" + rango + ")";
    }
}
