package vampireswargame;

/**
 *
 * @author gaat1
 */
public final class Muerte extends Pieza {
    private Piezas muerte = Piezas.MUERTE;
    private String nombre = "muerte";
    
    public Muerte() {
        this.ataque = muerte.getAtaque();
        this.escudo = muerte.getEscudo();
        this.salud = muerte.getSalud();
    }
    public String getNombre(){
        return nombre;
    }

    
    
}
