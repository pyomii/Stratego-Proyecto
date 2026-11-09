package gui;

import datos.jugador;
import datos.registrar;
import java.awt.*;
import javax.swing.*;

public class eliminarCuenta extends JPanel {

    private final registrar reg;
    private final jugador actual;
    private final menuPrincipal menu;
    private final ajustes padre;

    private final JComboBox<jugador> combo = new JComboBox<>();
    private final JLabel estado = estiloSets.etiqueta("", 17);
    private final botonImagen btnAccept = new botonImagen("Accept", "btn_peq.png", 114, 40, 22f);

    public eliminarCuenta(registrar reg, jugador actual, menuPrincipal menu, ajustes padre) {
        this.reg = reg;
        this.actual = actual;
        this.menu = menu;
        this.padre = padre;

        setOpaque(false);
        setLayout(null);

        JLabel titulo = estiloSets.titulo("DELETE ACCOUNT");
        titulo.setBounds(0, 18, 530, 45);
        add(titulo);

        estiloSets.Tarjeta tarjeta = estiloSets.tarjeta(estiloSets.BLANCO_SUAVE);
        tarjeta.setBounds(55, 95, 420, 245);
        add(tarjeta);

        JLabel lblCuenta = estiloSets.etiqueta("Select an account:", 21);
        lblCuenta.setBounds(35, 32, 350, 30);
        tarjeta.add(lblCuenta);

        DefaultComboBoxModel<jugador> modelo = new DefaultComboBoxModel<>();
        for (jugador j : reg.getJugadores()) {
            modelo.addElement(j);
        }
        combo.setModel(modelo);
        combo.setBounds(35, 72, 350, 42);
        combo.setFont(new Font("SansSerif", Font.BOLD, 17));
        combo.setBackground(new Color(245, 250, 255));
        combo.setForeground(estiloSets.TEXTO);
        combo.setBorder(BorderFactory.createLineBorder(new Color(80, 130, 210), 1));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor,
                    int indice, boolean seleccionado, boolean conFoco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, conFoco);
                if (valor instanceof jugador j) {
                    setText(j.getUsername() + (j == actual ? "   (you)" : ""));
                }
                setFont(new Font("SansSerif", Font.BOLD, 17));
                if (!seleccionado) {
                    setBackground(new Color(245, 250, 255));
                    setForeground(estiloSets.TEXTO);
                }
                return this;
            }
        });
        tarjeta.add(combo);

        JLabel aviso = estiloSets.etiqueta("This action cannot be undone.", 15);
        aviso.setForeground(new Color(150, 30, 40));
        aviso.setBounds(35, 130, 350, 28);
        tarjeta.add(aviso);

        btnAccept.setBounds(153, 182, 114, 40);
        btnAccept.addActionListener(e -> borrarCuenta());
        tarjeta.add(btnAccept);

        estado.setHorizontalAlignment(SwingConstants.CENTER);
        estado.setBounds(55, 362, 420, 30);
        add(estado);

        if (modelo.getSize() == 0) {
            btnAccept.setEnabled(false);
            estado.setText("There are no accounts to delete.");
        }
    }

    private void borrarCuenta() {
        jugador seleccionado = (jugador) combo.getSelectedItem();
        if (seleccionado == null) {
            estado.setForeground(new Color(150, 30, 40));
            estado.setText("Select an account first.");
            return;
        }

        reg.eliminarJugador(seleccionado);

        if (seleccionado == actual) {
            padre.dispose();
            menu.cerrarSesion();
            return;
        }

        DefaultComboBoxModel<jugador> modelo = (DefaultComboBoxModel<jugador>) combo.getModel();
        modelo.removeElement(seleccionado);
        estado.setForeground(estiloSets.TEXTO_SUAVE);
        estado.setText("Account deleted: " + seleccionado.getUsername());

        if (modelo.getSize() == 0) {
            btnAccept.setEnabled(false);
        }
    }
}
