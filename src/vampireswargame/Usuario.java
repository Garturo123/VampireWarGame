package vampireswargame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase padre concreta para las cuentas del sistema.
 * Puede instanciarse y contiene el estado común de cualquier usuario.
 */
public class Usuario {
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String userName;
    private final LocalDateTime fechaIngreso;
    private boolean activo;

    public Usuario(String userName) {
        this.userName = userName;
        this.fechaIngreso = LocalDateTime.now();
        this.activo = true;
    }

    public String getUserName() {
        return userName;
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
