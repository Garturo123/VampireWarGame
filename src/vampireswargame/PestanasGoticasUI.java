package vampireswargame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/** Apariencia de pestañas con contraste fijo para cualquier sistema. */
public final class PestanasGoticasUI extends BasicTabbedPaneUI {
    private static final Color SELECCIONADA_SUPERIOR = new Color(130, 25, 40);
    private static final Color SELECCIONADA_INFERIOR = new Color(55, 8, 17);
    private static final Color NORMAL = new Color(24, 26, 32);
    private static final Color BORDE = new Color(155, 132, 92);

    @Override
    protected void installDefaults() {
        super.installDefaults();
        tabPane.setFont(new Font("Serif", Font.BOLD, 15));
        tabPane.setBackground(RecursosVisuales.FONDO_GOTICO);
        tabPane.setForeground(RecursosVisuales.TEXTO_MARFIL);
        selectedTabPadInsets = new java.awt.Insets(3, 3, 3, 3);
    }

    @Override
    protected void paintTabBackground(Graphics grafico, int posicion,
            int indice, int x, int y, int ancho, int alto,
            boolean seleccionada) {
        Graphics2D g = (Graphics2D) grafico.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (seleccionada) {
            g.setPaint(new java.awt.GradientPaint(
                    x, y, SELECCIONADA_SUPERIOR,
                    x, y + alto, SELECCIONADA_INFERIOR));
        } else {
            g.setColor(NORMAL);
        }
        g.fillRoundRect(x + 1, y + 1, ancho - 2, alto - 1, 9, 9);
        g.dispose();
    }

    @Override
    protected void paintTabBorder(Graphics grafico, int posicion,
            int indice, int x, int y, int ancho, int alto,
            boolean seleccionada) {
        grafico.setColor(seleccionada ? BORDE : new Color(70, 70, 76));
        grafico.drawRoundRect(x + 1, y + 1, ancho - 3, alto - 2, 9, 9);
    }

    @Override
    protected void paintText(Graphics grafico, int posicion, Font fuente,
            FontMetrics metricas, int indice, String titulo,
            Rectangle rectangulo, boolean seleccionada) {
        grafico.setFont(fuente);
        grafico.setColor(seleccionada
                ? Color.WHITE : new Color(195, 190, 178));
        grafico.drawString(titulo, rectangulo.x,
                rectangulo.y + metricas.getAscent());
    }

    @Override
    protected void paintFocusIndicator(Graphics grafico, int posicion,
            Rectangle[] rectangulos, int indice, Rectangle icono,
            Rectangle texto, boolean seleccionada) {
        // El borde metálico de la pestaña ya comunica el foco con claridad.
    }

    @Override
    protected void paintContentBorder(Graphics grafico, int posicion,
            int indiceSeleccionado) {
        grafico.setColor(BORDE);
        grafico.drawRect(0, calculateTabAreaHeight(posicion,
                runCount, maxTabHeight) - 1,
                tabPane.getWidth() - 1,
                tabPane.getHeight() - calculateTabAreaHeight(
                        posicion, runCount, maxTabHeight));
    }

    @Override
    public void installUI(JComponent componente) {
        super.installUI(componente);
        componente.setOpaque(false);
    }
}
