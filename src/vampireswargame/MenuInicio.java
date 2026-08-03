package vampireswargame;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MenuInicio extends JFrame {
    public MenuInicio() {
        setTitle("Vampire Wargame - Inicio");
        setSize(360, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton iniciarSesion = new JButton("Iniciar sesión");
        JButton crearJugador = new JButton("Crear jugador");
        JButton salir = new JButton("Salir");
        JButton[] botones = {iniciarSesion, crearJugador, salir};
        for (JButton boton : botones) {
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        panel.add(Box.createVerticalGlue());
        panel.add(iniciarSesion);
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearJugador);
        panel.add(Box.createVerticalStrut(12));
        panel.add(salir);
        panel.add(Box.createVerticalGlue());
        add(panel);

        iniciarSesion.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            dispose();
            new IngresarSecion().setVisible(true);
        }));
        crearJugador.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            dispose();
            new CrearCuenta().setVisible(true);
        }));
        salir.addActionListener(e -> System.exit(0));
    }
}
