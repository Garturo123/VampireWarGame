package vampireswargame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Tablero extends JPanel {
    private static final int TAMANO = 6;
    private static final Border BORDE_NORMAL =
            BorderFactory.createLineBorder(Color.GRAY);
    private static final Border BORDE_SELECCION =
            BorderFactory.createLineBorder(Color.YELLOW, 4);
    private static final Border BORDE_GRUPO =
            BorderFactory.createLineBorder(new Color(255, 170, 35), 3);
    private static final Border BORDE_RULETA =
            BorderFactory.createLineBorder(new Color(76, 205, 230), 3);
    private static final long INTERVALO_DOBLE_CLIC_MS = 500;

    private final CasillaTablero[][] botones =
            new CasillaTablero[TAMANO][TAMANO];
    private final Pieza[][] piezas = new Pieza[TAMANO][TAMANO];
    private final opciones panel;
    private final JFrame ventana;
    private final Jugador jugadorBlanco;
    private final Jugador jugadorNegro;
    private final Pieza[] capturasBlancas = new Pieza[100];
    private final Pieza[] capturasNegras = new Pieza[100];
    private int cantidadCapturasBlancas;
    private int cantidadCapturasNegras;
    private boolean turnoBlanco = true;
    private boolean partidaFinalizada;
    private String tipoAutorizado = "";
    private int girosUtilizados;
    private int filaOrigen = -1;
    private int columnaOrigen = -1;
    private int ultimaFilaClic = -1;
    private int ultimaColumnaClic = -1;
    private long instanteUltimoClic;
    private Muerte muerteActiva;

    public Tablero(opciones panel, JFrame ventana, Jugador jugadorBlanco,
            Jugador jugadorNegro) {
        this.panel = panel;
        this.ventana = ventana;
        this.jugadorBlanco = jugadorBlanco;
        this.jugadorNegro = jugadorNegro;
        panel.configurarJugadores(jugadorBlanco, jugadorNegro);
        panel.actualizarCapturas(capturasBlancas, 0, capturasNegras, 0);
        setLayout(new GridLayout(TAMANO, TAMANO));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(66, 9, 20), 4),
                BorderFactory.createLineBorder(new Color(122, 111, 91), 2)));
        construirCasillas();
        colocarPiezasIniciales();
        panel.getRuleta().setRuletaListener(resultado ->
                UiSeguro.ejecutar(this, () -> procesarRuleta(resultado)));
        panel.setAccionRetiro(this::confirmarRetiro);
        prepararTurno();
    }

    private void construirCasillas() {
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                CasillaTablero boton = new CasillaTablero();
                boton.setBorder(BORDE_NORMAL);
                int f = fila;
                int c = columna;
                boton.addActionListener(e ->
                        UiSeguro.ejecutar(this, () -> seleccionarCasilla(f, c)));
                botones[fila][columna] = boton;
                add(boton);
            }
        }
        actualizarTablero();
    }

    private void colocarPiezasIniciales() {
        for (int columna = 0; columna < TAMANO; columna++) {
            piezas[0][columna] = crearPiezaInicial(columna, "negro");
            piezas[5][columna] = crearPiezaInicial(columna, "blanco");
        }
        actualizarTablero();
    }

    private Pieza crearPiezaInicial(int columna, String equipo) {
        Pieza pieza;
        switch (columna) {
            case 0, 5 -> pieza = new Lobo();
            case 1, 4 -> pieza = new Vampiro();
            case 2, 3 -> pieza = new Muerte();
            default -> throw new IllegalArgumentException("Columna inválida.");
        }
        pieza.setEquipo(equipo);
        return pieza;
    }

    private void procesarRuleta(String resultado) {
        if (partidaFinalizada) {
            return;
        }
        girosUtilizados++;
        String equipo = equipoActual();
        if (hayPiezaDisponible(resultado, equipo)) {
            tipoAutorizado = resultado;
            marcarPiezasAutorizadas();
            panel.getRuleta().setBotonGirarEnabled(false);
            panel.setMensaje("Turno de <b>" + jugadorActual().getUserName()
                    + "</b> (" + equipo + "). Seleccione una pieza "
                    + resultado + " marcada con borde celeste.");
            return;
        }

        int maximo = girosPermitidos(equipo);
        if (girosUtilizados < maximo) {
            panel.getRuleta().prepararGiro(
                    "No quedan " + resultado + ". Gira otra vez ("
                    + (maximo - girosUtilizados) + " intento(s)).");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No quedan piezas de tipo " + resultado
                    + " y se agotaron los giros. Se pierde el turno.");
            cambiarTurno();
        }
    }

    private void seleccionarCasilla(int fila, int columna) {
        if (partidaFinalizada) {
            return;
        }
        if (tipoAutorizado.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Primero debe girar la ruleta.");
            return;
        }

        long instanteActual = System.currentTimeMillis();
        boolean esDobleClic = fila == ultimaFilaClic
                && columna == ultimaColumnaClic
                && instanteActual - instanteUltimoClic
                <= INTERVALO_DOBLE_CLIC_MS;
        ultimaFilaClic = fila;
        ultimaColumnaClic = columna;
        instanteUltimoClic = instanteActual;

        if (esDobleClic && filaOrigen == fila && columnaOrigen == columna) {
            limpiarSeleccion();
            panel.mostrarInformacionPieza(null);
            panel.setMensaje("Pieza deseleccionada. Elija otra pieza "
                    + tipoAutorizado + " marcada con borde celeste.");
            return;
        }

        if (filaOrigen < 0) {
            seleccionarOrigen(fila, columna);
        } else if (filaOrigen == fila && columnaOrigen == columna) {
            panel.setMensaje("La pieza continúa seleccionada. Haga doble clic "
                    + "sobre ella para deseleccionarla.");
        } else {
            seleccionarDestino(fila, columna);
        }
    }

    private void seleccionarOrigen(int fila, int columna) {
        Pieza pieza = piezas[fila][columna];
        if (pieza == null) {
            JOptionPane.showMessageDialog(this, "La casilla está vacía.");
            return;
        }
        if (!equipoActual().equals(pieza.getEquipo())) {
            JOptionPane.showMessageDialog(this,
                    "La pieza pertenece al oponente.");
            return;
        }
        if (!tipoAutorizado.equals(pieza.getNombre())) {
            JOptionPane.showMessageDialog(this,
                    "La ruleta permite mover una pieza " + tipoAutorizado + ".");
            return;
        }
        filaOrigen = fila;
        columnaOrigen = columna;
        panel.mostrarInformacionPieza(pieza);
        if (pieza instanceof Muerte muerte) {
            muerteActiva = muerte;
            panel.setMensaje("Muerte y Zombies vinculados seleccionados. "
                    + "Elija una casilla para mover o invocar. Para ordenar "
                    + "un ataque mediante Zombie, seleccione una pieza "
                    + "enemiga adyacente a uno de ellos.");
        } else {
            panel.setMensaje("Pieza seleccionada. Elija una casilla celeste "
                    + "para mover o una roja para atacar.");
        }
        mostrarDestinosDisponibles();
    }

    private void seleccionarDestino(int fila, int columna) {
        Pieza atacante = piezas[filaOrigen][columnaOrigen];
        Pieza destino = piezas[fila][columna];
        if (atacante == null) {
            limpiarSeleccion();
            return;
        }
        if (muerteActiva != null && esIntegranteMuerteActiva(destino)) {
            seleccionarIntegranteMuerte(fila, columna);
            return;
        }
        if (filaOrigen == fila && columnaOrigen == columna) {
            mostrarDestinosDisponibles();
            return;
        }
        if (destino != null
                && equipoActual().equals(destino.getEquipo())
                && tipoAutorizado.equals(destino.getNombre())) {
            cambiarPiezaSeleccionada(fila, columna);
            return;
        }
        if (destino == null) {
            procesarCasillaVacia(atacante, fila, columna);
        } else if (atacante.getEquipo().equals(destino.getEquipo())) {
            JOptionPane.showMessageDialog(this,
                    "No puede moverse ni atacar una pieza propia.");
        } else {
            procesarAtaque(atacante, destino, fila, columna);
        }
    }

    private void cambiarPiezaSeleccionada(int fila, int columna) {
        limpiarSeleccion();
        seleccionarOrigen(fila, columna);
    }

    private boolean esIntegranteMuerteActiva(Pieza pieza) {
        if (pieza == null || muerteActiva == null) {
            return false;
        }
        if (pieza == muerteActiva) {
            return true;
        }
        return pieza instanceof Zombie zombie
                && muerteActiva.getEquipo().equals(zombie.getEquipo());
    }

    private void seleccionarIntegranteMuerte(int fila, int columna) {
        Pieza seleccionada = piezas[fila][columna];
        panel.mostrarInformacionPieza(seleccionada);
        if (seleccionada instanceof Zombie) {
            panel.setMensaje("El Zombie no puede desplazarse por sí mismo. "
                    + "La Muerte continúa seleccionada; elija una pieza "
                    + "enemiga adyacente al Zombie para ordenarle atacar.");
        } else {
            panel.setMensaje("Muerte seleccionada. Las casillas amarillas "
                    + "permiten invocar y las celestes permiten moverse.");
        }
        mostrarDestinosDisponibles();
    }

    private void mostrarDestinosDisponibles() {
        limpiarIndicadoresVisuales();
        if (!dentro(filaOrigen, columnaOrigen)) {
            return;
        }
        Pieza atacante = piezas[filaOrigen][columnaOrigen];
        if (atacante == null) {
            return;
        }

        marcarGrupoMuerte();
        botones[filaOrigen][columnaOrigen].setBorder(BORDE_SELECCION);

        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                if (fila == filaOrigen && columna == columnaOrigen) {
                    continue;
                }
                Pieza destino = piezas[fila][columna];
                if (destino == null) {
                    boolean puedeMover = puedeMover(atacante, filaOrigen,
                            columnaOrigen, fila, columna);
                    if (atacante instanceof Muerte) {
                        botones[fila][columna].setDestacado(puedeMover
                                ? CasillaTablero.Destacado.MOVIMIENTO_INVOCACION
                                : CasillaTablero.Destacado.INVOCACION);
                    } else if (puedeMover) {
                        botones[fila][columna].setDestacado(
                                CasillaTablero.Destacado.MOVIMIENTO);
                    }
                } else if (!atacante.getEquipo().equals(destino.getEquipo())
                        && puedeAtacar(atacante, filaOrigen, columnaOrigen,
                                fila, columna)) {
                    botones[fila][columna].setDestacado(
                            CasillaTablero.Destacado.ATAQUE);
                }
            }
        }
        repaint();
    }

    private void marcarGrupoMuerte() {
        if (muerteActiva == null) {
            return;
        }
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                if (esIntegranteMuerteActiva(piezas[fila][columna])) {
                    botones[fila][columna].setBorder(BORDE_GRUPO);
                }
            }
        }
    }

    private boolean puedeAtacar(Pieza atacante, int origenF, int origenC,
            int destinoF, int destinoC) {
        int distanciaF = Math.abs(destinoF - origenF);
        int distanciaC = Math.abs(destinoC - origenC);
        int distancia = Math.max(distanciaF, distanciaC);
        if (distancia == 1) {
            return true;
        }
        if (!(atacante instanceof Muerte muerte)) {
            return false;
        }
        if (esLanzaValida(origenF, origenC, destinoF, destinoC)) {
            return true;
        }
        return distancia > 2 && buscarZombieAdyacente(
                destinoF, destinoC, atacante.getEquipo()) != null;
    }

    private void procesarCasillaVacia(Pieza atacante, int fila, int columna) {
        boolean movimientoValido = puedeMover(
                atacante, filaOrigen, columnaOrigen, fila, columna);
        if (atacante instanceof Muerte) {
            String[] opcionesAccion = movimientoValido
                    ? new String[]{"Mover", "Invocar Zombie", "Cancelar"}
                    : new String[]{"Invocar Zombie", "Cancelar"};
            int eleccion = RecursosVisuales.mostrarOpciones(
                    this,
                    "¿Qué acción desea ejecutar con la Muerte?",
                    "Habilidad del Nigromante",
                    opcionesAccion);
            if (eleccion < 0 || "Cancelar".equals(opcionesAccion[eleccion])) {
                return;
            }
            if ("Invocar Zombie".equals(opcionesAccion[eleccion])) {
                invocarZombie((Muerte) atacante, fila, columna);
            } else {
                moverPieza(fila, columna);
            }
            return;
        }
        if (!movimientoValido) {
            JOptionPane.showMessageDialog(this, mensajeMovimiento(atacante));
            return;
        }
        moverPieza(fila, columna);
    }

    private String mensajeMovimiento(Pieza pieza) {
        if (pieza instanceof Lobo) {
            return "El Hombre Lobo solo puede avanzar hasta 2 casillas "
                    + "en línea horizontal, vertical o diagonal, sin obstáculos.";
        }
        return "La pieza solo puede avanzar a una casilla adyacente vacía.";
    }

    private boolean puedeMover(Pieza pieza, int origenF, int origenC,
            int destinoF, int destinoC) {
        if (pieza == null || pieza.getMovilidad() == 0
                || piezas[destinoF][destinoC] != null) {
            return false;
        }
        int deltaF = destinoF - origenF;
        int deltaC = destinoC - origenC;
        int distanciaF = Math.abs(deltaF);
        int distanciaC = Math.abs(deltaC);
        boolean direccionValida = deltaF == 0 || deltaC == 0
                || distanciaF == distanciaC;
        int distancia = Math.max(distanciaF, distanciaC);
        if (!direccionValida || distancia == 0
                || distancia > pieza.getMovilidad()) {
            return false;
        }
        if (distancia == 2) {
            int intermediaF = origenF + Integer.signum(deltaF);
            int intermediaC = origenC + Integer.signum(deltaC);
            return piezas[intermediaF][intermediaC] == null;
        }
        return true;
    }

    private void moverPieza(int fila, int columna) {
        Pieza pieza = piezas[filaOrigen][columnaOrigen];
        piezas[fila][columna] = pieza;
        piezas[filaOrigen][columnaOrigen] = null;
        actualizarTablero();
        panel.setMensaje("Se movió la pieza " + pieza.getNombre() + ".");
        finalizarAccion();
    }

    private void invocarZombie(Muerte muerte, int fila, int columna) {
        Zombie zombie = new Zombie(muerte);
        zombie.setEquipo(muerte.getEquipo());
        piezas[fila][columna] = zombie;
        actualizarTablero();
        panel.setMensaje("Se invocó un Zombie en la casilla seleccionada.");
        finalizarAccion();
    }

    private void procesarAtaque(Pieza atacante, Pieza objetivo,
            int filaObjetivo, int columnaObjetivo) {
        int distanciaF = Math.abs(filaObjetivo - filaOrigen);
        int distanciaC = Math.abs(columnaObjetivo - columnaOrigen);
        boolean adyacente = Math.max(distanciaF, distanciaC) == 1;

        if (adyacente) {
            if (atacante instanceof Vampiro) {
                String[] acciones = {"Ataque normal", "Absorber sangre", "Cancelar"};
                int opcion = elegirAtaque(acciones);
                if (opcion == 0) {
                    ejecutarAtaque(atacante, objetivo, filaObjetivo,
                            columnaObjetivo, "ataque normal", null);
                } else if (opcion == 1) {
                    ejecutarAtaque(atacante, objetivo, filaObjetivo,
                            columnaObjetivo, "absorción de sangre", atacante);
                }
                return;
            }
            ejecutarAtaque(atacante, objetivo, filaObjetivo,
                    columnaObjetivo, "ataque normal", null);
            return;
        }

        if (atacante instanceof Muerte) {
            boolean lanzaValida = esLanzaValida(
                    filaOrigen, columnaOrigen, filaObjetivo, columnaObjetivo);
            if (lanzaValida) {
                ejecutarAtaque(atacante, objetivo, filaObjetivo,
                        columnaObjetivo, "ataque con lanza", atacante);
                return;
            }
            int distancia = Math.max(distanciaF, distanciaC);
            Pieza zombie = distancia > 2
                    ? buscarZombieAdyacente(
                            filaObjetivo, columnaObjetivo,
                            atacante.getEquipo())
                    : null;
            if (zombie != null) {
                ejecutarAtaque(atacante, objetivo, filaObjetivo,
                        columnaObjetivo, "ataque mediante Zombie", zombie);
                return;
            }
        }
        JOptionPane.showMessageDialog(this,
                "La pieza enemiga está fuera del alcance permitido.");
    }

    private int elegirAtaque(String[] acciones) {
        return RecursosVisuales.mostrarOpciones(
                this,
                "Seleccione el ataque que desea realizar:",
                "Combate",
                acciones);
    }

    private boolean esLanzaValida(int origenF, int origenC,
            int destinoF, int destinoC) {
        int deltaF = Math.abs(destinoF - origenF);
        int deltaC = Math.abs(destinoC - origenC);
        boolean distanciaCorrecta =
                (deltaF == 2 && deltaC == 0) || (deltaF == 0 && deltaC == 2);
        if (!distanciaCorrecta) {
            return false;
        }
        int intermediaF = (origenF + destinoF) / 2;
        int intermediaC = (origenC + destinoC) / 2;
        return piezas[intermediaF][intermediaC] == null;
    }

    private Pieza buscarZombieAdyacente(int fila, int columna,
            String equipo) {
        for (int f = fila - 1; f <= fila + 1; f++) {
            for (int c = columna - 1; c <= columna + 1; c++) {
                if (dentro(f, c) && piezas[f][c] instanceof Zombie zombie
                        && equipo.equals(zombie.getEquipo())) {
                    return zombie;
                }
            }
        }
        return null;
    }

    private void ejecutarAtaque(Pieza atacante, Pieza objetivo,
            int filaObjetivo, int columnaObjetivo, String tipoAtaque,
            Pieza ejecutorEspecial) {
        int escudoAntes = objetivo.getEscudo();
        int saludAntes = objetivo.getSalud();
        int danio;
        if (ejecutorEspecial == null) {
            danio = atacante.atacarNormal(objetivo);
        } else {
            danio = ejecutorEspecial.Habilidad(objetivo);
        }

        String mensaje;
        if (objetivo.estaViva()) {
            mensaje = "Se atacó la pieza " + objetivo.getNombre()
                    + " mediante " + tipoAtaque + " y se le aplicaron "
                    + danio + " puntos; le quedan " + objetivo.getEscudo()
                    + " puntos de escudo y " + objetivo.getSalud()
                    + " de vida. Daño efectivo: escudo "
                    + (escudoAntes - objetivo.getEscudo()) + ", vida "
                    + (saludAntes - objetivo.getSalud()) + ".";
        } else {
            Jugador propietario = "blanco".equals(objetivo.getEquipo())
                    ? jugadorBlanco : jugadorNegro;
            mensaje = "Se destruyó la pieza " + objetivo.getNombre()
                    + " del jugador " + propietario.getUserName() + ".";
            registrarCaptura(atacante.getEquipo(), objetivo);
            piezas[filaObjetivo][columnaObjetivo] = null;
            if (objetivo instanceof Muerte muerteDestruida) {
                int zombiesEliminados = eliminarZombiesDe(
                        muerteDestruida, atacante.getEquipo());
                if (zombiesEliminados > 0) {
                    mensaje += " Sus " + zombiesEliminados
                            + (zombiesEliminados == 1
                                    ? " Zombie también murió."
                                    : " Zombies también murieron.");
                }
            }
        }
        actualizarTablero();
        panel.setMensaje(mensaje);
        JOptionPane.showMessageDialog(this, mensaje);
        if (!verificarFinPartida()) {
            finalizarAccion();
        }
    }

    private int eliminarZombiesDe(Muerte muerteDestruida,
            String equipoCapturador) {
        int eliminados = 0;
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                if (piezas[fila][columna] instanceof Zombie zombie
                        && zombie.getDuena() == muerteDestruida) {
                    registrarCaptura(equipoCapturador, zombie);
                    piezas[fila][columna] = null;
                    eliminados++;
                }
            }
        }
        return eliminados;
    }

    private void registrarCaptura(String equipoCapturador, Pieza capturada) {
        if ("blanco".equals(equipoCapturador)) {
            if (cantidadCapturasBlancas < capturasBlancas.length) {
                capturasBlancas[cantidadCapturasBlancas++] = capturada;
            }
        } else if (cantidadCapturasNegras < capturasNegras.length) {
            capturasNegras[cantidadCapturasNegras++] = capturada;
        }
        panel.actualizarCapturas(capturasBlancas, cantidadCapturasBlancas,
                capturasNegras, cantidadCapturasNegras);
    }

    private void finalizarAccion() {
        limpiarSeleccion();
        tipoAutorizado = "";
        cambiarTurno();
    }

    private void cambiarTurno() {
        turnoBlanco = !turnoBlanco;
        prepararTurno();
    }

    private void prepararTurno() {
        tipoAutorizado = "";
        girosUtilizados = 0;
        limpiarSeleccion();
        String nombre = jugadorActual().getUserName();
        panel.actualizarTurno(jugadorActual(), equipoActual());
        panel.mostrarInformacionPieza(null);
        panel.setMensaje("Turno de <b>" + nombre + "</b> ("
                + equipoActual() + "). Gire la ruleta.");
        panel.getRuleta().prepararGiro("Turno de " + nombre + ": gira la ruleta");
    }

    private int girosPermitidos(String equipo) {
        int perdidas = 6 - contarPiezasPrincipales(equipo);
        if (perdidas >= 4) {
            return 3;
        }
        if (perdidas >= 2) {
            return 2;
        }
        return 1;
    }

    private int contarPiezasPrincipales(String equipo) {
        int cantidad = 0;
        for (Pieza[] fila : piezas) {
            for (Pieza pieza : fila) {
                if (pieza != null && !(pieza instanceof Zombie)
                        && equipo.equals(pieza.getEquipo())) {
                    cantidad++;
                }
            }
        }
        return cantidad;
    }

    public boolean hayPiezaDisponible(String tipo, String equipo) {
        if (tipo == null || equipo == null) {
            return false;
        }
        for (Pieza[] fila : piezas) {
            for (Pieza pieza : fila) {
                if (pieza != null && pieza.estaViva()
                        && equipo.equals(pieza.getEquipo())
                        && tipo.equals(pieza.getNombre())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verificarFinPartida() {
        boolean quedanBlancas = quedanPiezas("blanco");
        boolean quedanNegras = quedanPiezas("negro");
        if (quedanBlancas && quedanNegras) {
            return false;
        }
        Jugador ganador = quedanBlancas ? jugadorBlanco : jugadorNegro;
        Jugador perdedor = quedanBlancas ? jugadorNegro : jugadorBlanco;
        finalizarPartida(ganador, ganador.getUserName() + " venció a "
                + perdedor.getUserName()
                + ". ¡Felicidades, has ganado 3 puntos!");
        return true;
    }

    private boolean quedanPiezas(String equipo) {
        for (Pieza[] fila : piezas) {
            for (Pieza pieza : fila) {
                if (pieza != null && equipo.equals(pieza.getEquipo())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void confirmarRetiro() {
        if (partidaFinalizada) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Confirma que desea retirarse de la partida?",
                "Confirmar retiro",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            Jugador retirado = jugadorActual();
            Jugador ganador = turnoBlanco ? jugadorNegro : jugadorBlanco;
            finalizarPartida(ganador, retirado.getUserName()
                    + " se ha retirado. ¡Felicidades, "
                    + ganador.getUserName() + ", has ganado 3 puntos!");
        }
    }

    private void finalizarPartida(Jugador ganador, String mensaje) {
        partidaFinalizada = true;
        panel.getRuleta().cancelarGiro();
        panel.getRuleta().setBotonGirarEnabled(false);
        ganador.agregarVictoria();
        try {
            RepositorioSistema repositorio = MemoriaSistema.getInstancia();
            repositorio.agregarPartida(
                    new HistorialPartida(jugadorBlanco, jugadorNegro, mensaje));
        } catch (ValidacionException excepcion) {
            JOptionPane.showMessageDialog(this,
                    "La partida terminó, pero no pudo registrarse: "
                    + excepcion.getMessage(),
                    "Historial lleno",
                    JOptionPane.WARNING_MESSAGE);
        }
        JOptionPane.showMessageDialog(this, mensaje, "Fin de la partida",
                JOptionPane.INFORMATION_MESSAGE);
        ventana.dispose();
        new MenuPrincipal().setVisible(true);
    }

    private void limpiarSeleccion() {
        limpiarIndicadoresVisuales();
        filaOrigen = -1;
        columnaOrigen = -1;
        muerteActiva = null;
    }

    private void limpiarIndicadoresVisuales() {
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                botones[fila][columna].setDestacado(
                        CasillaTablero.Destacado.NORMAL);
                botones[fila][columna].setBorder(BORDE_NORMAL);
            }
        }
        marcarPiezasAutorizadas();
        repaint();
    }

    private void marcarPiezasAutorizadas() {
        if (tipoAutorizado == null || tipoAutorizado.isBlank()) {
            return;
        }
        String equipo = equipoActual();
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                Pieza pieza = piezas[fila][columna];
                if (pieza != null
                        && equipo.equals(pieza.getEquipo())
                        && tipoAutorizado.equals(pieza.getNombre())) {
                    botones[fila][columna].setBorder(BORDE_RULETA);
                }
            }
        }
    }

    private Jugador jugadorActual() {
        return turnoBlanco ? jugadorBlanco : jugadorNegro;
    }

    private String equipoActual() {
        return turnoBlanco ? "blanco" : "negro";
    }

    private boolean dentro(int fila, int columna) {
        return fila >= 0 && fila < TAMANO && columna >= 0 && columna < TAMANO;
    }

    private void actualizarTablero() {
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                CasillaTablero boton = botones[fila][columna];
                boton.setColorBase((fila + columna) % 2 == 0
                        ? new Color(226, 224, 218)
                        : new Color(184, 188, 196));
                Pieza pieza = piezas[fila][columna];
                boton.setIcon(cargarIcono(pieza));
                boton.setToolTipText(pieza == null ? "Casilla vacía"
                        : pieza.getNombre() + " - " + pieza.getEquipo()
                        + " | Vida: " + pieza.getSalud()
                        + " | Escudo: " + pieza.getEscudo());
            }
        }
        revalidate();
        repaint();
    }

    private ImageIcon cargarIcono(Pieza pieza) {
        if (pieza == null) {
            return null;
        }
        String tipo = switch (pieza.getNombre()) {
            case "lobo" -> "Lobo";
            case "vampiro" -> "Vampiro";
            case "muerte" -> "Muerte";
            case "zombie" -> "Zombie";
            default -> "";
        };
        String color = "blanco".equals(pieza.getEquipo()) ? "Blanco" : "Negro";
        try {
            URL recurso = getClass().getResource(
                    "/vampireswargame/imagenes/" + tipo + color + ".png");
            if (recurso == null) {
                return null;
            }
            Image imagen = new ImageIcon(recurso).getImage()
                    .getScaledInstance(82, 82, Image.SCALE_SMOOTH);
            return new ImageIcon(imagen);
        } catch (RuntimeException excepcion) {
            return null;
        }
    }

    Pieza[][] getPiezasParaPruebas() {
        return piezas;
    }
}
