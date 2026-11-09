
package piezas;

import enums.bando;
import enums.resultado;
import enums.tipoPieza;

public class tierrra extends pieza {
    public tierrra(bando bando) {
        super("Tierra", 0, bando, tipoPieza.TIERRA);
    }

    @Override
    public resultado atacar(pieza defensora) {
        throw new UnsupportedOperationException("La Tierra no ataca");
    }

    @Override
    public String getEtiqueta() {
        return "TIERRA";
    }
}
