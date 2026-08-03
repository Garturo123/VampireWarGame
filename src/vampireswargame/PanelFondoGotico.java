package vampireswargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/** Panel que adapta el fondo gótico al tamaño de cada ventana. */
public class PanelFondoGotico extends JPanel {
    private final Image fondo;

    public PanelFondoGotico() {
        this(null);
    }

    public PanelFondoGotico(LayoutManager layout) {
        super(layout);
        fondo = cargarFondo();
        setOpaque(true);
    }

    private Image cargarFondo() {
        try {
            URL recurso = getClass().getResource(
                    "/vampireswargame/imagenes/FondoGotico.png");
            return recurso == null ? null : new ImageIcon(recurso).getImage();
        } catch (RuntimeException excepcion) {
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);
        Graphics2D g = (Graphics2D) grafico.create();
        if (fondo != null && getWidth() > 0 && getHeight() > 0) {
            double escala = Math.max(
                    getWidth() / (double) fondo.getWidth(this),
                    getHeight() / (double) fondo.getHeight(this));
            int ancho = (int) Math.ceil(fondo.getWidth(this) * escala);
            int alto = (int) Math.ceil(fondo.getHeight(this) * escala);
            int x = (getWidth() - ancho) / 2;
            int y = (getHeight() - alto) / 2;
            g.drawImage(fondo, x, y, ancho, alto, this);
        } else {
            g.setColor(RecursosVisuales.FONDO_GOTICO);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.setColor(new Color(0, 0, 0, 105));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.dispose();
    }
}
