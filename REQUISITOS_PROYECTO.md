# Cumplimiento de Vampire Wargame

El código que compila NetBeans se encuentra en `src/vampireswargame`.

## Requisitos técnicos

- Herencia y polimorfismo: `Pieza` es la base de `Vampiro`, `Lobo`,
  `Muerte` y `Zombie`. El tablero trabaja con referencias `Pieza`.
- Herencia entre clases concretas: `Usuario` es la clase padre normal y
  `Jugador` es su clase hija normal; hereda nombre, fecha y estado de cuenta.
- Clase y método abstractos: `Pieza` y sus métodos `getNombre()` y
  `Habilidad(Pieza)`.
- Clases y métodos finales: `Lobo`, `Muerte`, `Zombie`, `MiCuenta`,
  `MemoriaSistema` y varios métodos de `Pieza`.
- Recursividad: búsqueda de jugadores y ordenamiento por puntos en
  `MemoriaSistema`.
- Arreglos: `MemoriaSistema` guarda jugadores e historial en arreglos y
  cumple el contrato `RepositorioSistema`. No utiliza archivos, bases de
  datos ni colecciones.
- Excepciones: validaciones mediante `ValidacionException`, acciones de
  Swing protegidas por `UiSeguro` y manejador global de último recurso.
- Clase principal: `VampiresWarGame.main()` solo delega el arranque.
- GUI: todas las pantallas y el tablero utilizan Swing.

## Prácticas de usabilidad

- Paneles permanentes identifican Jugador 1/blanco/parte inferior y
  Jugador 2/negro/parte superior.
- El jugador que tiene el turno se resalta con borde dorado.
- Las piezas capturadas se muestran mediante resumen e iconos.
- La pieza seleccionada muestra ataque, vida, escudo, movimiento y habilidad.
- El tablero incluye una leyenda para movimiento, ataque e invocación.
- Los formularios incluyen tooltips, etiquetas accesibles, botón predeterminado
  y opción para mostrar u ocultar contraseñas.

## Pruebas

`test/vampireswargame/PruebasLogica.java` comprueba la estructura orientada
a objetos, el orden del daño, las habilidades y el almacenamiento en
arreglos. El artefacto ejecutable se genera en `dist/VampiresWarGame.jar`.
