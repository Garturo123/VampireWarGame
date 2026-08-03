package vampireswargame;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Juego extends JFrame {
    private final Tablero tablero;
    private final opciones panelOpciones;

    public Juego(Jugador jugadorUno, Jugador jugadorDos) {
        setTitle("Vampire Wargame - " + jugadorUno + " vs. " + jugadorDos);
        setSize(1280, 850);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        panelOpciones = new opciones();
        tablero = new Tablero(panelOpciones, this, jugadorUno, jugadorDos);
        add(panelOpciones, BorderLayout.WEST);
        add(tablero, BorderLayout.CENTER);
    }

    public Tablero getTablero() {
        return tablero;
    }

    public opciones getPanelOpciones() {
        return panelOpciones;
    }
}
