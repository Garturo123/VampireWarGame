package vampireswargame;

import java.awt.Component;
import java.awt.Font;
import javax.swing.*;

/**
 *
 * @author gaat1
 */
public class opciones extends JPanel{
    private static ruleta suerte = new ruleta();
    private static JLabel mensajero= new JLabel("");
    private JButton rendirse = new JButton("Rendirse");
    private JButton botonInvocar = null;
    private JButton botonChupar = new JButton("No Chupar Sangre");
    public opciones(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        rendirse.setFont(new Font("Arial", Font.BOLD, 30 ));
        mensajero.setFont(new Font("Arial", Font.BOLD, 20 ));
       botonChupar.setFont(new Font("Arial", Font.BOLD, 20));
       
        botonChupar.setAlignmentX(Component.CENTER_ALIGNMENT);
        suerte.setAlignmentX(Component.CENTER_ALIGNMENT);
        rendirse.setAlignmentX(Component.CENTER_ALIGNMENT);
        mensajero.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        limpiar();
        
        rendirse.addActionListener(e ->{
            if(Tablero.TurnoBlanco)
                Piezas.blancos = 0;
            else
                Piezas.negros = 0;
            Tablero.terminarJuego();
        
        });
        
    }
    public static ruleta getRuleta(){
        return suerte;
    }
    public static void setMensaje(String mensaje){
        mensajero.setText(mensaje);
    }
    
    public void mostrarBotonInvocar(JButton piezaMuerte) {
       if (botonInvocar != null) {
            remove(botonInvocar);
        }

        botonInvocar = new JButton("Invocar zombie");
        botonInvocar.setFont(new Font("Arial", Font.BOLD, 20));
        botonInvocar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Limpiamos el panel y rearmamos el orden de los componentes
        removeAll();

        add(Box.createVerticalGlue());
        add(suerte);
        add(Box.createVerticalStrut(100));
        add(mensajero);
        add(Box.createVerticalStrut(40));
        add(botonInvocar); 
        add(Box.createVerticalStrut(60));
        add(rendirse);
        add(Box.createVerticalGlue());

        revalidate();
        repaint();

        // Acción del botón
        botonInvocar.addActionListener(e -> {
            Pieza muerte = (Pieza) piezaMuerte.getClientProperty("Tipo");
            
            if (Tablero.getMuerteSeleccionada() != null) {
                Tablero.getMuerteSeleccionada().Habilidad(piezaMuerte, Tablero.getBotones(), Juego.getTablero());
            } else {
                JOptionPane.showMessageDialog(null, "No hay una Muerte seleccionada para invocar.");
            }
            
            remove(botonInvocar); 
            botonInvocar = null;

            limpiar();
        });
    }
    public void mostrarBotonChupar(JButton piezaVampiro) {
      
        removeAll();

        add(Box.createVerticalGlue());
        add(suerte);
        add(Box.createVerticalStrut(100));
        add(mensajero);
        add(Box.createVerticalStrut(40));
        add(botonChupar); 
        add(Box.createVerticalStrut(60));
        add(rendirse);
        add(Box.createVerticalGlue());

        revalidate();
        repaint();
        for (var al : botonChupar.getActionListeners()) {
            botonChupar.removeActionListener(al);
        }

        // Acción del botón
        botonChupar.addActionListener(e -> {
            Pieza vampiro = (Pieza) piezaVampiro.getClientProperty("Tipo");
            vampiro.Habilidad(piezaVampiro, Tablero.getBotones(), Juego.getTablero());
            if (vampiro.chupar)
                botonChupar.setText("Chupar Sangre");
            else
                botonChupar.setText("No Chupar Sangre");
            
        });
    }
    public final void limpiar(){
        removeAll();
        add(Box.createVerticalGlue());
        add(suerte);
        add(Box.createVerticalStrut(100));
        add(mensajero);
        add(Box.createVerticalStrut(100));
        add(rendirse);
        add(Box.createVerticalGlue());

        revalidate();
         repaint();
    }
}
