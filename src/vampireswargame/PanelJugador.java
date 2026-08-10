package vampireswargame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Panel permanente que identifica a un jugador y sus capturas. */
public final class PanelJugador extends JPanel {
    private static final Color FONDO_CAPTURAS =
            new Color(205, 210, 218, 190);
    private static final Color TEXTO_CAPTURAS = new Color(24, 27, 33);
    private final JLabel identidad = new JLabel();
    private final JLabel ubicacion = new JLabel();
    private final JLabel resumenCapturas = new JLabel("Capturas: ninguna");
    private final JPanel iconosCapturados = new PanelCapturas();
    private String titulo = "Jugador";

    public PanelJugador() {
        setLayout(new BorderLayout(6, 2));
        setOpaque(false);
        setMaximumSize(new Dimension(420, 92));
        setPreferredSize(new Dimension(420, 92));
        identidad.setFont(new Font("Serif", Font.BOLD, 17));
        ubicacion.setFont(new Font("Serif", Font.PLAIN, 13));
        resumenCapturas.setFont(new Font("Serif", Font.PLAIN, 13));
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(identidad);
        textos.add(ubicacion);
        textos.add(resumenCapturas);
        add(textos, BorderLayout.CENTER);
        add(iconosCapturados, BorderLayout.EAST);
        setTurno(false);
        RecursosVisuales.aplicarTema(this);
        iconosCapturados.setOpaque(false);
        iconosCapturados.setBorder(
                BorderFactory.createEmptyBorder(3, 5, 3, 5));
    }

    public void configurar(String numero, Jugador jugador, String equipo,
            String posicion) {
        titulo = numero;
        identidad.setText(numero + ": " + jugador.getUserName());
        ubicacion.setText(capitalizar(equipo) + " • " + posicion);
        identidad.getAccessibleContext().setAccessibleDescription(
                numero + " " + jugador.getUserName() + ", equipo " + equipo);
    }

    public void setTurno(boolean turno) {
        Color color = turno ? new Color(235, 205, 105)
                : new Color(100, 92, 78);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(color, turno ? 3 : 1),
                turno ? "TURNO ACTUAL" : titulo,
                0, 0, new Font("Serif", Font.BOLD, 13), color));
    }

    public void actualizarCapturas(Pieza[] capturas, int cantidad) {
        iconosCapturados.removeAll();
        int vampiros = 0;
        int lobos = 0;
        int muertes = 0;
        int zombies = 0;
        for (int i = 0; i < cantidad; i++) {
            Pieza pieza = capturas[i];
            if (pieza == null) {
                continue;
            }
            switch (pieza.getNombre()) {
                case "vampiro" -> vampiros++;
                case "lobo" -> lobos++;
                case "muerte" -> muertes++;
                case "zombie" -> zombies++;
                default -> { }
            }
            if (i < 5) {
                ImageIcon icono = RecursosVisuales.cargarIconoPieza(
                        pieza, 28, 28);
                JLabel miniatura = new JLabel(icono);
                miniatura.setToolTipText(
                        "Capturada: " + capitalizar(pieza.getNombre()));
                iconosCapturados.add(miniatura);
            }
        }
        if (cantidad > 5) {
            JLabel adicionales = new JLabel("+" + (cantidad - 5));
            adicionales.setForeground(TEXTO_CAPTURAS);
            adicionales.setFont(new Font("Serif", Font.BOLD, 13));
            adicionales.setToolTipText("Capturas adicionales");
            iconosCapturados.add(adicionales);
        }
        resumenCapturas.setText(cantidad == 0 ? "Capturas: ninguna"
                : "Capturas: " + cantidad + " (V:" + vampiros
                + " L:" + lobos + " M:" + muertes + " Z:" + zombies + ")");
        iconosCapturados.revalidate();
        iconosCapturados.repaint();
    }

    private String capitalizar(String texto) {
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    /** Superficie gris translúcida que conserva visible el fondo gótico. */
    private static final class PanelCapturas extends JPanel {
        private PanelCapturas() {
            super(new FlowLayout(FlowLayout.LEFT, 2, 0));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics grafico) {
            super.paintComponent(grafico);
            Graphics2D g = (Graphics2D) grafico.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(FONDO_CAPTURAS);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g.dispose();
        }
    }
}
