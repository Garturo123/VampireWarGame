package vampireswargame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class ruleta extends JPanel implements ActionListener {
    private final int numSectores = 6;
    private double anguloActual = 0;
    private double velocidad = 0;
    private Timer timer;
    private JButton botonGirar;
    private JLabel resultadoLabel;
    private Random random;
    private ImageIcon[] imagenes;
    private String[] nombres;
    private String resultado = "";
    private int intentosExtra = 0;

    public ruleta() {
        setPreferredSize(new Dimension(500, 520));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        
        botonGirar = new JButton("Girar");
        botonGirar.setFont(new Font("Arial", Font.BOLD, 18));

        resultadoLabel = new JLabel("Presiona para girar", SwingConstants.CENTER);
        resultadoLabel.setFont(new Font("Arial", Font.BOLD, 18));

        add(resultadoLabel, BorderLayout.NORTH);
        add(botonGirar, BorderLayout.SOUTH);

        random = new Random();
        cargarImagenes();

        botonGirar.addActionListener(e -> girarRuleta());
        timer = new Timer(20, this);
    }
    
    public interface RuletaListener {
        void onRuletaFinalizada(String resultado);
    }

    private RuletaListener listenerTurno;

    public void setRuletaListener(RuletaListener listener) {
        this.listenerTurno = listener;
    }
    
    private void cargarImagenes() {
        imagenes = new ImageIcon[numSectores];
        nombres = new String[numSectores];
        
        ImageIcon img0 = escalar("/vampireswargame/imagenes/VampiroNegro.png");
        ImageIcon img1 = escalar("/vampireswargame/imagenes/LoboNegro.png");
        ImageIcon img2 = escalar("/vampireswargame/imagenes/MuerteNegro.png");
        imagenes[0] = img0; nombres[0] = "lobo";
        imagenes[1] = img1; nombres[1] = "vampiro";
        imagenes[2] = img2; nombres[2] = "muerte";
        imagenes[3] = img0; nombres[3] = "lobo";
        imagenes[4] = img1; nombres[4] = "vampiro";
        imagenes[5] = img2; nombres[5] = "muerte";
        
    }
    private ImageIcon escalar(String ruta) {
        ImageIcon icon = new ImageIcon(getClass().getResource(ruta));
        Image imgEscalada = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(imgEscalada);
    }
    private void girarRuleta() {
        botonGirar.setEnabled(false);
        velocidad = 30 + random.nextDouble() * 10;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        anguloActual += velocidad;
        velocidad *= 0.97;
        
        if (velocidad < 0.6) {
            timer.stop();
            int indice = obtenerSectorGanador();
            resultado = nombres[indice];

            String equipoActual = Tablero.TurnoBlanco ? "blanco" : "negro";
            int tiposPerdidos = Tablero.contarTiposPerdidos(equipoActual);
            intentosExtra = tiposPerdidos == 1 ? 2 : tiposPerdidos == 2 ? 4 : 0;

            // 🔁 Intentos visibles (permiten re-girar)
            System.out.println("DEBUG → resultado=" + resultado + " | equipo=" + equipoActual);
            System.out.println("DEBUG → hayPiezaDisponible=" + Tablero.hayPiezaDisponible(resultado, equipoActual));
            while (!Tablero.hayPiezaDisponible(resultado, equipoActual) && intentosExtra > 0) {
                intentosExtra--;
                JOptionPane.showMessageDialog(null, 
                    "El equipo " + equipoActual + 
                    " no tiene piezas de tipo " + resultado + 
                    ". Intento extra disponible (" + (intentosExtra + 1) + " restantes)");

                // 🌀 Forzar un nuevo giro visible
                botonGirar.setEnabled(true);
                resultadoLabel.setText("¡Gira de nuevo!");
                resultado = ""; // limpiar para que el tablero no pueda mover nada aún
                repaint();
                return; // salir para que el jugador realmente gire otra vez
            }

            if (!Tablero.hayPiezaDisponible(resultado, equipoActual)) {
                if (intentosExtra > 0) {
                    intentosExtra--; // consume un giro extra
                    JOptionPane.showMessageDialog(null, 
                        "El equipo " + equipoActual + " no tiene piezas de tipo " + resultado +
                        ". Se usará un intento extra (" + intentosExtra + " restantes)");
                    botonGirar.setEnabled(true);
                    resultado = "";
                    resultadoLabel.setText("¡Gira de nuevo!");
                    repaint();
                    return;
                } else {
                    // No hay intentos extra → pierdes turno
                    JOptionPane.showMessageDialog(null, 
                        "No hay piezas disponibles para mover. Pierdes el turno.");
                    Tablero.TurnoBlanco = !Tablero.TurnoBlanco;
                    intentosExtra = 0; // 🔹 reinicia los giros extra aquí
                    botonGirar.setEnabled(true);
                    resultado = "";
                    resultadoLabel.setText("Presiona para girar");
                    repaint();
                    return;
                }
            }

            // Resultado válido → notificar al Tablero
            resultadoLabel.setText("Resultado: " + resultado);
            if (listenerTurno != null)
                listenerTurno.onRuletaFinalizada(resultado);
        }
        
        repaint();
        
    }
    
    public String getResultado(){
        return resultado;
    }

    private int obtenerSectorGanador() {
        double anguloPorSector = 360.0 / numSectores;

        // Normaliza el ángulo entre 0 y 360
        double anguloNormalizado = (anguloActual % 360 + 360) % 360;

        // Centra el ángulo en el medio del sector
        double anguloAjustado = (anguloNormalizado + anguloPorSector / 2) % 360;

        // Calcula el índice del sector (sentido horario)
        int indice = (int)(anguloAjustado / anguloPorSector);

        // Invertimos para que coincida con la dirección de giro
        indice = (numSectores - indice) % numSectores;

        return indice;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int size = 380;
        int radio = size / 2;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.rotate(Math.toRadians(anguloActual));

        double anguloPorSector = 360.0 / 6;

        for (int i = 0; i < 6; i++) {
            double anguloInicio = i * anguloPorSector;
            double anguloMedio = Math.toRadians(anguloInicio + anguloPorSector / 2);

            // Fondo del sector
            if (i % 2 == 0)
                g2d.setColor(new Color(240, 240, 240));
            else
                g2d.setColor(new Color(200, 200, 200));

            g2d.fillArc(-radio, -radio, size, size, (int) Math.round(anguloInicio), (int) Math.ceil(anguloPorSector));

            // Imagen dentro del sector
            if (imagenes[i] != null) {
                Image img = imagenes[i].getImage();
                int imgW = imagenes[i].getIconWidth();
                int imgH = imagenes[i].getIconHeight();

                int radioImagen = radio / 2;
                int x = (int) (radioImagen * Math.cos(anguloMedio));
                int y = (int) (radioImagen * Math.sin(-anguloMedio));
                g2d.drawImage(img, x - imgW / 2, y - imgH / 2, imgW, imgH, this);
            }
        }

        // Borde
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(-radio, -radio, size, size);

        g2d.dispose();

        // Indicador superior
        g.setColor(Color.RED);
        int[] px = {getWidth() / 2 - 10, getWidth() / 2 + 10, getWidth() / 2};
        int[] py = {(getHeight() - size) / 2 - 20, (getHeight() - size) / 2 - 20, (getHeight() - size) / 2};
        g.fillPolygon(px, py, 3);
    }
    public void setBotonGirarEnabled(boolean enabled) {
        botonGirar.setEnabled(enabled);
    }
    public void setIntentosExtra(int intentos) {
        this.intentosExtra = intentos;
    }

    public int getIntentosExtra() {
        return intentosExtra;
    }
}
