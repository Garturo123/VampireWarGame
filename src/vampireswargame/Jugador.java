package vampireswargame;

/** Clase hija concreta que añade contraseña, puntos y lógica de juego. */
public class Jugador extends Usuario {
    private String password;
    private int ranking;

    public Jugador(String userName, String password) throws ValidacionException {
        super(validarNombre(userName));
        validarPassword(password);
        this.password = password;
        this.ranking = 0;
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

    private static String validarNombre(String nombre) throws ValidacionException {
        String nombreLimpio = nombre == null ? "" : nombre.trim();
        if (nombreLimpio.isBlank()) {
            throw new ValidacionException("Debe ingresar un nombre de usuario.");
        }
        return nombreLimpio;
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

    public int getRanking() {
        return ranking;
    }

    public void agregarVictoria() {
        ranking += 3;
    }

}
