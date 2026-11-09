
package datos;

import enums.bando;
import enums.tipoFin;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class log {
    private static final DateTimeFormatter FORMATO
            = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
 
    private final String ganador;
    private final String perdedor;
    private final bando bandoGanador;
    private final tipoFin tipo;
    private final LocalDateTime fecha;
 
    public log(String ganador, String perdedor, bando bandoGanador, tipoFin tipo) {
        this.ganador = ganador;
        this.perdedor = perdedor;
        this.bandoGanador = bandoGanador;
        this.tipo = tipo;
        this.fecha = LocalDateTime.now();
    }
 
    public String getGanador() {
        return ganador;
    }

    public bando getBandoGanador() {
        return bandoGanador;
    }

    @Override
    public String toString() {
        String bg = bandoGanador.getNombre().toUpperCase();
        String bp = bandoGanador.opuesto().getNombre().toUpperCase();
        String f = fecha.format(FORMATO);
 
        return switch (tipo) {
            case CAPTURA_TIERRA -> {
                String verbo = (bandoGanador == bando.HEROES) ? "SALVADO" : "CAPTURADO";
                yield ganador + " usando " + bg + " ha " + verbo + " la TIERRA! Venciendo a " + perdedor + " — " + f;
            }
            case SIN_MOVIMIENTOS ->
                perdedor + " usando " + bp + " ha perdido por no tener movimientos " + "válidos disponibles ante " + ganador + " — " + f;
            case RETIRO ->
                ganador + " usando " + bg + " ha ganado ya que " + perdedor + " usando " + bp + " se ha retirado del juego — " + f;
        };
    }
}
