package vampireswargame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class HistorialPartida {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Jugador jugadorUno;
    private final Jugador jugadorDos;
    private final LocalDateTime fecha;
    private final String mensaje;

    public HistorialPartida(Jugador jugadorUno, Jugador jugadorDos, String mensaje) {
        this.jugadorUno = jugadorUno;
        this.jugadorDos = jugadorDos;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }

    public boolean incluye(Jugador jugador) {
        return jugador != null && (jugador == jugadorUno || jugador == jugadorDos);
    }

    public String getFechaFormateada() {
        return fecha.format(FORMATO);
    }

    public String getMensaje() {
        return mensaje;
    }
}
