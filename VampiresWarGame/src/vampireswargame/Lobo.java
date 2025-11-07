package vampireswargame;

/**
 *
 * @author gaat1
 */
public final class Lobo  extends Pieza {
    private Piezas lobo = Piezas.HOMBRELOBO;
     private String nombre = "lobo";
    
    public Lobo() {
        this.ataque = lobo.getAtaque();
        this.escudo = lobo.getEscudo();
        this.salud = lobo.getSalud();
    }
     public String getNombre(){
        return nombre;
    }
    
}
