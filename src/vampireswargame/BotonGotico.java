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
        g.setPaint(new GradientPaint(0, 0, superior, 0, getHeight(), inferior));
        g.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
        g.setStroke(new BasicStroke(2));
        g.setColor(getModel().isRollover()
                ? new Color(235, 213, 155) : new Color(145, 128, 100));
        g.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 12, 12);
        g.dispose();
        super.paintComponent(grafico);
    }
}
