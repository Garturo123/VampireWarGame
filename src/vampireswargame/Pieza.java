package vampireswargame;

/**
 * Base polimórfica de todas las piezas del tablero.
 */
public abstract class Pieza {
    private final int ataque;
    private final int saludMaxima;
    private final int movilidad;
    private int escudo;
    private int salud;
    private String equipo;

    protected Pieza(int ataque, int salud, int escudo, int movilidad) {
        this.ataque = ataque;
        this.saludMaxima = salud;
        this.salud = salud;
        this.escudo = escudo;
        this.movilidad = movilidad;
    }

    public abstract String getNombre();

    /**
     * Ejecuta la habilidad propia del subtipo y devuelve el daño aplicado.
     */
    public abstract int Habilidad(Pieza objetivo);

    public final int atacarNormal(Pieza objetivo) {
        if (objetivo == null) {
            return 0;
        }
        objetivo.recibirDanio(ataque, false);
        return ataque;
    }

    public void recibirDanio(int cantidad, boolean penetrante) {
        int danio = Math.max(0, cantidad);
        if (penetrante) {
            salud = Math.max(0, salud - danio);
            return;
        }
        int absorbido = Math.min(escudo, danio);
        escudo -= absorbido;
        salud = Math.max(0, salud - (danio - absorbido));
    }

    protected final void recuperarVida(int cantidad) {
        salud = Math.min(saludMaxima, salud + Math.max(0, cantidad));
    }

    public final boolean estaViva() {
        return salud > 0;
    }

    public final int getAtaque() {
        return ataque;
    }

    public final int getSalud() {
        return salud;
    }

    public final int getSaludMaxima() {
        return saludMaxima;
    }

    public final int getEscudo() {
        return escudo;
    }

    public final int getMovilidad() {
        return movilidad;
    }

    public final String getEquipo() {
        return equipo;
    }

    public final void setEquipo(String equipo) {
        this.equipo = equipo;
    }
}
