
package piezas;

import enums.bando;
import enums.tipoPieza;
import modelo.atacar;

public abstract class pieza implements atacar {

    protected final String nombre;
    protected final int rango;
    protected final bando bando;
    protected final tipoPieza tipo;
    private boolean revelada;

    protected pieza(String nombre, int rango, bando bando, tipoPieza tipo) {
        this.nombre = nombre;
        this.rango = rango;
        this.bando = bando;
        this.tipo = tipo;
        this.revelada = false;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRango() {
        return rango;
    }

    public bando getBando() {
        return bando;
    }

    public tipoPieza getTipo() {
        return tipo;
    }

    public boolean isRevelada() { 
        return revelada; 
    }
    
    public void revelar() { 
        revelada = true; 
    }
    
    public abstract String getEtiqueta();
}
