package vampireswargame;

import java.awt.BorderLayout;
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

public class CrearCuenta extends JFrame {
    public CrearCuenta() {
        setTitle("Crear jugador");
        setSize(480, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario = new PanelFondoGotico(new GridLayout(4, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JTextField usuario = new JTextField();
        JPasswordField password = new JPasswordField();
        JCheckBox mostrar = new JCheckBox("Mostrar contraseña");
        JButton crear = RecursosVisuales.crearBoton("Crear");
        JButton cancelar = RecursosVisuales.crearBoton("Cancelar");

        JLabel etiquetaUsuario = new JLabel("Nombre de usuario:");
        JLabel etiquetaPassword = new JLabel("Contraseña (5 caracteres):");
        etiquetaUsuario.setLabelFor(usuario);
        etiquetaPassword.setLabelFor(password);
        usuario.setToolTipText("Escribe un nombre único para tu cuenta");
        password.setToolTipText("La contraseña debe tener exactamente 5 caracteres");
        usuario.getAccessibleContext().setAccessibleName("Nombre de usuario");
        password.getAccessibleContext().setAccessibleName("Contraseña de 5 caracteres");
        RecursosVisuales.configurarVisibilidad(mostrar, password);

        formulario.add(etiquetaUsuario);
        formulario.add(usuario);
        formulario.add(etiquetaPassword);
        formulario.add(password);
        formulario.add(new JLabel(""));
        formulario.add(mostrar);
        formulario.add(crear);
        formulario.add(cancelar);
        add(formulario, BorderLayout.CENTER);
        RecursosVisuales.aplicarTema(formulario);
        getRootPane().setDefaultButton(crear);

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
