package vampireswargame;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author gaat1
 */
public class Juego extends JFrame {
    private static Tablero tabla;
    private static opciones panel;
    
    public Juego(){
        setTitle("Vampire War Game");
        setSize(1350,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new opciones();
        tabla = new Tablero(panel, this);
        
        
        
        add(panel, BorderLayout.WEST);
        add(tabla, BorderLayout.CENTER);
    }
    public static Tablero getTablero(){
        return tabla;
    }
    public static opciones panel(){
        return panel;
    }
}
