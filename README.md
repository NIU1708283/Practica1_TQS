# LightyRoom

## 1. Concepte del Joc

Ets un "ser de llum" en un món de rajoles fosques. L'objectiu a cada nivell és simple: il·luminar un camí complet, activant totes les rajoles de l'objectiu **sense passar dues vegades pel mateix lloc**. És un joc de trencaclosques pur, centrat a trobar la ruta correcta.

L'estil visual és minimalista. El fons és fosc i les rajoles són grises. Quan el "ser de llum" (jugador) passa sobre una rajola, aquesta s'il·lumina amb un color groc brillant i hi roman.

**Game Over:** Si el jugador torna a passar sobre una rajola ja il·luminada, aquesta se "sobreescalfa", es torna vermella i la partida acaba (GAME OVER).

## 2. Mecàniques de Joc i Regles


El jugador es controla amb les tecles de fletxa (o WASD).

* **Controls (Terminal):**
    * **Moviment:** `W` (Amunt), `A` (Esquerra), `S` (Avall), `D` (Dreta).
    * **Reiniciar:** `R`.
    * **Sortir Nivel:** `Q` o `ESC`.
    * **Acceptar/Entrar:** ` ` (Espai).

* **Regles Fonamentals:**
    1.  **Il·luminació:** En moure's a una rajola, aquesta s'il·lumina.
    2.  **La Regla d'Or:** No pots tornar a una rajola que ja has il·luminat.
    3.  **Bloqueig:** Si et quedes sense rajoles grises adjacents (no il·luminades) on moure't, quedes encallat i has de reiniciar (`R`).
    4.  **Objectiu:** Cada nivell té un conjunt de rajoles "corrompudes" que s'han d'il·luminar. El nivell es completa quan totes aquestes rajoles estan enceses.
    5.  **Condició de Pèrdua Addicional:** Els mapes tenen un `START` i un `END`. Si el jugador arriba a la casella `END` sense haver il·luminat totes les rajoles requerides, perdrà.

## 3. Mapes i Rajoles Especials

Els mapes són quadrícules de 20x20. La primera versió tindrà 5 mapes.

* **Tipus de Rajoles d'Acció**:
    * **Llave-Candau (Clau-Pany):** En passar per una `Clau`, s'afegeix a l'inventari. Això permet al jugador moure's cap a una casella `Pany` per il·luminar-la.
    * **Foc Intermitent:** Caselles que es bloquegen i desbloquegen cada 2 segons. Si el jugador està en una d'aquestes caselles quan es bloqueja, mor. Si es mou abans, la casella queda il·luminada.
    * **Teleport:** Caselles que transporten el jugador a una altra casella del mapa.

* **Murs:** Representen un espai *entre* dues caselles que impedeix el moviment, tot i que siguin adjacents.

## 4. Estructura del Projecte (MVC)

El projecte segueix una arquitectura Model-Vista-Controlador per separar la lògica de l'estat del joc (Model) de la representació visual (Vista) i la gestió d'entrades (Controlador). Això és crucial per complir els requisits de testeig de l'assignatura, permetent provar el Model i el Controlador de forma aïllada.

---

### Model
Conté l'estat, les dades i les regles de negoci del joc.

* **`Game`**:
    * **Responsabilitat:** Classe principal del Model. Gestiona l'estat general del joc (`GameStatus`), el nivell actual i la lògica de moviment del jugador.
    * **Mètodes Clau:**
        * `movePlayer(Direction direction)`: Mètode central. Calcula la nova posició, comprova murs, comprova si la nova rajola ja està il·luminada (GAME OVER) i interactua amb la rajola.
        * `checkGameStatus()`: Comprova si el jugador ha guanyat (totes les rajoles il·luminades) , ha perdut (encallat, sobreescalfament o finalització incompleta).
        * `restartLevel()`: Reinicia el nivell actual.
        * `updateWorld(double deltaTime)`: Actualitza components que depenen del temps, com les rajoles de `Foc Intermitent`.
