package vampireswargame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/** Recursos gráficos compartidos por las pantallas del juego. */
public final class RecursosVisuales {
    public static final Color FONDO_GOTICO = new Color(8, 10, 15);
    public static final Color TEXTO_MARFIL = new Color(226, 218, 194);

    private RecursosVisuales() {
    }

    public static JLabel crearLogo(int ancho, int alto) {
        JLabel logo = new JLabel("VAMPIRE WARGAME", SwingConstants.CENTER);
        logo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        logo.setForeground(TEXTO_MARFIL);
        logo.setFont(new Font("Serif", Font.BOLD, 30));
        logo.getAccessibleContext().setAccessibleName(
                "Logotipo de Vampire Wargame");
        try {
            URL recurso = RecursosVisuales.class.getResource(
                    "/vampireswargame/imagenes/LogoVampireWargame.png");
            if (recurso != null) {
                Image imagen = new ImageIcon(recurso).getImage()
                        .getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                logo.setText("");
                logo.setIcon(new ImageIcon(imagen));
            }
        } catch (RuntimeException excepcion) {
            // El texto alternativo mantiene el menú utilizable.
        }
        return logo;
    }
}
