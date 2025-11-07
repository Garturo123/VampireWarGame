package vampireswargame;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author gaat1
 */
public class BotonEscalable extends JButton {
    private Image imagenOriginal;

    public BotonEscalable(ImageIcon icono) {
        this.imagenOriginal = icono.getImage();

        // Detectar cuando el botón cambia de tamaño
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensionarIcono();
            }
        });
    }

    private void redimensionarIcono() {
        
        if (imagenOriginal != null) {
            int ancho = getWidth();
            int alto = getHeight();
            if (ancho > 0 && alto > 0) {
                Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(imagenEscalada));
            }
        }
    }
}
