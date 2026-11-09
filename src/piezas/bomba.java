
package piezas;

import enums.bando;
import enums.resultado;
import enums.tipoPieza;

public class bomba extends pieza {
    
    public bomba(String nombre, bando bando) {
        super(nombre, 0, bando, tipoPieza.BOMBA);
    }

    @Override
    public resultado atacar(pieza defensora) {
        throw new UnsupportedOperationException("Las bombas no atacan");
    }

    @Override
    public String getEtiqueta() {
        return "BOMBA";
    }
}
