
package excepciones;

public class usuarioDuplicado extends Exception {
 
    public usuarioDuplicado(String username) {
        super("El username \"" + username + "\" ya existe. Ingrese uno nuevo.");
    }
}
