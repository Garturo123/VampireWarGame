package vampireswargame;

import javax.swing.JButton;

/**
 *
 * @author gaat1
 */
public abstract class Pieza {
    protected int escudo;
    protected int salud;
    protected int ataque;
    private String nombre;
    protected boolean chupar;
    public Pieza(){
        
    }
    public String getNombre(){
        return nombre;
    }
    public boolean estaViva() { return salud > 0; }
    
    public void RecibirDanio(int cantidad, boolean penetrante){
        if(penetrante){
            salud -= cantidad;
            return;
        }
        
        int danioRestante = cantidad - escudo;
        escudo -= cantidad;
        if (escudo < 0) escudo = 0;
        if (danioRestante > 0) {
            salud -= danioRestante;
        }
        if (salud < 0) salud = 0;
    }
    public int getSalud(){
        return salud;
    }
    public int getEscudo(){
        return escudo;
    }
    
    public abstract void Habilidad(JButton invocador, JButton[][] tablero, Tablero tabla);

}
