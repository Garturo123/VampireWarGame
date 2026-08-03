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

public final class MiCuenta extends JFrame {
    public MiCuenta() {
        Jugador jugador = MenuPrincipal.getJugadorActual();
        setTitle("Mi cuenta");
        setSize(470, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contenido = new PanelFondoGotico(new GridLayout(8, 2, 8, 8));
        contenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPasswordField actual = new JPasswordField();
        JPasswordField nueva = new JPasswordField();
        JPasswordField confirmacion = new JPasswordField();
        JButton cambiar = RecursosVisuales.crearBoton("Cambiar contraseña");
        JButton cerrarCuenta = RecursosVisuales.crearBoton("Cerrar mi cuenta");
        JButton volver = RecursosVisuales.crearBoton("Volver");

        contenido.add(new JLabel("Usuario:"));
        contenido.add(new JLabel(jugador.getUserName()));
        contenido.add(new JLabel("Puntos:"));
        contenido.add(new JLabel(String.valueOf(jugador.getRanking())));
        contenido.add(new JLabel("Fecha de ingreso:"));
        contenido.add(new JLabel(jugador.getFechaIngresoFormateada()));
        contenido.add(new JLabel("Contraseña actual:"));
        contenido.add(actual);
        contenido.add(new JLabel("Nueva contraseña:"));
        contenido.add(nueva);
        contenido.add(new JLabel("Confirmar contraseña:"));
        contenido.add(confirmacion);
        contenido.add(cambiar);
        contenido.add(cerrarCuenta);
        contenido.add(volver);
        add(contenido, BorderLayout.CENTER);
        RecursosVisuales.aplicarTema(contenido);

        cambiar.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            try {
                jugador.cambiarPassword(
                        new String(actual.getPassword()),
                        new String(nueva.getPassword()),
                        new String(confirmacion.getPassword()));
                JOptionPane.showMessageDialog(this,
                        "La contraseña fue actualizada correctamente.");
                actual.setText("");
                nueva.setText("");
                confirmacion.setText("");
            } catch (ValidacionException excepcion) {
                JOptionPane.showMessageDialog(this, excepcion.getMessage(),
                        "No se pudo cambiar la contraseña",
                        JOptionPane.WARNING_MESSAGE);
            }
        }));
        cerrarCuenta.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Confirma que desea cerrar su cuenta?",
                    "Cerrar cuenta",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                jugador.cerrarCuenta();
                MenuPrincipal.setJugadorActual(null);
                dispose();
                new MenuInicio().setVisible(true);
            }
        }));
        volver.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        }));
    }
}
