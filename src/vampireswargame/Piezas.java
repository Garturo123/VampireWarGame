package vampireswargame;

import java.awt.Color;
import javax.swing.JButton;


/**
 *
 * @author gaat1
 */
public enum Piezas {
    ZOMBIE(1,1,0,1,1),
    MUERTE(4,3,1,1,2),
    HOMBRELOBO(5,5,2,2,1),
    VAMPIRO(3,4,5,1,1);
    
    private int fila;
    private int columna;
    public static int vampirosBlancos = 2;
    public static int vampirosNegros = 2;
    public static int lobosBlancos = 2;
    public static int lobosNegros = 2;
    public static int muertesBlancas = 2;
    public static int muertesNegras = 2;
    private final int ataque;
    private final int salud;
    private final int escudo;
    private final int movilidad;
    private final int radioAtaque;
    public static int blancos = 6;
    public static int negros = 6;
    public static boolean seMovio = false;
    private Piezas(int ataque, int salud, int escudo, int movilidad, int radioAtaque){
        this.ataque = ataque;
        this.salud = salud;
        this.escudo = escudo;
        this.movilidad = movilidad;
        this.radioAtaque = radioAtaque;
    }
    public void moverse(JButton casilla,  JButton[][] tablero, Tablero tabla){
        fila = (int) casilla.getClientProperty("Fila");
        columna = (int) casilla.getClientProperty("Columna");
       
        
        String equipo1 = (String) tablero[fila][columna].getClientProperty("equipo");
        Pieza tipo1 = (Pieza) tablero[fila][columna].getClientProperty("Tipo");
        
        for(int i =fila-movilidad; i <= fila + movilidad; i++)
        {
            for(int j = columna-movilidad; j <= columna + movilidad;j++)
            {
                if(i >= 0 && i < 6 && j >= 0 && j < 6 && !(fila == i&&columna==j)){

                   
                    Pieza tipo2 = (Pieza) tablero[i][j].getClientProperty("Tipo");
                    
                    if(tipo2==null){
                        tablero[i][j].setBackground(Color.CYAN);
                        int f = i, c = j;
                        int filaO = fila, columnaO = columna;
                        Pieza original = tipo1;
                        String equipo = equipo1;
                        for (var al : tablero[i][j].getActionListeners()) {
                            tablero[i][j].removeActionListener(al);
                        }
                         Pieza tipoActual = (Pieza) tablero[filaO][columnaO].getClientProperty("Tipo");
                            if(tipoActual == null) return;
                        tablero[i][j].addActionListener(e -> {
                            tablero[f][c].setIcon(tablero[filaO][columnaO].getIcon());
                            tablero[f][c].putClientProperty("Tipo", original);
                            tablero[f][c].putClientProperty("equipo", equipo);
                            
                            tablero[filaO][columnaO].putClientProperty("equipo", null);
                            tablero[filaO][columnaO].putClientProperty("Tipo", null);
                            tablero[filaO][columnaO].setIcon(null);
                            tabla.limpiarColores(tablero);
                            seMovio = true;
                            opciones.getRuleta().setBotonGirarEnabled(true);
                        });
                    }
                }
            }
        }
    }
    public void ataque(JButton casilla,  JButton[][] tablero, Tablero tabla) {
        
        fila = (int) casilla.getClientProperty("Fila");
        columna = (int) casilla.getClientProperty("Columna");
         Pieza sujeto = (Pieza) tablero[fila][columna].getClientProperty("Tipo");
        for(int i =fila-radioAtaque; i <= fila + radioAtaque ; i++)
        {
            for(int j =columna -radioAtaque; j <= columna + radioAtaque;j++)
            {
                 
                if(i >= 0 && i < 6 && j >= 0 && j < 6  && !(fila == i&&columna==j)){
                     
                    Pieza enemigo = (Pieza) tablero[i][j].getClientProperty("Tipo");
                    Pieza yo = (Pieza) tablero[fila][columna].getClientProperty("Tipo");
                    String enemigos = (String) tablero[i][j].getClientProperty("equipo");
                    String aliados = (String) tablero[fila][columna].getClientProperty("equipo");
                    
                    if(enemigo!=null &&  !aliados.equals(enemigos) ){
                        tablero[i][j].setBackground(Color.RED);
                        int f = i, c = j;

                        tablero[i][j].addActionListener(e -> {
                            int saludAntes = enemigo.getSalud();
                            int escudoAntes = enemigo.getEscudo();
                            int danioRecibido =0;
                            if (sujeto instanceof Vampiro && sujeto.chupar){
                                sujeto.RecibirDanio(1, true);
                                enemigo.RecibirDanio(1, false);
                                danioRecibido =1;
                            }
                            else if(f<fila-1 || f>fila+1 || c<columna-1 || c>columna+1){
                                enemigo.RecibirDanio(this.ataque/2, true);
                                danioRecibido =(this.ataque/2);
                            }else{
                                enemigo.RecibirDanio(this.ataque, false);
                                danioRecibido =(this.ataque);
                            }
                            
                            opciones.setMensaje(
                                "<html><b>  " + yo.getNombre() + " (" + aliados + ")</b> ataco a <b>" +
                                enemigo.getNombre() + " (" + enemigos + ")</b><br>" +
                                "️   Danio total: " + danioRecibido + "<br>" +
                                "   Danio al escudo: " + (-1*(enemigo.getEscudo()-escudoAntes)) + "<br>" +
                                "   Danio a la vida: " + (-1*(enemigo.getSalud()-saludAntes)) + "<br>" +
                                "   Vida restante del enemigo: " + enemigo.getSalud() + "<br>" +
                                "   Escudo restante: " + enemigo.getEscudo() + "</html>"
                            );
                            
                            
                            
                            if (!enemigo.estaViva()) {
                                JButton boton = tablero[f][c];
                                String muerto = boton.getClientProperty("equipo").toString();
                                if(muerto.equals("blanco")){
                                    blancos--;
                                }
                                else{
                                    negros--;  
                                }
                                if (enemigo instanceof Muerte) {
                                    
                                    Tablero.limpiarMuerteSeleccionada((Muerte) enemigo);
                                }else if(enemigo instanceof Zombie){
                                    Zombie z = (Zombie) enemigo;
                                    Muerte dueña = (Muerte) boton.getClientProperty("duenaMuerte");
                                    dueña.eliminarZombie(boton);
                                }
                                
                                boton.putClientProperty("Tipo", null);
                                boton.putClientProperty("equipo", null);        
                                boton.setIcon(null);
                                tabla.verificarTipoEliminado(enemigos, enemigo.getNombre());
                                

                                System.out.println("El enemigo ha muerto en (" + f + ", " + c + ")");
                                
                            }
                            tabla.limpiarColores(tablero);
                            tablero[f][c].removeActionListener(tabla);
                            
                            seMovio = true;
                            opciones.getRuleta().setBotonGirarEnabled(true);
                        });
                        
                    }
                }
            }
        }
    }
    public int getAtaque(){
        return ataque;
    }
    public int getSalud(){
       return salud; 
    }
    public int getEscudo(){
        return escudo;
    }
}
