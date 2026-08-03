package vampireswargame;

public final class Muerte extends Pieza {
    public Muerte() {
        super(4, 3, 1, 1);
    }

    @Override
    public String getNombre() {
        return "muerte";
    }

    @Override
    public int Habilidad(Pieza objetivo) {
        if (objetivo == null) {
            return 0;
        }
        int danioLanza = getAtaque() / 2;
        objetivo.recibirDanio(danioLanza, true);
        return danioLanza;
    }
}
