package vampireswargame;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Frontera común para impedir que una excepción cierre abruptamente la GUI.
 */
public final class UiSeguro {
    private UiSeguro() {
    }

    public static final void ejecutar(Component padre, Runnable accion) {
        try {
            accion.run();
        } catch (RuntimeException excepcion) {
            mostrarError(padre, excepcion);
        }
    }

    public static final void iniciarAplicacion() {
        RecursosVisuales.inicializarTemaGlobal();
        Thread.setDefaultUncaughtExceptionHandler(
                (hilo, error) -> mostrarError(null, error));
        SwingUtilities.invokeLater(() -> ejecutar(null, () -> {
            MenuInicio inicio = new MenuInicio();
            inicio.setVisible(true);
        }));
    }

    private static void mostrarError(Component padre, Throwable error) {
        Runnable mensaje = () -> JOptionPane.showMessageDialog(
                padre,
                "Ocurrió un error inesperado, pero el programa puede continuar.\n"
                        + mensajeSeguro(error),
                "Error controlado",
                JOptionPane.ERROR_MESSAGE);
        if (EventQueue.isDispatchThread()) {
            mensaje.run();
        } else {
            EventQueue.invokeLater(mensaje);
        }
        Toolkit.getDefaultToolkit().beep();
    }

    private static String mensajeSeguro(Throwable error) {
        String mensaje = error == null ? null : error.getMessage();
        return mensaje == null || mensaje.isBlank()
                ? "Revise la acción e intente nuevamente."
                : mensaje;
    }
}
