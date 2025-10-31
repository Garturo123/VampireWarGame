package vampireswargame;

import javax.swing.SwingUtilities;

/**
 *
 * @author gaat1
 */
public class VampiresWarGame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            MenuInicio Inicio = new MenuInicio();
           Inicio.setVisible(true);
        });
    }
}
