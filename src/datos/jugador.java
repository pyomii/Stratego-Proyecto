
package datos;

import java.util.ArrayList;

public class jugador {
    private final String username;
    private String password;
    private int puntos;
    private final ArrayList<log> historial;
 
    public jugador(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.historial = new ArrayList<>();
    }
 
    public String getUsername() {
        return username;
    }
 
    public int getPuntos() {
        return puntos;
    }

    public int getVictorias() {
        int victorias = 0;
        for (log l : historial) {
            if (l.getGanador().equals(username)) {
                victorias++;
            }
        }
        return victorias;
    }

    public String getPassword() {
        return password;
    }
  
    public boolean verificarPassword(String intento) {
        return password.equals(intento);
    }
 
    void setPassword(String nuevo) {
        this.password = nuevo;
    }
 
    void sumarPuntos(int cantidad) {
        this.puntos += cantidad;
    }
 
    void agregarLog(log log) {
        historial.add(0, log);
    }
 
    @Override
    public String toString() {
        return username + " (" + puntos + " pts)";
    }
}
