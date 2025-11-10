package vampireswargame;

import java.awt.BorderLayout;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gaat1
 */
public class reportes extends JFrame {
    public reportes(){
        setTitle("Ranking de Jugadores");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Ordenar la lista de jugadores de mayor a menor
        Collections.sort(Jugador.jugadores, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.getRanking(), j1.getRanking());
            }
        });
        
        // Crear modelo de tabla
        String[] columnas = {"Posición", "Nombre", "Puntos"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        
        // Llenar tabla
        int posicion = 1;
        for (Jugador j : Jugador.jugadores) {
            Object[] fila = {posicion++, j.getUserName(), j.getRanking()};
            modelo.addRow(fila);
        }
        
        JTable tabla = new JTable(modelo);
        tabla.setEnabled(false);
        
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);
        
        JButton volver = new JButton("Volver");
        volver.addActionListener(e -> {
            this.dispose();
            new MenuPrincipal().setVisible(true);
        });
        
        add(volver, BorderLayout.SOUTH);
    }
}
