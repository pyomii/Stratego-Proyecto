
package modelo;
 
import datos.jugador;
import datos.log;
import datos.registrar;
import enums.bando;
import enums.modoJuego;
import enums.tipoFin;
import enums.tipoPieza;
import excepciones.movimientoInvalido;
import java.util.ArrayList;
import piezas.pieza;

public class partida {

    private final registrar reg;
    private final tablero tab;
    private final jugador jugadorHeroes;
    private final jugador jugadorVillanos;
    private final modoJuego modo;

    private bando turno = bando.HEROES;
    private boolean enCurso = true;

    private final ArrayList<pieza> vencidasHeroes = new ArrayList<>();
    private final ArrayList<pieza> vencidasVillanos = new ArrayList<>();

    private log logFinal;
 
    public partida(registrar reg, tablero tablero, jugador jugadorHeroes,
            jugador jugadorVillanos, modoJuego modo) {
        if (jugadorHeroes == jugadorVillanos) {
            throw new IllegalArgumentException("Se necesitan dos jugadores distintos.");
        }
        this.reg = reg;
        this.tab = tablero;
        this.jugadorHeroes = jugadorHeroes;
        this.jugadorVillanos = jugadorVillanos;
        this.modo = modo;
        validarFormacion(bando.HEROES);
        validarFormacion(bando.VILLANOS);
    }
    
    private void validarFormacion(bando b) {
        int tierras = 0;
        int moviles = 0;
        for (int f = 0; f < tablero.TAMANO; f++) {
            for (int c = 0; c < tablero.TAMANO; c++) {
                pieza p = tab.getPieza(f, c);
                if (p != null && p.getBando() == b) {
                    if (p.getTipo() == tipoPieza.TIERRA) {
                        tierras++;
                    } else if (p.getTipo().esMovil()) {
                        moviles++;
                    }
                }
            }
        }
        if (tierras != 1 || moviles == 0) {
            throw new IllegalStateException("La formacion de " + b.getNombre() + " esta incompleta.");
        }
    }
 
    public jugada jugar(posicion origen, posicion destino) throws movimientoInvalido {
        if (!enCurso) {
            throw new movimientoInvalido("La partida ya termino.");
        }
        jugada j = tab.ejecutar(origen, destino, turno);
        for (pieza p : j.getEliminadas()) {
            vencidasDe(p.getBando()).add(p);
        }
        if (j.isCapturaTierra()) {
            terminar(turno, tipoFin.CAPTURA_TIERRA);
        } else if (!tab.tieneMovimientos(turno.opuesto())) {
            terminar(turno, tipoFin.SIN_MOVIMIENTOS);
        } else {
            turno = turno.opuesto();
        }
        return j;
    }
 
    public void retirar() {
        if (!enCurso) {
            throw new IllegalStateException("La partida ya termino.");
        }
        terminar(turno.opuesto(), tipoFin.RETIRO);
    }

    private void terminar(bando ganador, tipoFin tipo) {
        logFinal = reg.registrarPartida(getJugador(ganador), getJugador(ganador.opuesto()),
                ganador, tipo);
        enCurso = false;
    }

    public boolean esVisible(pieza p) {
        if (!enCurso) {
            return true;
        }
        return switch (modo) {
            case TUTORIAL -> true;
            case CLASICO -> p.isRevelada() || p.getBando() == turno;
        };
    }
 
    private ArrayList<pieza> vencidasDe(bando b) {
        return (b == bando.HEROES) ? vencidasHeroes : vencidasVillanos;
    }
 
    public ArrayList<pieza> getVencidas(bando b) {
        return new ArrayList<>(vencidasDe(b));
    }
 
    public jugador getJugador(bando b) {
        return (b == bando.HEROES) ? jugadorHeroes : jugadorVillanos;
    }
 
    public jugador getJugadorEnTurno() {
        return getJugador(turno);
    }
 
    public bando getTurno() {
        return turno;
    }

    public boolean estaEnCurso() {
        return enCurso;
    }
 
    public modoJuego getModo() {
        return modo;
    }
 
    public tablero getTablero() {
        return tab;
    }
 
    public log getLogFinal() {
        return logFinal;
    }
}
