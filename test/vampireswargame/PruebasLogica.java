package vampireswargame;

import java.lang.reflect.Modifier;

/**
 * Pruebas sin interfaz gráfica para las reglas y requisitos estructurales.
 */
public final class PruebasLogica {
    private PruebasLogica() {
    }

    public static void main(String[] args) throws Exception {
        probarEstructuraOrientadaAObjetos();
        probarCombatePolimorfico();
        probarAlmacenamientoYRecursion();
        System.out.println("Todas las pruebas de lógica finalizaron correctamente.");
    }

    private static void probarEstructuraOrientadaAObjetos() throws Exception {
        exigir(Modifier.isAbstract(Pieza.class.getModifiers()),
                "Pieza debe ser abstracta.");
        exigir(Modifier.isFinal(Lobo.class.getModifiers()),
                "Lobo debe ser final.");
        exigir(Modifier.isFinal(Pieza.class
                .getMethod("atacarNormal", Pieza.class).getModifiers()),
                "atacarNormal debe ser final.");
    }

    private static void probarCombatePolimorfico() {
        Pieza lobo = new Lobo();
        Pieza vampiro = new Vampiro();
        lobo.atacarNormal(vampiro);
        exigir(vampiro.getEscudo() == 0 && vampiro.getSalud() == 4,
                "El escudo debe absorber primero el ataque.");

        Pieza muerte = new Muerte();
        Pieza objetivo = new Lobo();
        muerte.Habilidad(objetivo);
        exigir(objetivo.getEscudo() == 2 && objetivo.getSalud() == 3,
                "La lanza debe ignorar el escudo y causar 2 de daño.");

        Pieza zombie = new Zombie((Muerte) muerte);
        exigir(zombie.getMovilidad() == 1,
                "El Zombie debe poder moverse durante el turno de su Muerte.");
        vampiro.Habilidad(zombie);
        exigir(!zombie.estaViva(),
                "La absorción debe causar un punto de daño.");
    }

    private static void probarAlmacenamientoYRecursion() throws Exception {
        MemoriaSistema memoria = MemoriaSistema.getInstancia();
        Jugador ana = Jugador.registrar("AnaPrueba", "12345");
        Jugador beto = Jugador.registrar("BetoPrueba", "abcde");
        beto.agregarVictoria();

        exigir(memoria.buscarJugador("anaprueba") == ana,
                "La búsqueda recursiva no encontró al jugador.");
        Jugador[] ranking = memoria.obtenerJugadoresActivosOrdenados();
        exigir(ranking.length == 2 && ranking[0] == beto,
                "El ordenamiento recursivo del ranking es incorrecto.");

        memoria.agregarPartida(new HistorialPartida(
                ana, beto, "Partida de prueba finalizada."));
        exigir(memoria.obtenerHistorial(ana).length == 1,
                "El historial en arreglos no guardó la partida.");
        ana.cerrarCuenta();
        exigir(memoria.obtenerJugadoresActivosOrdenados().length == 1,
                "El ranking debe ocultar cuentas cerradas.");
    }

    private static void exigir(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
