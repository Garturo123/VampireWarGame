package vampireswargame;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/**
 * Casilla capaz de mostrar visualmente las acciones disponibles.
 */
public final class CasillaTablero extends JButton {
    public enum Destacado {
        NORMAL,
        MOVIMIENTO,
        ATAQUE,
        INVOCACION,
        MOVIMIENTO_INVOCACION
    }

    private Color colorBase = Color.WHITE;
    private Destacado destacado = Destacado.NORMAL;

    public CasillaTablero() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    public void setColorBase(Color colorBase) {
        this.colorBase = colorBase;
        repaint();
    }

    public void setDestacado(Destacado destacado) {
        this.destacado = destacado == null ? Destacado.NORMAL : destacado;
        repaint();
    }

    public Destacado getDestacado() {
        return destacado;
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        Graphics2D g = (Graphics2D) grafico.create();
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        Color inicio;
        Color fin;
        switch (destacado) {
            case MOVIMIENTO -> {
                inicio = new Color(220, 252, 255);
                fin = new Color(64, 190, 220);
            }
            case ATAQUE -> {
                inicio = new Color(255, 225, 225);
                fin = new Color(220, 55, 70);
            }
            case INVOCACION -> {
                inicio = new Color(255, 255, 225);
                fin = new Color(245, 211, 92);
            }
            case MOVIMIENTO_INVOCACION -> {
                inicio = new Color(145, 225, 240);
                fin = new Color(255, 239, 135);
            }
            default -> {
                inicio = colorBase.brighter();
                fin = colorBase;
            }
        }
        g.setPaint(new GradientPaint(0, 0, inicio,
                getWidth(), getHeight(), fin));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.dispose();
        super.paintComponent(grafico);
    }
}
