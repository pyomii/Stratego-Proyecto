
package modelo;

import enums.bando;
import enums.resultado;
import excepciones.movimientoInvalido;
import piezas.pieza;

public class tablero {

    public static final int TAMANO = 10;

    private static final int[] DF = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    private final pieza[][] casillas = new pieza[TAMANO][TAMANO];

    public static boolean dentro(int fila, int col) {
        return fila >= 0 && fila < TAMANO && col >= 0 && col < TAMANO;
    }

    public static boolean esZonaProhibida(int fila, int col) {
        boolean filasLago = (fila == 4 || fila == 5);
        boolean colsLago = (col == 2 || col == 3 || col == 6 || col == 7);
        return filasLago && colsLago;
    }

    public pieza getPieza(int fila, int col) {
        return dentro(fila, col) ? casillas[fila][col] : null;
    }

    public pieza getPieza(posicion p) {
        return getPieza(p.getFila(), p.getColumna());
    }

    public boolean estaVacia(int fila, int col) {
        return dentro(fila, col) && !esZonaProhibida(fila, col) && casillas[fila][col] == null;
    }

    public void colocar(pieza p, int fila, int col) {
        if (!dentro(fila, col)) {
            throw new IllegalArgumentException("Fuera del tablero: (" + fila + ", " + col + ")");
        }
        if (esZonaProhibida(fila, col)) {
            throw new IllegalArgumentException("Zona prohibida: (" + fila + ", " + col + ")");
        }
        if (casillas[fila][col] != null) {
            throw new IllegalArgumentException("Casilla ocupada: (" + fila + ", " + col + ")");
        }
        casillas[fila][col] = p;
    }

    public pieza quitar(int fila, int col) {
        if (!dentro(fila, col)) {
            return null;
        }
        pieza p = casillas[fila][col];
        casillas[fila][col] = null;
        return p;
    }
    public jugada ejecutar(posicion origen, posicion destino, bando turno)
            throws movimientoInvalido {
        if (!dentro(origen.getFila(), origen.getColumna())
                || !dentro(destino.getFila(), destino.getColumna())) {
            throw new movimientoInvalido("La casilla esta fuera del tablero.");
        }
        pieza mov = getPieza(origen);
        if (mov == null) {
            throw new movimientoInvalido("No hay ninguna pieza en " + origen + ".");
        }
        if (mov.getBando() != turno) {
            throw new movimientoInvalido("Esa pieza no es de tu equipo.");
        }
        if (!mov.getTipo().esMovil()) {
            throw new movimientoInvalido("Las bombas y la Tierra no se pueden mover.");
        }
        if (esZonaProhibida(destino.getFila(), destino.getColumna())) {
            throw new movimientoInvalido("No se puede entrar a una zona prohibida.");
        }
        pieza objetivo = getPieza(destino);
        if (objetivo != null && objetivo.getBando() == turno) {
            throw new movimientoInvalido("Esa casilla esta ocupada por una pieza propia.");
        }
        if (!(mov instanceof mover m) || !m.movimientoPermitido(origen, destino)) {
            throw new movimientoInvalido(
                    mov.getNombre() + " no puede moverse de " + origen + " a " + destino + ".");
        }

        int df = destino.getFila() - origen.getFila();
        int dc = destino.getColumna() - origen.getColumna();
        int pasos = Math.abs(df) + Math.abs(dc);

        if (pasos > 1) {
            if (objetivo != null) {
                throw new movimientoInvalido(
                        "Solo se puede atacar a una pieza adyacente.");
            }
            if (!caminoDespejado(origen.getFila(), origen.getColumna(),
                    Integer.signum(df), Integer.signum(dc), pasos)) {
                throw new movimientoInvalido("El camino esta bloqueado.");
            }
            mov.revelar();
        }
        
        if (objetivo == null) {
            casillas[destino.getFila()][destino.getColumna()] = mov;
            casillas[origen.getFila()][origen.getColumna()] = null;
            return jugada.movimiento(mov);
        }

        mov.revelar();
        objetivo.revelar();
        resultado r = mov.atacar(objetivo);
        switch (r) {
            case GANA -> {
                casillas[destino.getFila()][destino.getColumna()] = mov;
                casillas[origen.getFila()][origen.getColumna()] = null;
            }
            case PIERDE -> casillas[origen.getFila()][origen.getColumna()] = null;
            case EMPATE -> {
                casillas[origen.getFila()][origen.getColumna()] = null;
                casillas[destino.getFila()][destino.getColumna()] = null;
            }
        }
        return jugada.combate(mov, objetivo, r);
    }

    private boolean caminoDespejado(int fila, int col, int sf, int sc, int pasos) {
        for (int i = 1; i <= pasos; i++) {
            if (!estaVacia(fila + sf * i, col + sc * i)) {
                return false;
            }
        }
        return true;
    }

    public boolean tieneMovimientos(bando b) {
        for (int f = 0; f < TAMANO; f++) {
            for (int c = 0; c < TAMANO; c++) {
                pieza p = casillas[f][c];
                if (p != null && p.getBando() == b && p.getTipo().esMovil()
                        && tieneVecinoJugable(f, c, b)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean tieneVecinoJugable(int fila, int col, bando b) {
        for (int k = 0; k < 4; k++) {
            int f = fila + DF[k];
            int c = col + DC[k];
            if (!dentro(f, c) || esZonaProhibida(f, c)) {
                continue;
            }
            pieza vecina = casillas[f][c];
            if (vecina == null || vecina.getBando() != b) {
                return true;
            }
        }
        return false;
    }
}