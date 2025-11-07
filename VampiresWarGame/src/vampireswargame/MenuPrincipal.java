package vampireswargame;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author gaat1
 */
public class MenuPrincipal extends JFrame {
    static Jugador JugadorActual;
    public MenuPrincipal(){
        setTitle("Menu de Principal");
        setSize(300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JButton jugar = new JButton("Jugar Vampire Castlevania");
        JButton miCuenta = new JButton("Mi Cuenta");
        JButton Reportes = new JButton("Reportes");
        JButton LogOut = new JButton("LogOut");
        
        jugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        miCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
        Reportes.setAlignmentX(Component.CENTER_ALIGNMENT);
        LogOut.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalGlue());
        panel.add(jugar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(miCuenta);
        panel.add(Box.createVerticalStrut(10));
        panel.add(Reportes);
        panel.add(Box.createVerticalStrut(10));
        panel.add(LogOut);
        panel.add(Box.createVerticalGlue());
        
        add(panel);
        jugar.addActionListener(e ->{ 
            SwingUtilities.invokeLater(()->{
                Juego Inicio = new Juego ();
                this.setVisible(false);
               Inicio.setVisible(true);
               });
            });
        miCuenta.addActionListener(e ->{ 
            
            });
        Reportes.addActionListener(e ->{ 
            
            });
        LogOut.addActionListener(e ->{ 
            JugadorActual   = null ;
            SwingUtilities.invokeLater(()->{
                MenuInicio Inicio = new MenuInicio ();
                this.setVisible(false);
               Inicio.setVisible(true);
               });
            });
    }
}