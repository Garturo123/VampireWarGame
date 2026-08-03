package vampireswargame;

public final class Lobo extends Pieza {
    public Lobo() {
        super(5, 5, 2, 2);
    }

    @Override
    public String getNombre() {
        return "lobo";
    }

    @Override
    public int Habilidad(Pieza objetivo) {
        return 0;
    }
}
