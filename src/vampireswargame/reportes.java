package vampireswargame;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class reportes extends JFrame {
    public reportes() {
        setTitle("Reportes");
        setSize(680, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        PanelFondoGotico fondo = new PanelFondoGotico(new BorderLayout());
        fondo.setBorder(javax.swing.BorderFactory.createEmptyBorder(
                14, 14, 14, 14));
        setContentPane(fondo);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Ranking de jugadores", crearRanking());
        pestanas.addTab("Historial de mis últimos juegos", crearHistorial());
        fondo.add(pestanas, BorderLayout.CENTER);

        JButton volver = RecursosVisuales.crearBoton("Volver");
        volver.addActionListener(e -> UiSeguro.ejecutar(this, () -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        }));
        fondo.add(volver, BorderLayout.SOUTH);
        RecursosVisuales.aplicarTema(fondo);
    }

    private JScrollPane crearRanking() {
        String[] columnas = {"Posición", "Usuario", "Puntos"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        RepositorioSistema repositorio = MemoriaSistema.getInstancia();
        Jugador[] jugadores = repositorio.obtenerJugadoresActivosOrdenados();
        for (int i = 0; i < jugadores.length; i++) {
            modelo.addRow(new Object[]{
                i + 1, jugadores[i].getUserName(), jugadores[i].getRanking()
            });
        }
        return new JScrollPane(new JTable(modelo));
    }

    private JScrollPane crearHistorial() {
        RepositorioSistema repositorio = MemoriaSistema.getInstancia();
        HistorialPartida[] historial = repositorio.obtenerHistorial(
                MenuPrincipal.getJugadorActual());
        JTextArea texto = new JTextArea();
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        if (historial.length == 0) {
            texto.setText("Aún no hay partidas finalizadas para este jugador.");
        } else {
            StringBuilder contenido = new StringBuilder();
            for (HistorialPartida partida : historial) {
                contenido.append(partida.getFechaFormateada())
                        .append(" - ")
                        .append(partida.getMensaje())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());
            }
            texto.setText(contenido.toString());
        }
        return new JScrollPane(texto);
    }
}