* **`Level`**:
    * **Responsabilitat:** Emmagatzema les dades d'un nivell: la quadrícula, la posició inicial/final i les rajoles especials.
    * **Atributs:** `Tile[][] grid`, `Player player`, `startX`, `startY`, `endX`, `endY`, `Set<WallConnection> walls`, `int requiredTiles`.
    * **Mètodes Clau:** `loadMap(int levelNumber)`, `hasWall(int x1, int y1, int x2, int y2)`.
* **`Player`**:
    * **Responsabilitat:** Emmagatzema l'estat del jugador.
    * **Atributs:** `int x`, `int y`, `Set<String> inventory` (per a les claus).
* **`Tile` (Classe Abstracta)**:
    * **Responsabilitat:** Defineix la interfície comuna per a totes les rajoles del joc.
    * **Atributs:** `boolean isLit`.
    * **Mètodes Abstractes:**
        * `isWalkable(Player player)`: Retorna `true` si el jugador pot entrar-hi (p.ex., `LockTile` comprovarà l'inventari del jugador).
        * `onStep(Game game)`: Executa l'acció de la rajola quan el jugador hi entra (p.ex., `KeyTile` afegeix a l'inventari, `TeleportTile` mou el jugador).
* **Subclasses de `Tile`**:
    * `FloorTile`: Rajola bàsica.
    * `StartTile`: Punt d'inici.
    * `EndTile`: Punt final. `onStep` comprova si el joc s'ha completat correctament.
    * `KeyTile`: Dona una clau al jugador.
    * `LockTile`: Requereix una clau per ser transitable.
    * `FireTile`: S'activa/desactiva. `isWalkable` canvia segons el temporitzador. Pot causar GAME OVER.
    * `TeleportTile`: Mou el jugador a altres coordenades.
* **Enums**:
    * `GameStatus`: `PLAYING`, `WON`, `LOST_OVERHEAT`, `LOST_STUCK`, `LOST_INCOMPLETE`.
    * `Direction`: `UP`, `DOWN`, `LEFT`, `RIGHT`.

---

### Vista (View)
Responsable de *dibuixar* l'estat del Model a la terminal. No conté lògica de joc.

* **`GameView` (Terminal)**:
    * **Responsabilitat:** Renderitzar el joc a la consola.
    * **Mètodes Clau:**
        * `render(Level level)`: Dibuixa la quadrícula, el jugador, i l'estat de les rajoles (p.ex., `.` per fosca, `*` per il·luminada, `P` per jugador, `#` per pany).
        * `displayMessage(String message)`: Mostra missatges com "GAME OVER!", "Nivell Completat!" o "Encallat! Prem 'R' per reiniciar".
        * `displayMenu(String[] options)`: Mostra el menú de selecció de nivell.
        * `clearScreen()`: Neteja la consola per al següent fotograma.

---

### Controlador (Controller)
Actua com a pont. Llegeix l'entrada de l'usuari (des de la Vista) i li diu al Model què ha de fer.

* **`GameController`**:
    * **Responsabilitat:** Orquestrar el flux del joc. Conté el bucle principal del joc.
    * **Atributs:** `Game gameModel`, `GameView gameView`.
    * **Mètodes Clau:**
        * `startGame()`: Inicia el bucle principal del joc.
        * `gameLoop()`:
            1.  `gameView.render(gameModel.getCurrentLevel())`
            2.  `gameView.displayMessage(gameModel.getStatusMessage())`
            3.  `char input = gameView.getInput()` (llegeix la tecla de l'usuari)
            4.  `processInput(input)`
            5.  `gameModel.updateWorld(...)` (per al foc intermitent)
        * `processInput(char input)`: Converteix la tecla de l'usuari (`'w'`, `'r'`) en una acció del Model (p.ex., `gameModel.movePlayer(Direction.UP)` o `gameModel.restartLevel()`).
* **`Main`**:
    * **Responsabilitat:** El punt d'entrada de l'aplicació.
    * **Funció:** Crea les instàncies de `Game`, `GameView` i `GameController`, les connecta i crida a `gameController.startGame()`.