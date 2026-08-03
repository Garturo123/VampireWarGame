package vampireswargame;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuInicio extends JFrame {
    public MenuInicio() {
        setTitle("Vampire Wargame - Inicio");
        setSize(680, 570);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new PanelFondoGotico();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(RecursosVisuales.FONDO_GOTICO);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 30, 24, 30));

        JLabel logo = RecursosVisuales.crearLogo(600, 300);

        JButton iniciarSesion = RecursosVisuales.crearBoton("Iniciar sesión");
        JButton crearJugador = RecursosVisuales.crearBoton("Crear jugador");
        JButton salir = RecursosVisuales.crearBoton("Salir");
        JButton[] botones = {iniciarSesion, crearJugador, salir};
        for (JButton boton : botones) {
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        panel.add(logo);
        panel.add(Box.createVerticalStrut(12));
        panel.add(Box.createVerticalGlue());
        panel.add(iniciarSesion);
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearJugador);
        panel.add(Box.createVerticalStrut(12));
        panel.add(salir);
        panel.add(Box.createVerticalGlue());
        add(panel);
        RecursosVisuales.aplicarTema(panel);

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
