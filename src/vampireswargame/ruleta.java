package vampireswargame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
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
    private double velocidadObjetivo;
    private int ciclosHastaVariacion;
    private String resultado = "";

    public ruleta() {
        setPreferredSize(new Dimension(420, 350));
        setMaximumSize(new Dimension(420, 350));
        setOpaque(false);
        setLayout(new BorderLayout());
        botonGirar.setFont(new Font("Serif", Font.BOLD, 18));
        resultadoLabel.setFont(new Font("Serif", Font.BOLD, 18));
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
        if (timer.isRunning()) {
            detenerGiro();
            return;
        }
        resultado = "";
        anguloActual += random.nextDouble() * 55;
        velocidad = 16 + random.nextDouble() * 8;
        prepararVariacionVelocidad();
        resultadoLabel.setText("Ruleta girando... presiona Detener");
        botonGirar.setText("Detener");
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        velocidad += (velocidadObjetivo - velocidad) * 0.08;
        anguloActual += velocidad;
        ciclosHastaVariacion--;
        if (ciclosHastaVariacion <= 0) {
            prepararVariacionVelocidad();
        }
        repaint();
    }

    private void prepararVariacionVelocidad() {
        velocidadObjetivo = 15 + random.nextDouble() * 11;
        ciclosHastaVariacion = 8 + random.nextInt(18);
    }

    private void detenerGiro() {
        timer.stop();
        botonGirar.setText("Girar");
        int indice = obtenerSectorGanador();
        resultado = nombres[indice];
        resultadoLabel.setText("Resultado: " + resultado);
        if (listener != null) {
            listener.onRuletaFinalizada(resultado);
        }
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

        g.setColor(new Color(7, 8, 12, 225));
        g.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 22, 22);
        g.setStroke(new BasicStroke(2));
        g.setColor(new Color(112, 24, 39));
        g.drawRoundRect(4, 4, getWidth() - 9, getHeight() - 9, 22, 22);

        g.translate(getWidth() / 2, getHeight() / 2);
        g.rotate(Math.toRadians(anguloActual));
        double anguloSector = 360.0 / SECTORES;
        for (int i = 0; i < SECTORES; i++) {
            Color superior = i % 2 == 0
                    ? new Color(229, 226, 216)
                    : new Color(178, 182, 190);
            Color inferior = i % 2 == 0
                    ? new Color(169, 169, 171)
                    : new Color(116, 121, 132);
            g.setPaint(new GradientPaint(-radio, -radio, superior,
                    radio, radio, inferior));
            int inicio = (int) Math.round(i * anguloSector);
            g.fillArc(-radio, -radio, tamano, tamano, inicio,
                    (int) Math.ceil(anguloSector));
            g.setColor(new Color(82, 18, 31));
            g.setStroke(new BasicStroke(2));
            g.drawArc(-radio, -radio, tamano, tamano, inicio,
                    (int) Math.ceil(anguloSector));
            double medio = Math.toRadians(inicio + anguloSector / 2);
            ImageIcon icono = imagenes[i];
            int x = (int) (radio * 0.55 * Math.cos(medio));
            int y = (int) (radio * 0.55 * Math.sin(-medio));
            g.drawImage(icono.getImage(), x - 36, y - 36, 72, 72, this);
        }
        g.setColor(new Color(44, 8, 17));
        g.setStroke(new BasicStroke(7));
        g.drawOval(-radio, -radio, tamano, tamano);
        g.setColor(new Color(166, 143, 87));
        g.setStroke(new BasicStroke(2));
        g.drawOval(-radio + 5, -radio + 5, tamano - 10, tamano - 10);
        g.setPaint(new GradientPaint(-16, -16, new Color(145, 33, 49),
                16, 16, new Color(35, 6, 13)));
        g.fillOval(-17, -17, 34, 34);
        g.setColor(new Color(196, 172, 108));
        g.drawOval(-17, -17, 34, 34);
        g.dispose();

        Graphics2D marcador = (Graphics2D) grafico.create();
        marcador.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int centro = getWidth() / 2;
        int superior = (getHeight() - tamano) / 2;
        int[] x = {centro - 13, centro + 13, centro};
        int[] y = {superior - 20, superior - 20, superior + 3};
        marcador.setColor(new Color(116, 17, 32));
        marcador.fillPolygon(x, y, 3);
        marcador.setColor(new Color(211, 184, 112));
        marcador.setStroke(new BasicStroke(2));
        marcador.drawPolygon(x, y, 3);
        marcador.dispose();
    }

    public void setRuletaListener(RuletaListener listener) {
        this.listener = listener;
    }

    public String getResultado() {
        return resultado;
    }

    public void prepararGiro(String mensaje) {
        timer.stop();
        resultado = "";
        resultadoLabel.setText(mensaje);
        botonGirar.setText("Girar");
        botonGirar.setEnabled(true);
    }

    public void setBotonGirarEnabled(boolean habilitado) {
        botonGirar.setEnabled(habilitado);
    }

    public void cancelarGiro() {
        timer.stop();
        resultado = "";
        botonGirar.setText("Girar");
    }
}
