package modelo;

import enums.bando;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import piezas.agente;
import piezas.bomba;
import piezas.explorador;
import piezas.mistico;
import piezas.pieza;
import piezas.piezaMovil;
import piezas.tierrra;

public class formacion {
 
    private static final int[] RANGOS = {
        10, 9, 8, 8, 7, 7, 7, 6,
        6, 6, 6, 5, 5, 5, 5, 4,
        4, 4, 4, 3, 3, 3, 3, 3,
        2, 2, 2, 2, 2, 2, 2, 2,
        1
    };
 
    private static final String[] NOMBRES_HEROES = {
        "Mr. Fantastic", "Captain America", "Professor X", "Nick Fury",
        "Spider-Man", "Wolverine", "Namor", "Daredevil",
        "Silver Surfer", "Hulk", "Iron Man", "Thor",
        "Human Torch", "Cyclops", "Invisible Woman", "Ghost Rider",
        "Punisher", "Blade", "Thing", "Emma Frost",
        "She-Hulk", "Giant Man", "Beast", "Colossus",
        "Gambit", "Spider-Girl", "Iceman", "Storm",
        "Phoenix", "Dr. Strange", "Elektra", "Nightcrawler",
        "Black Widow"
    };
 
    private static final String[] NOMBRES_VILLANOS = {
        "Dr. Doom", "Galactus", "Kingpin", "Magneto",
        "Apocalypse", "Green Goblin", "Venom", "Bullseye",
        "Omega Red", "Onslaught", "Red Skull", "Mystique",
        "Mysterio", "Dr. Octopus", "Deadpool", "Abomination",
        "Thanos", "Black Cat", "Sabretooth", "Juggernaut",
        "Rhino", "Carnage", "Mole Man", "Lizard",
        "Mr. Sinister", "Sentinel 1", "Ultron", "Sandman",
        "Leader", "Viper", "Sentinel 2", "Electro",
        "Black Widow"
    };
 
    public static final int TOTAL_BOMBAS = 6;

    public static int[] filasDe(bando b) {
        return (b == bando.VILLANOS) ? new int[]{0, 1, 2, 3} : new int[]{9, 8, 7, 6};
    }

    public static ArrayList<pieza> crearPiezas(bando b) {
        String[] nombres = (b == bando.HEROES) ? NOMBRES_HEROES : NOMBRES_VILLANOS;
        ArrayList<pieza> lista = new ArrayList<>();
        for (int i = 0; i < RANGOS.length; i++) {
            lista.add(crearMovil(nombres[i], RANGOS[i], b));
        }
        String nombreBomba = (b == bando.HEROES) ? "Nova Blast" : "Pumpkin Bomb";
        for (int i = 0; i < TOTAL_BOMBAS; i++) {
            lista.add(new bomba(nombreBomba, b));
        }
        lista.add(new tierrra(b));
        return lista;
    }
 
    private static pieza crearMovil(String nombre, int rango, bando b) {
        return switch (rango) {
            case 1 -> new agente(nombre, b);
            case 2 -> new mistico(nombre, b);
            case 3 -> new explorador(nombre, b);
            default -> new piezaMovil(nombre, rango, b);
        };
    }
 
    public static void generarAleatoria(tablero t, bando b, Random rnd) {
        int[] filas = filasDe(b);
        int fondo = filas[0];
        int segunda = filas[1];
        int frente = filas[3];

        ArrayList<pieza> bombas = new ArrayList<>();
        ArrayList<pieza> misticos = new ArrayList<>();
        ArrayList<pieza> resto = new ArrayList<>();
        pieza tierra = null;
        for (pieza p : crearPiezas(b)) {
            switch (p.getTipo()) {
                case TIERRA -> tierra = p;
                case BOMBA -> bombas.add(p);
                default -> {
                    if (p.getRango() == 2) {
                        misticos.add(p);
                    } else {
                        resto.add(p);
                    }
                }
            }
        }

        int col = 1 + rnd.nextInt(tablero.TAMANO - 2);
        t.colocar(tierra, fondo, col);
        ArrayList<posicion> alrededor = new ArrayList<>();
        alrededor.add(new posicion(fondo, col - 1));
        alrededor.add(new posicion(fondo, col + 1));
        alrededor.add(new posicion(segunda, col));
        ArrayList<pieza> bombasAlrededor = new ArrayList<>(bombas.subList(0, alrededor.size()));
        bombas.subList(0, alrededor.size()).clear();
        colocarGrupo(t, bombasAlrededor, alrededor, rnd);

        colocarGrupo(t, bombas, celdasLibres(t, new int[]{filas[0], filas[1]}), rnd);

        colocarGrupo(t, misticos, celdasLibres(t, new int[]{filas[2], filas[3]}), rnd);

        ArrayList<posicion> libresFrente = celdasLibres(t, new int[]{frente});
        Collections.shuffle(resto, rnd);
        ArrayList<pieza> paraFrente = new ArrayList<>(resto.subList(0, libresFrente.size()));
        resto.subList(0, libresFrente.size()).clear();
        colocarGrupo(t, paraFrente, libresFrente, rnd);

        colocarGrupo(t, resto, celdasLibres(t, filas), rnd);
    }
 
    static ArrayList<posicion> celdasLibres(tablero t, int[] filas) {
        ArrayList<posicion> libres = new ArrayList<>();
        for (int f : filas) {
            for (int c = 0; c < tablero.TAMANO; c++) {
                if (t.estaVacia(f, c)) {
                    libres.add(new posicion(f, c));
                }
            }
        }
        return libres;
    }
    
    static void colocarGrupo(tablero t, ArrayList<pieza> piezas,
            ArrayList<posicion> candidatas, Random rnd) {
        while (!piezas.isEmpty()) {
            if (candidatas.isEmpty()) {
                throw new IllegalStateException("No hay casillas suficientes para colocar las piezas.");
            }
            pieza p = piezas.remove(piezas.size() - 1);
            posicion destino = candidatas.remove(rnd.nextInt(candidatas.size()));
            t.colocar(p, destino.getFila(), destino.getColumna());
        }
    }
}