package vampireswargame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import static vampireswargame.Piezas.seMovio;

public final class Muerte extends Pieza {
    private Piezas muerte = Piezas.MUERTE;
    private String nombre = "muerte";
    private List<JButton> zombiesInvocados = new ArrayList<>(); // 👈 Lista de zombies de esta muerte

    public Muerte() {
        this.ataque = muerte.getAtaque();
        this.escudo = muerte.getEscudo();
        this.salud = muerte.getSalud();
    }

    @Override
    public String getNombre() {
        return nombre;
    }
    public int getSalud(){
        return salud;
    }
    public int getEscudo(){
        return escudo;
    }
    @Override
    public void Habilidad(JButton invocador, JButton[][] tablero, Tablero tabla) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Pieza tipo2 = (Pieza) tablero[i][j].getClientProperty("Tipo");
                String equipo = invocador.getClientProperty("equipo").toString();

                if (tipo2 == null) {
                    tablero[i][j].setBackground(Color.GREEN);

                    for (var al : tablero[i][j].getActionListeners()) {
                        tablero[i][j].removeActionListener(al);
                    }

                    int f = i, c = j;
                    tablero[i][j].addActionListener(e -> {
                        BotonEscalable nuevoBoton;

                        if (equipo.equals("negro")) {
                            nuevoBoton = new BotonEscalable(
                                new ImageIcon(Tablero.class.getResource("/vampireswargame/imagenes/ZombieNegro.png"))
                            );
                            nuevoBoton.putClientProperty("equipo", "negro");
                        } else {
                            nuevoBoton = new BotonEscalable(
                                new ImageIcon(Tablero.class.getResource("/vampireswargame/imagenes/ZombieBlanco.png"))
                            );
                            nuevoBoton.putClientProperty("equipo", "blanco");
                        }

                        nuevoBoton.putClientProperty("Tipo", new Zombie());
                        nuevoBoton.putClientProperty("duenaMuerte", Muerte.this); 
                        nuevoBoton.putClientProperty("Fila",f);
                        nuevoBoton.putClientProperty("Columna",c);
                        tablero[f][c] = nuevoBoton;

                        zombiesInvocados.add(nuevoBoton); 

                        tabla.remove(tabla.getComponent(f * 6 + c));
                        tabla.add(nuevoBoton, f * 6 + c);

                        tabla.revalidate();
                        tabla.repaint();
                        tabla.limpiarColores(tablero);
                        seMovio = true;
                        opciones.getRuleta().setBotonGirarEnabled(true);
                    });
                }
            }
        }
    }

    @Override
    public void RecibirDanio(int cantidad, boolean penetrante) {
        super.RecibirDanio(cantidad, penetrante);
        
        if (!estaViva()) {
            eliminarZombies();
        }
    }

    private void eliminarZombies() {
        for (JButton zombieBtn : zombiesInvocados) {
            zombieBtn.putClientProperty("Tipo", null);
            zombieBtn.putClientProperty("equipo", null);
            zombieBtn.setIcon(null);
        }
        zombiesInvocados.clear();
    }
    public List<JButton> getZombiesInvocados(){
        return zombiesInvocados;
    }
    public boolean controlaZombie(JButton boton) {
        return zombiesInvocados.contains(boton);
    }
    public void eliminarZombie(JButton zombie) {
        zombiesInvocados.remove(zombie);
    }
}
