
package enums;
 
public enum modoJuego {
    TUTORIAL("Tutorial"),
    CLASICO("Classic");
 
    private final String nombre;
 
    modoJuego(String nombre) {
        this.nombre = nombre;
    }
 
    public String getNombre() {
        return nombre;
    }
}
