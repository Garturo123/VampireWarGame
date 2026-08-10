package vampireswargame;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class IngresarSecion extends JFrame {
    public IngresarSecion() {
        setTitle("Iniciar sesión");
        setSize(480, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario = new PanelFondoGotico(new GridLayout(4, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JTextField usuario = new JTextField();
        JPasswordField password = new JPasswordField();
        JCheckBox mostrar = new JCheckBox("Mostrar contraseña");
        JButton ingresar = RecursosVisuales.crearBoton("Ingresar");
        JButton cancelar = RecursosVisuales.crearBoton("Cancelar");

        JLabel etiquetaUsuario = new JLabel("Nombre de usuario:");
        JLabel etiquetaPassword = new JLabel("Contraseña:");
        etiquetaUsuario.setLabelFor(usuario);
        etiquetaPassword.setLabelFor(password);
        usuario.setToolTipText("Ingresa el nombre con el que registraste tu cuenta");
        password.setToolTipText("Ingresa tu contraseña de 5 caracteres");
        usuario.getAccessibleContext().setAccessibleName("Nombre de usuario");
        password.getAccessibleContext().setAccessibleName("Contraseña");
        RecursosVisuales.configurarVisibilidad(mostrar, password);

        formulario.add(etiquetaUsuario);
        formulario.add(usuario);
        formulario.add(etiquetaPassword);
        formulario.add(password);
        formulario.add(new JLabel(""));
        formulario.add(mostrar);
        formulario.add(ingresar);
        formulario.add(cancelar);
        add(formulario);
        RecursosVisuales.aplicarTema(formulario);
        getRootPane().setDefaultButton(ingresar);

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
