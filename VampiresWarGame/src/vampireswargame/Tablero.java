package vampireswargame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author gaat1
 */
public class Tablero extends JPanel implements ActionListener{
    private JButton[][] botones = new JButton[6][6];
    
    public static Piezas PiezaTipo (Piezas tipo) {
        
        return tipo;
    }
    public Tablero(){
        
        this.setLayout(new GridLayout(6,6));
        boolean blancoUltimo = false;
        
        for (int Fila = 0; Fila < 6; Fila++){
            
            for(int Columna = 0; Columna < 6;Columna++){
                botones[Fila][Columna] = new JButton();
                botones[Fila][Columna].putClientProperty("Tipo", null);
                botones[Fila][Columna].putClientProperty("equipo", null);
                //Negras
                if(Fila == 0){
                    if(Columna==1 || Columna == 4){
                        botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/VampiroNegro.png")));
                        botones[Fila][Columna].putClientProperty("Tipo", new Vampiro());
                    }
                    else if(Columna==0 || Columna == 5){
                        botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/LoboNegro.png")));
                        botones[Fila][Columna].putClientProperty("Tipo", new Lobo());
                    }
                    else if(Columna==2 || Columna == 3){
                        botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/MuerteNegro.png")));
                        botones[Fila][Columna].putClientProperty("Tipo", new Muerte());
                    }
                    botones[Fila][Columna].putClientProperty("equipo", "negro");
                }
                //Blancas
                if(Fila == 5){
                    if(Columna==1 || Columna == 4){
                        botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/VampiroBlanco.png")));
                        botones[Fila][Columna].putClientProperty("Tipo", new Vampiro());
                    }    
                    else if(Columna==0 || Columna == 5){
                        botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/LoboBlanco.png")));
                        botones[Fila][Columna].putClientProperty("Tipo", new Lobo());
                    }    
                    else if(Columna==2 || Columna == 3){
                        botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/MuerteBlanco.png")));
                        botones[Fila][Columna].putClientProperty("Tipo", new Muerte());
                    }
                    botones[Fila][Columna].putClientProperty("equipo", "blanco");
                }
                
                botones[Fila][Columna].putClientProperty("Fila", Fila);
                botones[Fila][Columna].putClientProperty("Columna", Columna);
                
                botones[Fila][Columna].addActionListener(this);
                this.revalidate();
                this.repaint();
                
                //Fondo
                if(blancoUltimo == false){
                    botones[Fila][Columna].setBackground(Color.white);
                    blancoUltimo = true;
                    
                }
                else{
                    botones[Fila][Columna].setBackground(Color.DARK_GRAY);
                    blancoUltimo = false;
                }
                add(botones[Fila][Columna]);
                
            }
            if(blancoUltimo)
                blancoUltimo = false;
            else
                blancoUltimo = true;
            
        }
       
    }
    
    public void actionPerformed(ActionEvent e){
        JButton boton = (JButton) e.getSource();
        limpiarColores(botones);
        int fila = (int) boton.getClientProperty("Fila");
        int columna = (int) boton.getClientProperty("Columna");
        Pieza tipo = (Pieza) boton.getClientProperty("Tipo");
        if(tipo != null){
            System.out.println("Presionaste el boton en fila " + fila + " columna " + columna+ " tipo: "+ tipo.getNombre());
            if(tipo.getNombre().equals("vampiro")){
                Piezas.VAMPIRO.moverse(botones[fila][columna] , botones, this);
                Piezas.VAMPIRO.ataque(botones[fila][columna] , botones, this);
            }
            else if(tipo.getNombre().equals("muerte")){
                Piezas.MUERTE.moverse(botones[fila][columna] , botones, this);
                Piezas.MUERTE.ataque(botones[fila][columna] , botones, this);
            }
            else if(tipo.getNombre().equals("lobo")){
                Piezas.HOMBRELOBO.moverse(botones[fila][columna] , botones, this);
                Piezas.HOMBRELOBO.ataque(botones[fila][columna] , botones, this);
            }
        }
 
    }
    public  void limpiarColores(JButton[][] tablero) {
         boolean blancoUltimo = false;
        for (int Fila = 0; Fila < 6; Fila++){

           for(int Columna = 0; Columna < 6;Columna++){

               if(blancoUltimo == false){
                   tablero[Fila][Columna].setBackground(Color.white);
                   blancoUltimo = true;

               }
               else{
                   tablero[Fila][Columna].setBackground(Color.DARK_GRAY);
                   blancoUltimo = false;
               }

           }
            if(blancoUltimo)
                blancoUltimo = false;
            else
                blancoUltimo = true;
                
        }
        
       
    }
}
