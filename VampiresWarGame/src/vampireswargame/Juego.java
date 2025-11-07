package vampireswargame;

import javax.swing.JFrame;

/**
 *
 * @author gaat1
 */
public class Juego extends JFrame {
    public Juego(){
        setTitle("Vampire War Game");
        setSize(1000,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Tablero tabla = new Tablero();
        add(tabla);
    }
}
