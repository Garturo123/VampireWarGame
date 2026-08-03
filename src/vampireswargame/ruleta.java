package vampireswargame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class ruleta extends JPanel implements ActionListener {
    public interface RuletaListener {
        void onRuletaFinalizada(String resultado);
    }

    private static final int SECTORES = 6;
    private final JButton botonGirar = RecursosVisuales.crearBoton("Girar");
    private final JLabel resultadoLabel =
            new JLabel("Presiona para girar", SwingConstants.CENTER);
    private final Random random = new Random();
    private final Timer timer = new Timer(20, this);
    private final ImageIcon[] imagenes = new ImageIcon[SECTORES];
    private final String[] nombres = {
        "lobo", "vampiro", "muerte", "lobo", "vampiro", "muerte"
    };
    private RuletaListener listener;
    private double anguloActual;
    private double velocidad;
    private String resultado = "";

    public ruleta() {
        setPreferredSize(new Dimension(440, 500));
        setBackground(RecursosVisuales.FONDO_GOTICO);
        setOpaque(true);
        setLayout(new BorderLayout());
        botonGirar.setFont(new Font("Arial", Font.BOLD, 18));
        resultadoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultadoLabel.setForeground(RecursosVisuales.TEXTO_MARFIL);
        add(resultadoLabel, BorderLayout.NORTH);
        add(botonGirar, BorderLayout.SOUTH);
        cargarImagenes();
        botonGirar.addActionListener(e -> UiSeguro.ejecutar(this, this::girar));
    }

    private void cargarImagenes() {
        ImageIcon lobo = escalar("/vampireswargame/imagenes/LoboNegro.png");
        ImageIcon vampiro = escalar("/vampireswargame/imagenes/VampiroNegro.png");
        ImageIcon muerte = escalar("/vampireswargame/imagenes/MuerteNegro.png");
        ImageIcon[] base = {vampiro, lobo, muerte, vampiro, lobo, muerte};
        System.arraycopy(base, 0, imagenes, 0, SECTORES);
    }

    private ImageIcon escalar(String ruta) {
        try {
            URL recurso = getClass().getResource(ruta);
            if (recurso == null) {
                return new ImageIcon();
            }
            Image imagen = new ImageIcon(recurso).getImage()
                    .getScaledInstance(72, 72, Image.SCALE_SMOOTH);
            return new ImageIcon(imagen);
        } catch (RuntimeException excepcion) {
            return new ImageIcon();
        }
    }

    private void girar() {
        botonGirar.setEnabled(false);
        resultado = "";
        velocidad = 30 + random.nextDouble() * 10;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        anguloActual += velocidad;
        velocidad *= 0.97;
        if (velocidad < 0.6) {
            timer.stop();
            int indice = obtenerSectorGanador();
            resultado = nombres[indice];
            resultadoLabel.setText("Resultado: " + resultado);
            if (listener != null) {
                listener.onRuletaFinalizada(resultado);
            }
        }
        repaint();
    }

    private int obtenerSectorGanador() {
        double anguloPorSector = 360.0 / SECTORES;
        double normalizado = (anguloActual % 360 + 360) % 360;
        double ajustado = (normalizado + anguloPorSector / 2) % 360;
        int indice = (int) (ajustado / anguloPorSector);
        return (SECTORES - indice) % SECTORES;
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);
        int tamano = Math.min(360, Math.min(getWidth() - 20, getHeight() - 100));
        int radio = tamano / 2;
        Graphics2D g = (Graphics2D) grafico.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(getWidth() / 2, getHeight() / 2);
        g.rotate(Math.toRadians(anguloActual));
        double anguloSector = 360.0 / SECTORES;
        for (int i = 0; i < SECTORES; i++) {
            g.setColor(i % 2 == 0 ? Color.WHITE
                    : new Color(205, 210, 218));
            int inicio = (int) Math.round(i * anguloSector);
            g.fillArc(-radio, -radio, tamano, tamano, inicio,
                    (int) Math.ceil(anguloSector));
            double medio = Math.toRadians(inicio + anguloSector / 2);
            ImageIcon icono = imagenes[i];
            int x = (int) (radio * 0.55 * Math.cos(medio));
            int y = (int) (radio * 0.55 * Math.sin(-medio));
            g.drawImage(icono.getImage(), x - 36, y - 36, 72, 72, this);
        }
        g.setColor(new Color(35, 38, 45));
        g.setStroke(new BasicStroke(3));
        g.drawOval(-radio, -radio, tamano, tamano);
        g.dispose();

        grafico.setColor(new Color(170, 25, 40));
        int centro = getWidth() / 2;
        int superior = (getHeight() - tamano) / 2;
        int[] x = {centro - 10, centro + 10, centro};
        int[] y = {superior - 18, superior - 18, superior};
        grafico.fillPolygon(x, y, 3);
    }

    public void setRuletaListener(RuletaListener listener) {
        this.listener = listener;
    }

    public String getResultado() {
        return resultado;
    }

    public void prepararGiro(String mensaje) {
        resultado = "";
        resultadoLabel.setText(mensaje);
        botonGirar.setEnabled(true);
    }

    public void setBotonGirarEnabled(boolean habilitado) {
        botonGirar.setEnabled(habilitado);
    }
}
