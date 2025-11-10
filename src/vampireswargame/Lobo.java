package vampireswargame;

import javax.swing.JButton;

/**
 *
 * @author gaat1
 */
public final class Lobo  extends Pieza {
    private Piezas lobo = Piezas.HOMBRELOBO;
     private final String nombre = "lobo";
    
    public Lobo() {
        this.ataque = lobo.getAtaque();
        this.escudo = lobo.getEscudo();
        this.salud = lobo.getSalud();
    }
    public String getNombre(){
        return nombre;
    }
    public int getSalud(){
        return salud;
    }
    public int getEscudo(){
        return escudo;
    }
    public void Habilidad(JButton invocador, JButton[][] tablero, Tablero tabla){}
}
