package vampireswargame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static vampireswargame.Piezas.blancos;
import static vampireswargame.Piezas.negros;

/**
 *
 * @author gaat1
 */
public class Tablero extends JPanel implements ActionListener{
    private static JButton[][] botones = new JButton[6][6];
    private ruleta suerte;
    private opciones panel;
    public static boolean TurnoBlanco = false;
    private static Muerte muerteSeleccionada = null;

    private static JFrame juego;
    
    public static void terminarJuego(){
         if(blancos == 0 || negros == 0){
             if(blancos == 0)
                JOptionPane.showMessageDialog(null, "Victoria del equipo negro.");
             else
                JOptionPane.showMessageDialog(null, "Victoria del equipo blanco."); 
            SwingUtilities.invokeLater(()->{
                MenuPrincipal principal = new MenuPrincipal();
                juego.dispose();
                principal.setVisible(true);
            });
        }
    }
    public static Piezas PiezaTipo (Piezas tipo) {
        
        return tipo;
    }
    
    public Tablero(opciones panel, JFrame juego){
        this.panel = panel;
        this.juego = juego;
        suerte = panel.getRuleta();
        suerte.setRuletaListener(new ruleta.RuletaListener() {
            @Override
            public void onRuletaFinalizada(String resultado) {
                TurnoBlanco = !TurnoBlanco; 
                
                Piezas.seMovio = false;  // ahora el jugador puede mover
                panel.setMensaje("Turno del equipo: " + (TurnoBlanco ? "blanco" : "negro"));
            }
        });
        
        
        this.setLayout(new GridLayout(6,6));
        boolean blancoUltimo = false;
        
        for (int Fila = 0; Fila < 6; Fila++){
            
            for(int Columna = 0; Columna < 6;Columna++){
                botones[Fila][Columna] = new JButton();
                botones[Fila][Columna].putClientProperty("Tipo", null);
                botones[Fila][Columna].putClientProperty("equipo", null);
                //Negras
                if(Fila == 0){
                    switch(Columna){
                        case 1, 4 -> {
                            botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/VampiroNegro.png")));
                            botones[Fila][Columna].putClientProperty("Tipo", new Vampiro());
                        }
                        case 0, 5 -> {
                            botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/LoboNegro.png")));
                            botones[Fila][Columna].putClientProperty("Tipo", new Lobo());
                        }
                        case 2, 3 -> {
                            botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/MuerteNegro.png")));
                            botones[Fila][Columna].putClientProperty("Tipo", new Muerte());
                        }
                    }
                    botones[Fila][Columna].putClientProperty("equipo", "negro");
                }
                //Blancas
                if(Fila == 5){
                    switch (Columna){
                        case 1, 4 -> {
                            botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/VampiroBlanco.png")));
                            botones[Fila][Columna].putClientProperty("Tipo", new Vampiro());
                        }
                        case 0, 5 -> {
                            botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/LoboBlanco.png")));
                            botones[Fila][Columna].putClientProperty("Tipo", new Lobo());
                        }
                        case 2, 3 -> {
                            botones[Fila][Columna]= new BotonEscalable(new ImageIcon(getClass().getResource("/vampireswargame/imagenes/MuerteBlanco.png")));
                            botones[Fila][Columna].putClientProperty("Tipo", new Muerte());
                        }
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
            blancoUltimo = !blancoUltimo;
            
        }
       
    }
    public static boolean hayPiezaDisponible(String tipo, String equipo) {
        switch(tipo) {
            case "vampiro": return equipo.equals("blanco") ? Piezas.vampirosBlancos > 0 : Piezas.vampirosNegros > 0;
            case "lobo": return equipo.equals("blanco") ? Piezas.lobosBlancos > 0 : Piezas.lobosNegros > 0;
            case "muerte": return equipo.equals("blanco") ? Piezas.muertesBlancas > 0 : Piezas.muertesNegras > 0;
        }
        return false;
    }
    public static int contarTiposPerdidos(String equipo) {
        int perdidos = 0;

        if(equipo.equals("blanco")) {
            if(Piezas.vampirosBlancos == 0) perdidos++;
            if(Piezas.lobosBlancos == 0) perdidos++;
            if(Piezas.muertesBlancas == 0) perdidos++;
        } else {
            if(Piezas.vampirosNegros == 0) perdidos++;
            if(Piezas.lobosNegros == 0) perdidos++;
            if(Piezas.muertesNegras == 0) perdidos++;
        }

        return perdidos;
    }
    public void actionPerformed(ActionEvent e){
        JButton boton = (JButton) e.getSource();
        limpiarColores(botones);
        
        if(Piezas.seMovio || Piezas.blancos == 0 || Piezas.negros == 0)
            return;
        
        
        String turnoEquipo = TurnoBlanco ? "blanco" : "negro";
        int fila = (int) boton.getClientProperty("Fila");
        int columna = (int) boton.getClientProperty("Columna");
        Pieza tipo = (Pieza) boton.getClientProperty("Tipo");
        String equipo = (String) boton.getClientProperty("equipo");
        suerte.getResultado();
        if(tipo != null ){
            System.out.println("Presionaste el boton en fila " + fila + " columna " + columna+ " tipo: "+ tipo.getNombre());
            
            if(suerte.getResultado().equals((tipo.getNombre())) && turnoEquipo.equals(equipo)) {
                switch (tipo.getNombre()){
                    case "vampiro" -> {
                        Piezas.VAMPIRO.moverse(botones[fila][columna] , botones, this);
                        Piezas.VAMPIRO.ataque(botones[fila][columna] , botones, this);
                        panel.mostrarBotonChupar(boton);
    
                    }
                    case "muerte" -> {
                        Muerte muerte = (Muerte) tipo;
                        muerteSeleccionada = muerte;
                        
                        Piezas.MUERTE.moverse(botones[fila][columna] , botones, this);
                        Piezas.MUERTE.ataque(botones[fila][columna] , botones, this);
  
                        panel.mostrarBotonInvocar(boton);
                        
                        for (JButton zombieBtn : muerteSeleccionada.getZombiesInvocados()) {
                            Piezas.ZOMBIE.moverse(zombieBtn, botones, this);
                            Piezas.ZOMBIE.ataque(zombieBtn, botones, this);
                        }
                        
                    }
                    case "lobo" -> {
                        Piezas.HOMBRELOBO.moverse(botones[fila][columna] , botones, this);
                        Piezas.HOMBRELOBO.ataque(botones[fila][columna] , botones, this);
                        
                    }
                }
                System.out.println("Turno de: " + (TurnoBlanco ? "blanco" : "negro") + " - Debe girar la ruleta");
                Piezas.seMovio = true;
                panel.getRuleta().setBotonGirarEnabled(true);
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
               for (var al : tablero[Fila][Columna].getActionListeners()) {
                    tablero[Fila][Columna].removeActionListener(al);
                }
                tablero[Fila][Columna].addActionListener(this); // vuelve al listener base

           }
            blancoUltimo = !blancoUltimo;
                
        }
        
        panel.limpiar();
                        
       
    }
    public static Muerte getMuerteSeleccionada(){
        return muerteSeleccionada;
    }
    
    public static void limpiarMuerteSeleccionada(Muerte muerta) {
        if (muerteSeleccionada == muerta) {
            muerteSeleccionada = null;
        }
    }
    public static JButton[][] getBotones() {
        return botones;
    }
    
}
