package vampireswargame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Jugador {
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String userName;
    private String password;
    private int ranking;
    private final LocalDateTime fechaIngreso;
    private boolean activo;

    public Jugador(String userName, String password) throws ValidacionException {
        String nombreLimpio = userName == null ? "" : userName.trim();
        validarNombre(nombreLimpio);
        validarPassword(password);
        this.userName = nombreLimpio;
        this.password = password;
        this.ranking = 0;
        this.fechaIngreso = LocalDateTime.now();
        this.activo = true;
    }

    public static Jugador registrar(String nombre, String password)
            throws ValidacionException {
        Jugador jugador = new Jugador(nombre, password);
        RepositorioSistema repositorio = MemoriaSistema.getInstancia();
        repositorio.agregarJugador(jugador);
        return jugador;
    }

    public static Jugador autenticar(String nombre, char[] password)
            throws ValidacionException {
        RepositorioSistema repositorio = MemoriaSistema.getInstancia();
        Jugador jugador = repositorio.buscarJugador(nombre);
        if (jugador == null || !jugador.isActivo()) {
            throw new ValidacionException("El jugador no existe o su cuenta está cerrada.");
        }
        String clave = password == null ? "" : new String(password);
        if (!jugador.password.equals(clave)) {
            throw new ValidacionException("Contraseña incorrecta.");
        }
        return jugador;
    }

    private static void validarNombre(String nombre) throws ValidacionException {
        if (nombre == null || nombre.isBlank()) {
            throw new ValidacionException("Debe ingresar un nombre de usuario.");
        }
    }

    private static void validarPassword(String password) throws ValidacionException {
        if (password == null || password.length() != 5) {
            throw new ValidacionException(
                    "La contraseña debe tener exactamente 5 caracteres.");
        }
    }

    public void cambiarPassword(String actual, String nueva, String confirmacion)
            throws ValidacionException {
        if (!password.equals(actual)) {
            throw new ValidacionException("La contraseña actual es incorrecta.");
        }
        validarPassword(nueva);
        if (!nueva.equals(confirmacion)) {
            throw new ValidacionException("La confirmación no coincide.");
        }
        password = nueva;
    }

    public String getUserName() {
        return userName;
    }

    public int getRanking() {
        return ranking;
    }

    public void agregarVictoria() {
        ranking += 3;
    }

    public String getFechaIngresoFormateada() {
        return fechaIngreso.format(FORMATO_FECHA);
    }

    public boolean isActivo() {
        return activo;
    }

    public void cerrarCuenta() {
        activo = false;
    }

    @Override
    public String toString() {
        return userName;
    }
}
