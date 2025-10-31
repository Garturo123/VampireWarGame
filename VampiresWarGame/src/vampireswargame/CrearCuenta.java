package vampireswargame;

/**
 *
 * @author gaat1
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author gaat1
// */
public class CrearCuenta extends JFrame{
    public CrearCuenta(){
        setTitle("Menu de Inicio");
        setSize(300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JTextField nombreUsuario = new JTextField();
        JPasswordField password = new JPasswordField();
        //JButton Ingresar = new JButton("Ingresar");
        JButton Cancelar = new JButton("Cancelar");
        
        
       
        Cancelar.addActionListener(e -> {
            SwingUtilities.invokeLater(()->{
                MenuInicio Inicio = new MenuInicio ();
                this.setVisible(false);
               Inicio.setVisible(true);
               
            });
        });
//        Ingresar.addActionListener(e -> {
//            
//            Jugador.registrarUsuario(nombreUsuario,password,this);
//           
//        });
            
         panel.add(new JLabel("Nombre de usuario: "));
        panel.add(nombreUsuario);
        panel.add(new JLabel("Contrasenia: "));
        panel.add(password);
        //panel.add(Ingresar);
        panel.add(Cancelar);
        add(panel,BorderLayout.CENTER);
    }
    
}
