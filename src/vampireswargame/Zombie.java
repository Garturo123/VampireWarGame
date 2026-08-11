package vampireswargame;

public final class Zombie extends Pieza {
    private final Muerte duena;

    public Zombie(Muerte duena) {
        super(1, 1, 0, 0);
        this.duena = duena;
    }

    @Override
    public String getNombre() {
        return "zombie";
    }

    @Override
    public int Habilidad(Pieza objetivo) {
        return atacarNormal(objetivo);
    }

    public Muerte getDuena() {
        return duena;
    }
}
