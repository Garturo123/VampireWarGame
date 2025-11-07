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
    
    private final int ataque;
    private final int salud;
    private final int escudo;
    private final int movilidad;
    private final int radioAtaque;
    
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
                        tablero[i][j].addActionListener(e -> {
                            tablero[f][c].setIcon(tablero[filaO][columnaO].getIcon());
                            tablero[f][c].putClientProperty("Tipo", original);
                            tablero[f][c].putClientProperty("equipo", equipo);
                            
                            tablero[filaO][columnaO].putClientProperty("equipo", null);
                            tablero[filaO][columnaO].putClientProperty("Tipo", null);
                            tablero[filaO][columnaO].setIcon(null);
                            tabla.limpiarColores(tablero);
                            
                             for (int x = 0; x < 6; x++) {
                                for (int y = 0; y < 6; y++) {
                                    for (var al : tablero[x][y].getActionListeners()) {
                                        tablero[x][y].removeActionListener(al);
                                    }
                                    tablero[x][y].addActionListener(tabla);
                                }
                            }
                            
                        });
                    }
                }
            }
        }
    }
    public void ataque(JButton casilla,  JButton[][] tablero, Tablero tabla) {
        
        fila = (int) casilla.getClientProperty("Fila");
        columna = (int) casilla.getClientProperty("Columna");
         
        for(int i =fila-radioAtaque; i <= fila + radioAtaque ; i++)
        {
            for(int j =columna -radioAtaque; j <= columna + radioAtaque;j++)
            {
                 
                if(i >= 0 && i < 6 && j >= 0 && j < 6  && !(fila == i&&columna==j)){
                    Pieza enemigo = (Pieza) tablero[i][j].getClientProperty("Tipo");
                    String enemigos = (String) tablero[i][j].getClientProperty("equipo");
                    String aliados = (String) tablero[fila][columna].getClientProperty("equipo");
                    if(enemigo!=null && !aliados.equals(enemigos)){
                        tablero[i][j].setBackground(Color.RED);
                        int f = i, c = j;

                        tablero[i][j].addActionListener(e -> {
                            enemigo.RecibirDanio(this.ataque);
                            System.out.println("Escudo restante del enemigo: " + enemigo.getEscudo());
                            System.out.println("Salud restante del enemigo: " + enemigo.getSalud());
                            if (!enemigo.estaViva()) {
                                tablero[f][c].putClientProperty("Tipo", null);
                                tablero[f][c].putClientProperty("equipo", null);        
                                tablero[f][c].setIcon(null);
                                System.out.println("El enemigo ha muerto en (" + f + ", " + c + ")");
                            }
                            
                            tablero[f][c].removeActionListener(tabla);
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
