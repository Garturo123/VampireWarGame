package vampireswargame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/** Botón común con acabado carmesí y metálico. */
public final class BotonGotico extends JButton {
    public BotonGotico(String texto) {
        super(texto);
        setForeground(RecursosVisuales.TEXTO_MARFIL);
        setFont(new Font("Serif", Font.BOLD, 16));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setPreferredSize(new Dimension(230, 42));
        setMaximumSize(new Dimension(280, 42));
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        Graphics2D g = (Graphics2D) grafico.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Color superior;
        Color inferior;
        if (!isEnabled()) {
            superior = new Color(65, 65, 70);
            inferior = new Color(28, 28, 32);
        } else if (getModel().isPressed()) {
            superior = new Color(70, 8, 16);
            inferior = new Color(125, 18, 30);
        } else if (getModel().isRollover()) {
            superior = new Color(150, 30, 42);
            inferior = new Color(55, 8, 16);
        } else {
            superior = new Color(105, 18, 30);
            inferior = new Color(28, 8, 14);
        }
        g.setColor(new Color(0, 0, 0, 115));
        g.fillRoundRect(4, 5, getWidth() - 8, getHeight() - 7, 10, 10);
        g.setPaint(new GradientPaint(0, 2, superior,
                0, getHeight() - 3, inferior));
        g.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 6, 10, 10);
        g.setStroke(new BasicStroke(2));
        g.setColor(getModel().isRollover()
                ? new Color(235, 213, 155) : new Color(145, 128, 100));
        g.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 7, 10, 10);
        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(235, 218, 172, 80));
        g.drawRoundRect(6, 6, getWidth() - 13, getHeight() - 15, 7, 7);
        pintarAdorno(g, 10, getHeight() / 2 - 2);
        pintarAdorno(g, getWidth() - 14, getHeight() / 2 - 2);
        g.dispose();
        super.paintComponent(grafico);
    }

    private void pintarAdorno(Graphics2D g, int x, int y) {
        int[] xs = {x, x + 4, x + 8, x + 4};
        int[] ys = {y + 4, y, y + 4, y + 8};
        g.setColor(new Color(194, 168, 102));
        g.fillPolygon(xs, ys, 4);
        g.setColor(new Color(62, 25, 28));
        g.drawPolygon(xs, ys, 4);
    }
}
