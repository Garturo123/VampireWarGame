package vampireswargame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class CrearCuenta extends JFrame {
    public CrearCuenta() {
        setTitle("Crear jugador");
        setSize(390, 190);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JTextField usuario = new JTextField();
        JPasswordField password = new JPasswordField();
        JButton crear = new JButton("Crear");
        JButton cancelar = new JButton("Cancelar");

        formulario.add(new JLabel("Nombre de usuario:"));
        formulario.add(usuario);
        formulario.add(new JLabel("Contraseña (5 caracteres):"));
        formulario.add(password);
        formulario.add(crear);
        formulario.add(cancelar);
        add(formulario, BorderLayout.CENTER);

        crear.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            try {
                Jugador nuevo = Jugador.registrar(
                        usuario.getText(), new String(password.getPassword()));
                MenuPrincipal.setJugadorActual(nuevo);
                dispose();
                new MenuPrincipal().setVisible(true);
            } catch (ValidacionException excepcion) {
                JOptionPane.showMessageDialog(this, excepcion.getMessage(),
                        "No se pudo crear la cuenta", JOptionPane.WARNING_MESSAGE);
            }
        }));
        cancelar.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            dispose();
            new MenuInicio().setVisible(true);
        }));
    }
}
