package vampireswargame;

/**
 * Almacenamiento central del programa. No utiliza colecciones, archivos ni
 * bases de datos: todos los registros viven en arreglos durante la ejecución.
 */
public final class MemoriaSistema implements RepositorioSistema {
    private static final int MAX_JUGADORES = 100;
    private static final int MAX_PARTIDAS = 500;
    private static final MemoriaSistema INSTANCIA = new MemoriaSistema();

    private final Jugador[] jugadores = new Jugador[MAX_JUGADORES];
    private final HistorialPartida[] partidas = new HistorialPartida[MAX_PARTIDAS];
    private int cantidadJugadores;
    private int cantidadPartidas;

    private MemoriaSistema() {
    }

    public static MemoriaSistema getInstancia() {
        return INSTANCIA;
    }

    @Override
    public void agregarJugador(Jugador jugador) throws ValidacionException {
        if (jugador == null) {
            throw new ValidacionException("El jugador no puede ser nulo.");
        }
        if (cantidadJugadores >= jugadores.length) {
            throw new ValidacionException("Se alcanzó el máximo de jugadores.");
        }
        if (buscarJugador(jugador.getUserName()) != null) {
            throw new ValidacionException("Ya existe una cuenta con ese nombre.");
        }
        jugadores[cantidadJugadores++] = jugador;
    }

    @Override
    public Jugador buscarJugador(String nombre) {
        if (nombre == null) {
            return null;
        }
        return buscarJugadorRecursivo(nombre.trim(), 0);
    }

    // Primera función recursiva: búsqueda secuencial en el arreglo.
    private Jugador buscarJugadorRecursivo(String nombre, int indice) {
        if (indice >= cantidadJugadores) {
            return null;
        }
        Jugador actual = jugadores[indice];
        if (actual != null && actual.getUserName().equalsIgnoreCase(nombre)) {
            return actual;
        }
        return buscarJugadorRecursivo(nombre, indice + 1);
    }

    @Override
    public Jugador[] obtenerJugadoresActivosOrdenados() {
        Jugador[] activos = new Jugador[contarActivos(0)];
        copiarActivos(activos, 0, 0);
        ordenarPorPuntosRecursivo(activos, activos.length);
        return activos;
    }

    private int contarActivos(int indice) {
        if (indice >= cantidadJugadores) {
            return 0;
        }
        int suma = jugadores[indice] != null && jugadores[indice].isActivo() ? 1 : 0;
        return suma + contarActivos(indice + 1);
    }

    private int copiarActivos(Jugador[] destino, int origen, int posicion) {
        if (origen >= cantidadJugadores) {
            return posicion;
        }
        Jugador jugador = jugadores[origen];
        if (jugador != null && jugador.isActivo()) {
            destino[posicion++] = jugador;
        }
        return copiarActivos(destino, origen + 1, posicion);
    }

    // Segunda función recursiva: ordenamiento burbuja por puntos.
    private void ordenarPorPuntosRecursivo(Jugador[] datos, int limite) {
        if (limite <= 1) {
            return;
        }
        for (int i = 0; i < limite - 1; i++) {
            if (datos[i].getRanking() < datos[i + 1].getRanking()) {
                Jugador temporal = datos[i];
                datos[i] = datos[i + 1];
                datos[i + 1] = temporal;
            }
        }
        ordenarPorPuntosRecursivo(datos, limite - 1);
    }

    @Override
    public Jugador[] obtenerOponentes(Jugador jugadorActual) {
        Jugador[] activos = obtenerJugadoresActivosOrdenados();
        int cantidad = 0;
        for (Jugador jugador : activos) {
            if (jugador != jugadorActual) {
                cantidad++;
            }
        }
        Jugador[] oponentes = new Jugador[cantidad];
        int posicion = 0;
        for (Jugador jugador : activos) {
            if (jugador != jugadorActual) {
                oponentes[posicion++] = jugador;
            }
        }
        return oponentes;
    }

    @Override
    public void agregarPartida(HistorialPartida partida) throws ValidacionException {
        if (partida == null) {
            throw new ValidacionException("El registro de la partida no puede ser nulo.");
        }
        if (cantidadPartidas >= partidas.length) {
            throw new ValidacionException("Se alcanzó el máximo de partidas.");
        }
        partidas[cantidadPartidas++] = partida;
    }

    @Override
    public HistorialPartida[] obtenerHistorial(Jugador jugador) {
        int cantidad = 0;
        for (int i = 0; i < cantidadPartidas; i++) {
            if (partidas[i].incluye(jugador)) {
                cantidad++;
            }
        }
        HistorialPartida[] resultado = new HistorialPartida[cantidad];
        int posicion = 0;
        for (int i = cantidadPartidas - 1; i >= 0; i--) {
            if (partidas[i].incluye(jugador)) {
                resultado[posicion++] = partidas[i];
            }
        }
        return resultado;
    }
}
