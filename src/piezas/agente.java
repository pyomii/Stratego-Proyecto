
package piezas;

import enums.bando;
import enums.resultado;
import enums.tipoPieza;
 
public class agente extends piezaMovil {
 
    public agente(String nombre, bando bando) {
        super(nombre, 1, bando);
    }
 
    @Override
    public resultado atacar(pieza defensora) {
        if (defensora.getTipo() == tipoPieza.NORMAL && defensora.getRango() == 10) {
            return resultado.GANA;
        }
        return super.atacar(defensora);
    }
}
