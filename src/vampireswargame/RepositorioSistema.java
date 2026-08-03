package vampireswargame;

/**
 * Contrato de acceso al almacenamiento en memoria del sistema.
 * La implementación utiliza únicamente arreglos.
 */
public interface RepositorioSistema {
    void agregarJugador(Jugador jugador) throws ValidacionException;

    Jugador buscarJugador(String nombre);

    Jugador[] obtenerJugadoresActivosOrdenados();

    Jugador[] obtenerOponentes(Jugador jugadorActual);

    void agregarPartida(HistorialPartida partida) throws ValidacionException;

    HistorialPartida[] obtenerHistorial(Jugador jugador);
}
