
package enums;

public enum tipoPieza {
    NORMAL, BOMBA, TIERRA;

    public boolean esMovil() {
        return this == NORMAL;
    }
}
