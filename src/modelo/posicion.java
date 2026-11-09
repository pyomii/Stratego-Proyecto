
package modelo;

public class posicion {
    private final int fila;
    private final int columna;

    public posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof posicion)) {
            return false;
        }
        posicion p = (posicion) otro;
        return fila == p.fila && columna == p.columna;
    }

    @Override
    public String toString() {
        return "(" + fila + ", " + columna + ")";
    }
}
