
package stratego;

import datos.registrar;
import gui.menuInicial;
import javax.swing.SwingUtilities;

public class Stratego {

    public static void main(String[] args) {
        registrar reg = new registrar();
        SwingUtilities.invokeLater(() -> new menuInicial(reg).setVisible(true));
    }
}
