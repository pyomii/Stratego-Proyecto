
package piezas;

import enums.bando;
import modelo.posicion;

public class mistico extends piezaMovil {
 
    public mistico(String nombre, bando bando) {
        super(nombre, 2, bando);
    }
 
    @Override
    public boolean movimientoPermitido(posicion origen, posicion destino) {
        int df = Math.abs(origen.getFila() - destino.getFila());
        int dc = Math.abs(origen.getColumna() - destino.getColumna());
        return (df == 0 && dc > 0) || (dc == 0 && df > 0);
    }
}
