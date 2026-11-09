
package datos;

import enums.bando;
import enums.tipoFin;
import excepciones.credencialesInvalidas;
import excepciones.passwordInvalido;
import excepciones.usuarioDuplicado;
import java.util.ArrayList;

public class registrar {
    public static final int LARGO_PASSWORD = 5;
    public static final int PUNTOS_VICTORIA = 3;
    public static final int PUNTOS_RETIRO = 3;
 
    private final ArrayList<jugador> jugadores = new ArrayList<>();
    private final ArrayList<log> logsGlobales = new ArrayList<>();
    private int usuariosHistoricos = 0;

    public jugador buscar(String username) {
        for (jugador j : jugadores) {
            if (j.getUsername().equalsIgnoreCase(username)) {
                return j;
            }
        }
        return null;
    }
 
    public static void validarPassword(String password) throws passwordInvalido {
        if (password == null || password.length() != LARGO_PASSWORD) {
            throw new passwordInvalido(
                    "El password debe tener exactamente " + LARGO_PASSWORD + " caracteres.");
        }
    }
 
    public jugador crearJugador(String username, String password)
            throws usuarioDuplicado, passwordInvalido {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username no puede estar vacio.");
        }
        username = username.trim();
        if (buscar(username) != null) {
            throw new usuarioDuplicado(username);
        }
        validarPassword(password);
 
        jugador nuevo = new jugador(username, password);
        jugadores.add(nuevo);
        usuariosHistoricos++;
        return nuevo;
    }
 
    public jugador login(String username, String password)
            throws credencialesInvalidas {
        jugador j = (username == null) ? null : buscar(username.trim());
        if (j == null || !j.verificarPassword(password)) {
            throw new credencialesInvalidas("Usuario o password incorrectos.");
        }
        return j;
    }
 
    public void cambiarPassword(jugador j, String actual, String nuevo)
            throws credencialesInvalidas, passwordInvalido {
        if (!j.verificarPassword(actual)) {
            throw new credencialesInvalidas("El password actual es incorrecto.");
        }
        validarPassword(nuevo);
        j.setPassword(nuevo);
    }
 
    public void eliminarJugador(jugador j) {
        jugadores.remove(j);
    }

    public boolean puedeIniciarPartida() {
        return jugadores.size() >= 2;
    }
 
    public ArrayList<jugador> getOponentesDe(jugador j) {
        ArrayList<jugador> lista = new ArrayList<>();
        for (jugador otro : jugadores) {
            if (otro != j) {
                lista.add(otro);
            }
        }
        return lista;
    }

    public ArrayList<jugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public int getUsuariosActivos() {
        return jugadores.size();
    }
 
    public int getUsuariosHistoricos() {
        return usuariosHistoricos;
    }

    public log registrarPartida(jugador ganador, jugador perdedor, bando bandoGanador, tipoFin tipo) {
        if (ganador == perdedor) {
            throw new IllegalArgumentException("Ganador y perdedor no pueden ser el mismo.");
        }
        log log = new log(ganador.getUsername(), perdedor.getUsername(),
                bandoGanador, tipo);
 
        ganador.sumarPuntos(tipo == tipoFin.RETIRO ? PUNTOS_RETIRO : PUNTOS_VICTORIA);
        ganador.agregarLog(log);
        perdedor.agregarLog(log);
        logsGlobales.add(log);
        return log;
    }

    public ArrayList<jugador> getRanking() {
        ArrayList<jugador> ranking = new ArrayList<>(jugadores);
        ranking.sort((a, b) -> b.getPuntos() - a.getPuntos());
        return ranking;
    }

    public int[] getEstadisticas() {
        int[] acc = new int[3];
        for (log l : logsGlobales) {
            acc[0]++;
            if (l.getBandoGanador() == bando.HEROES) {
                acc[1]++;
            } else {
                acc[2]++;
            }
        }
        return acc;
    }
}
