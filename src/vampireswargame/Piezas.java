package vampireswargame;

/**
 * Catálogo inmutable de valores oficiales de las piezas.
 */
public enum Piezas {
    ZOMBIE(1, 1, 0, 1),
    MUERTE(4, 3, 1, 1),
    HOMBRE_LOBO(5, 5, 2, 2),
    VAMPIRO(3, 4, 5, 1);

    private final int ataque;
    private final int salud;
    private final int escudo;
    private final int movilidad;

    Piezas(int ataque, int salud, int escudo, int movilidad) {
        this.ataque = ataque;
        this.salud = salud;
        this.escudo = escudo;
        this.movilidad = movilidad;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getSalud() {
        return salud;
    }

    public int getEscudo() {
        return escudo;
    }

    public int getMovilidad() {
        return movilidad;
    }
}
