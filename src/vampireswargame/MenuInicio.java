package vampireswargame;

/**
 *
 * @author gaat1
 */
import java.awt.*;
import javax.swing.*;

public class MenuInicio extends JFrame{
        public MenuInicio(){
        setTitle("Menu de Inicio");
        setSize(300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JButton logIN = new JButton("Log In");
        JButton crearCuenta = new JButton("Crear Cuenta");
        JButton salir = new JButton("Salir");
        
        logIN.setAlignmentX(Component.CENTER_ALIGNMENT);
        crearCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
        salir.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalGlue());
        panel.add(logIN);
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearCuenta);
        panel.add(Box.createVerticalStrut(10));
        panel.add(salir);
        panel.add(Box.createVerticalGlue());
        
        add(panel);
        
        logIN.addActionListener(e ->{ 
            System.out.println("Log in");
            IngresarSecion frame = new IngresarSecion();
             this.setVisible(false);
               frame.setVisible(true);
                });
        crearCuenta.addActionListener(e -> {
            SwingUtilities.invokeLater(()->{
                CrearCuenta frame = new CrearCuenta();
                this.setVisible(false);
               frame.setVisible(true);
               
            });
            
        });
        salir.addActionListener(e -> System.out.println("Salir"));
        }
    }