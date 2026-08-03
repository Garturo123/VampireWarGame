package vampireswargame;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class IngresarSecion extends JFrame {
    public IngresarSecion() {
        setTitle("Iniciar sesión");
        setSize(390, 190);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JTextField usuario = new JTextField();
        JPasswordField password = new JPasswordField();
        JButton ingresar = new JButton("Ingresar");
        JButton cancelar = new JButton("Cancelar");

        formulario.add(new JLabel("Nombre de usuario:"));
        formulario.add(usuario);
        formulario.add(new JLabel("Contraseña:"));
        formulario.add(password);
        formulario.add(ingresar);
        formulario.add(cancelar);
        add(formulario);

        ingresar.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            try {
                Jugador jugador = Jugador.autenticar(
                        usuario.getText(), password.getPassword());
                MenuPrincipal.setJugadorActual(jugador);
                dispose();
                new MenuPrincipal().setVisible(true);
            } catch (ValidacionException excepcion) {
                JOptionPane.showMessageDialog(this, excepcion.getMessage(),
                        "Inicio de sesión rechazado", JOptionPane.WARNING_MESSAGE);
                dispose();
                new MenuInicio().setVisible(true);
            }
        }));
        cancelar.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            dispose();
            new MenuInicio().setVisible(true);
        }));
    }
}
