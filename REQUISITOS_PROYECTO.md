# Cumplimiento de Vampire Wargame

El código que compila NetBeans se encuentra en `src/vampireswargame`.

## Requisitos técnicos

- Herencia y polimorfismo: `Pieza` es la base de `Vampiro`, `Lobo`,
  `Muerte` y `Zombie`. El tablero trabaja con referencias `Pieza`.
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

## Pruebas

`test/vampireswargame/PruebasLogica.java` comprueba la estructura orientada
a objetos, el orden del daño, las habilidades y el almacenamiento en
arreglos. El artefacto ejecutable se genera en `dist/VampiresWarGame.jar`.
