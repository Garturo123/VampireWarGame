package vampireswargame;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuPrincipal extends JFrame {
    private static Jugador jugadorActual;

    public MenuPrincipal() {
        setTitle("Vampire Wargame - Menú principal");
        setSize(430, 310);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JButton jugar = new JButton("Nueva partida");
        JButton miCuenta = new JButton("Mi cuenta");
        JButton reportes = new JButton("Reportes");
        JButton cerrarSesion = new JButton("Cerrar sesión");
        JButton[] botones = {jugar, miCuenta, reportes, cerrarSesion};
        for (JButton boton : botones) {
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        panel.add(Box.createVerticalGlue());
        for (JButton boton : botones) {
            panel.add(boton);
            panel.add(Box.createVerticalStrut(12));
        }
        panel.add(Box.createVerticalGlue());
        add(panel);

        jugar.addActionListener(e -> UiSeguro.ejecutar(this, this::iniciarPartida));
        miCuenta.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            dispose();
            new MiCuenta().setVisible(true);
        }));
        reportes.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            dispose();
            new reportes().setVisible(true);
        }));
        cerrarSesion.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            jugadorActual = null;
            dispose();
            new MenuInicio().setVisible(true);
        }));
    }

    private void iniciarPartida() {
        RepositorioSistema repositorio = MemoriaSistema.getInstancia();
        Jugador[] oponentes = repositorio.obtenerOponentes(jugadorActual);
        if (oponentes.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe existir otro jugador activo para iniciar una partida.",
                    "Sin oponentes", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Jugador elegido = (Jugador) JOptionPane.showInputDialog(
                this,
                "Seleccione al oponente:",
                "Nueva partida",
                JOptionPane.QUESTION_MESSAGE,
                null,
                oponentes,
                oponentes[0]);
        if (elegido != null) {
            dispose();
            new Juego(jugadorActual, elegido).setVisible(true);
        }
    }

    public static Jugador getJugadorActual() {
        return jugadorActual;
    }

    public static void setJugadorActual(Jugador jugador) {
        jugadorActual = jugador;
    }
}
