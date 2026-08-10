package vampireswargame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** Panel de ayuda, jugadores y controles que acompaña al tablero. */
public class opciones extends PanelFondoGotico {
    private final PanelJugador jugadorNegro = new PanelJugador();
    private final PanelJugador jugadorBlanco = new PanelJugador();
    private final ruleta suerte = new ruleta();
    private final JLabel turnoActual = new JLabel("", SwingConstants.CENTER);
    private final JLabel mensajero = new JLabel("", SwingConstants.CENTER);
    private final JLabel informacionPieza = new JLabel(
            "Selecciona una pieza para consultar sus atributos.",
            SwingConstants.CENTER);
    private final JLabel leyenda = new JLabel(
            "<html><b>Guía:</b> <font color='#75d9ee'>■ Movimiento</font> "
            + "<font color='#ef5968'>■ Ataque</font> "
            + "<font color='#f3d76e'>■ Invocación</font></html>",
            SwingConstants.CENTER);
    private final JButton rendirse = RecursosVisuales.crearBoton("Retirarse");

    public opciones() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(450, 840));
        setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        turnoActual.setFont(new Font("Serif", Font.BOLD, 18));
        turnoActual.setForeground(new Color(245, 215, 115));
        mensajero.setFont(new Font("Serif", Font.BOLD, 14));
        informacionPieza.setFont(new Font("Serif", Font.PLAIN, 14));
        JLabel[] etiquetas = {turnoActual, mensajero, informacionPieza, leyenda};
        for (JLabel etiqueta : etiquetas) {
            etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        suerte.setAlignmentX(Component.CENTER_ALIGNMENT);
        rendirse.setAlignmentX(Component.CENTER_ALIGNMENT);
        limpiar();
        RecursosVisuales.aplicarTema(this);
    }

    public void configurarJugadores(Jugador blanco, Jugador negro) {
        jugadorNegro.configurar("Jugador 2", negro, "negro", "parte superior");
        jugadorBlanco.configurar("Jugador 1", blanco, "blanco", "parte inferior");
    }

    public void actualizarTurno(Jugador jugador, String equipo) {
        boolean turnoBlanco = "blanco".equals(equipo);
        jugadorBlanco.setTurno(turnoBlanco);
        jugadorNegro.setTurno(!turnoBlanco);
        turnoActual.setText("Turno: " + jugador.getUserName()
                + " • Equipo " + equipo.toUpperCase());
    }

    public void actualizarCapturas(Pieza[] blancas, int cantidadBlancas,
            Pieza[] negras, int cantidadNegras) {
        jugadorBlanco.actualizarCapturas(blancas, cantidadBlancas);
        jugadorNegro.actualizarCapturas(negras, cantidadNegras);
    }

    public void mostrarInformacionPieza(Pieza pieza) {
        if (pieza == null) {
            informacionPieza.setText(
                    "Selecciona una pieza para consultar sus atributos.");
            return;
        }
        informacionPieza.setText("<html><div style='text-align:center;width:410px'>"
                + "<b>" + capitalizar(pieza.getNombre()) + " "
                + pieza.getEquipo().toUpperCase() + "</b> • Ataque: "
                + pieza.getAtaque() + " • Vida: " + pieza.getSalud() + "/"
                + pieza.getSaludMaxima() + " • Escudo: " + pieza.getEscudo()
                + " • Movimiento: " + pieza.getMovilidad() + "<br>"
                + descripcionHabilidad(pieza) + "</div></html>");
    }

    private String descripcionHabilidad(Pieza pieza) {
        if (pieza instanceof Vampiro) {
            return "Especial: absorbe 1 punto de sangre y recupera vida.";
        }
        if (pieza instanceof Lobo) {
            return "Especial: puede desplazarse hasta 2 casillas.";
        }
        if (pieza instanceof Muerte) {
            return "Especial: lanza penetrante, invocación y control de Zombies.";
        }
        return "Zombie: se mueve y ataca durante el turno de su Muerte.";
    }

    private String capitalizar(String texto) {
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    public ruleta getRuleta() {
        return suerte;
    }

    public void setMensaje(String mensaje) {
        mensajero.setText("<html><div style='text-align:center;width:410px'>"
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
        add(jugadorNegro);
        add(Box.createVerticalStrut(4));
        add(suerte);
        add(Box.createVerticalStrut(4));
        add(turnoActual);
        add(mensajero);
        add(Box.createVerticalStrut(4));
        add(panelInformacion());
        add(leyenda);
        add(Box.createVerticalGlue());
        add(jugadorBlanco);
        add(Box.createVerticalStrut(4));
        add(rendirse);
        revalidate();
        repaint();
    }

    private JPanel panelInformacion() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(145, 128, 100)),
                "Información de la pieza", 0, 0,
                new Font("Serif", Font.BOLD, 13),
                RecursosVisuales.TEXTO_MARFIL));
        panel.add(informacionPieza);
        panel.setMaximumSize(new Dimension(430, 80));
        return panel;
    }
}
