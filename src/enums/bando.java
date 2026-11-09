
package enums;

public enum bando {
    HEROES("Heroes"),
    VILLANOS("Villains");

    private final String nombre;

    bando(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public bando opuesto() {
        return this == HEROES ? VILLANOS : HEROES;
    }
}
