package vampireswargame;

import javax.swing.JButton;

/**
 *
 * @author gaat1
 */
public final class Zombie extends Pieza {
    private Piezas zombie = Piezas.ZOMBIE;
    private String nombre = "zombie";

    
    public Zombie(){
        this.ataque = zombie.getAtaque();
        this.escudo = zombie.getEscudo();
        this.salud = zombie.getSalud();
    }
    public String getNombre(){
        return nombre;
    }
    public void Habilidad(JButton invocador, JButton[][] tablero, Tablero tabla){}
}
