package vampireswargame;

import javax.swing.JButton;


/**
 *
 * @author gaat1
 */
public class Vampiro extends Pieza {
    private Piezas vampiro = Piezas.VAMPIRO;
    private String nombre = "vampiro";
    
    public Vampiro() {
        this.ataque = vampiro.getAtaque();
        this.escudo = vampiro.getEscudo();
        this.salud = vampiro.getSalud();
      
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
     public void Habilidad(JButton invocador, JButton[][] tablero, Tablero tabla){
         this.chupar = !this.chupar;
     }
    
}
