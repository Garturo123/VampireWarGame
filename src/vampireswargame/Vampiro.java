package vampireswargame;

public class Vampiro extends Pieza {
    public Vampiro() {
        super(3, 4, 5, 1);
    }

    @Override
    public String getNombre() {
        return "vampiro";
    }

    @Override
    public int Habilidad(Pieza objetivo) {
        if (objetivo == null) {
            return 0;
        }
        objetivo.recibirDanio(1, false);
        recuperarVida(1);
        return 1;
    }
}
