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
                inicio = mezclar(colorBase, Color.WHITE, 0.28f);
                fin = colorBase.darker();
            }
        }
        g.setPaint(new GradientPaint(0, 0, inicio,
                getWidth(), getHeight(), fin));
        g.fillRect(0, 0, getWidth(), getHeight());
        if (destacado == Destacado.NORMAL) {
            pintarTexturaPiedra(g);
        }
        g.setColor(new Color(255, 255, 255, 80));
        g.drawLine(1, 1, getWidth() - 2, 1);
        g.drawLine(1, 1, 1, getHeight() - 2);
        g.setColor(new Color(25, 27, 33, 90));
        g.drawLine(1, getHeight() - 2, getWidth() - 2, getHeight() - 2);
        g.drawLine(getWidth() - 2, 1, getWidth() - 2, getHeight() - 2);
        g.dispose();
        super.paintComponent(grafico);
    }

    private void pintarTexturaPiedra(Graphics2D g) {
        g.setColor(new Color(48, 45, 48, 25));
        g.drawLine(0, getHeight() / 3, getWidth() / 3, getHeight() / 4);
        g.drawLine(getWidth() * 2 / 3, getHeight(),
                getWidth(), getHeight() * 2 / 3);
        g.setColor(new Color(255, 255, 255, 25));
        g.drawLine(getWidth() / 5, 0,
                getWidth() / 2, getHeight() / 5);
    }

    private Color mezclar(Color base, Color destino, float proporcion) {
        float inversa = 1f - proporcion;
        return new Color(
                Math.round(base.getRed() * inversa
                        + destino.getRed() * proporcion),
                Math.round(base.getGreen() * inversa
                        + destino.getGreen() * proporcion),
                Math.round(base.getBlue() * inversa
                        + destino.getBlue() * proporcion));
    }
}
