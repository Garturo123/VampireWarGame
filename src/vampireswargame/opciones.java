package vampireswargame;

import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class opciones extends PanelFondoGotico {
    private final ruleta suerte = new ruleta();
    private final JLabel mensajero = new JLabel("", SwingConstants.CENTER);
    private final JButton rendirse = RecursosVisuales.crearBoton("Retirarse");

    public opciones() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mensajero.setFont(new Font("Arial", Font.BOLD, 16));
        mensajero.setAlignmentX(Component.CENTER_ALIGNMENT);
        suerte.setAlignmentX(Component.CENTER_ALIGNMENT);
        rendirse.setAlignmentX(Component.CENTER_ALIGNMENT);
        limpiar();
        RecursosVisuales.aplicarTema(this);
    }

    public ruleta getRuleta() {
        return suerte;
    }

    public void setMensaje(String mensaje) {
        mensajero.setText("<html><div style='text-align:center;width:390px'>"
                + mensaje + "</div></html>");
    }

    public void setAccionRetiro(Runnable accion) {
        for (var listener : rendirse.getActionListeners()) {
            rendirse.removeActionListener(listener);
        }
        rendirse.addActionListener(e -> UiSeguro.ejecutar(this, accion));
    }

    public final void limpiar() {
        removeAll();
        add(suerte);
        add(Box.createVerticalStrut(20));
        add(mensajero);
        add(Box.createVerticalGlue());
        add(rendirse);
        revalidate();
        repaint();
    }
}
