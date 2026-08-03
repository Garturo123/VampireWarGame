package vampireswargame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/** Recursos gráficos compartidos por las pantallas del juego. */
public final class RecursosVisuales {
    public static final Color FONDO_GOTICO = new Color(8, 10, 15);
    public static final Color TEXTO_MARFIL = new Color(226, 218, 194);

    private RecursosVisuales() {
    }

    public static void inicializarTemaGlobal() {
        UIManager.put("OptionPane.background", FONDO_GOTICO);
        UIManager.put("Panel.background", FONDO_GOTICO);
        UIManager.put("OptionPane.messageForeground", TEXTO_MARFIL);
        UIManager.put("Button.font", new Font("Serif", Font.BOLD, 14));
        UIManager.put("Label.font", new Font("Serif", Font.PLAIN, 15));
    }

    public static BotonGotico crearBoton(String texto) {
        return new BotonGotico(texto);
    }

    public static void aplicarTema(Component componente) {
        if (componente instanceof JLabel etiqueta && etiqueta.getIcon() == null) {
            etiqueta.setForeground(TEXTO_MARFIL);
            if (etiqueta.getFont() == null || !etiqueta.getFont().isBold()) {
                etiqueta.setFont(new Font("Serif", Font.PLAIN, 15));
            }
        }
        if (componente instanceof JTextField campo) {
            campo.setBackground(new Color(20, 22, 28));
            campo.setForeground(TEXTO_MARFIL);
            campo.setCaretColor(new Color(205, 55, 70));
            campo.setSelectionColor(new Color(120, 25, 38));
            campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(145, 128, 100)),
                    BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        }
        if (componente instanceof JTextArea area) {
            area.setBackground(new Color(15, 17, 22));
            area.setForeground(TEXTO_MARFIL);
            area.setCaretColor(TEXTO_MARFIL);
            area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
        if (componente instanceof JTable tabla) {
            tabla.setBackground(new Color(18, 20, 26));
            tabla.setForeground(TEXTO_MARFIL);
            tabla.setGridColor(new Color(100, 30, 40));
            tabla.setSelectionBackground(new Color(105, 18, 30));
            tabla.setSelectionForeground(Color.WHITE);
            tabla.getTableHeader().setBackground(new Color(60, 12, 22));
            tabla.getTableHeader().setForeground(TEXTO_MARFIL);
            tabla.setRowHeight(25);
        }
        if (componente instanceof JTabbedPane pestanas) {
            pestanas.setUI(new PestanasGoticasUI());
            pestanas.setBackground(new Color(20, 22, 28));
            pestanas.setForeground(TEXTO_MARFIL);
        }
        if (componente instanceof JScrollPane desplazamiento) {
            desplazamiento.setBorder(BorderFactory.createLineBorder(
                    new Color(145, 128, 100)));
            desplazamiento.getViewport().setBackground(new Color(15, 17, 22));
        }
        if (componente instanceof JComponent swing
                && !(componente instanceof PanelFondoGotico)
                && !(componente instanceof BotonGotico)
                && !(componente instanceof CasillaTablero)
                && !(componente instanceof JTextField)
                && !(componente instanceof JTextArea)
                && !(componente instanceof JTable)
                && !(componente instanceof JScrollPane)) {
            swing.setOpaque(false);
        }
        if (componente instanceof Container contenedor) {
            for (Component hijo : contenedor.getComponents()) {
                aplicarTema(hijo);
            }
        }
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
