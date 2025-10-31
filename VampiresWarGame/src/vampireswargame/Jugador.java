package vampireswargame;

/**
 *
 * @author gaat1
 */
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author gaat1
 */
public class Jugador {
  
    private String userName;
    private String password;
    private int Ranking;
    private Calendar fechaIngreso; 
    static ArrayList<Jugador> jugadores = new ArrayList<>();;
    
    public Jugador(String userName, String password){
        
        this.password = password;
        this.userName = userName;
        Ranking = 0;
        fechaIngreso = Calendar.getInstance();
    }
    public String getUserName(){
        return userName;
    }
    
    public boolean buscarJugadorDuplicado(String j, int i){
        if(jugadores.size()<i){
            
            if(jugadores.get(i).userName.equals(j))
                return true;
            
            return buscarJugadorDuplicado(j, i+=1);
        }
        return false;
            
    }
    
    public static void registrarUsuario(JTextField nombreUsuario, JPasswordField password, JFrame frame){
            String nombre = nombreUsuario.getText();
            boolean existe = false;
            for (Jugador j : Jugador.jugadores) {
                if (j.getUserName().equals(nombre)) {
                    existe = true;
                    break;
                }
            }
            
            
            String PassWord = new String(password.getPassword());
            if(existe){
                JOptionPane.showMessageDialog(null, "Ya existe una cuenta con este nombre.");
                return;
            }
            
            if (nombreUsuario.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No ingresastes nombre.");
                return;
            }
            System.out.println(""+nombre);
            System.out.println("PassWord: "+ PassWord);
            if(PassWord.length()!=5){
                JOptionPane.showMessageDialog(null, "La contrasenia tiene que ser de 5 caracteres exactos.");
                return;
            }

            Jugador player = new Jugador(nombre,PassWord);
            MenuPrincipal.JugadorActual   = player ;
            Jugador.jugadores.add(player);
            SwingUtilities.invokeLater(()->{
               MenuPrincipal principal = new MenuPrincipal();
               frame.setVisible(false);
               principal.setVisible(true);
           });
                    
    }
    
}