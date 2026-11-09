
package piezas;

import enums.bando;
import enums.resultado;
import enums.tipoPieza;

public class explorador extends piezaMovil {
 
    public explorador(String nombre, bando bando) {
        super(nombre, 3, bando);
    }
 
    @Override
    public resultado atacar(pieza defensora) {
        if (defensora.getTipo() == tipoPieza.BOMBA) {
            return resultado.GANA;
        }
        return super.atacar(defensora);
    }
}
