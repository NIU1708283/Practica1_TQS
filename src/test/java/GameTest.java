import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class GameTest {
    
    @BeforeEach
    void setUp() {
        // Configuración previa a cada prueba si es necesaria
    }

    @Test
    void testPlayerMovesToEmptyTile() {
        Game game = new Game();
        game.startNewGame();
        game.setPlayerPosition(1, 1); // Starting position
        
        boolean moveResult = game.movePlayer(Direction.UP); // Move to an empty tile
        
        assertTrue(moveResult, "Player should be able to move to an empty tile");
        assertEquals(1, game.getPlayerX(), "Player X position should be updated");
        assertEquals(0, game.getPlayerY(), "Player Y position should be updated");
    }

    @Test
    void testPlayerMoveLightsUpNewTile() { // comprova que quan es mou a una nova casella, aquesta s'il·lumina
        Game game = new Game();
        game.startNewGame();
        game.setPlayerPosition(5, 5); // Starting position
        game.movePlayer(Direction.RIGHT); // Move to a new tile

        boolean isLit = game.isTileLit(6, 5); // Assume the tile lights up successfully
        
        assertTrue(isLit, "The new tile should light up");

    }

    @Test
    void testSteppingOnLitTileCausesGameOver() { //
        Game game = new Game();
        game.startNewGame();
        game.setPlayerPosition(10, 10);

        game.movePlayer(Direction.RIGHT);
        game.movePlayer(Direction.LEFT);
        game.movePlayer(Direction.RIGHT);

        // el estado debería ser de derrota por sobrecalentamiento
        assertEquals(GameStatus.LOST_OVERHEAT, game.getStatus(), "El jugador debería perder al pisar una casilla ya iluminada");
    }

    @Test
    void testAbbysTileBlocksMovement() {
        Game game = new Game();
        game.startNewGame();
        Level level = game.getLevel();

        level.setCell(0, 1, 'X'); 
        game.setPlayerPosition(0, 0);

        boolean moveResult = game.movePlayer(Direction.DOWN);

        assertFalse(moveResult, "El jugador no debería poder caer en el abismo (AbbysTile)");
        assertEquals(0, game.getPlayerY(), "La posición Y no debería cambiar");
    }

    @Test
    void testMovementBlockedByWall() {
        Game game = new Game();
        game.startNewGame();
        game.setPlayerPosition(0, 0);

        // ponemos un muro vertical entre (0,0) y (1,0) (a la derecha del jugador)
        game.getLevel().addWall(0, 0, 1, 0);

        // intentamos mover a la derecha
        boolean moveResult = game.movePlayer(Direction.RIGHT);

        assertFalse(moveResult, "El movimiento debería fallar pq hay un muro");
        assertEquals(0, game.getPlayerX(), "El jugador no debería haber cambiado de X");
        assertEquals(0, game.getPlayerY(), "El jugador no debería haber cambiado de Y");
    }

    @Test
    void testWinningCondition() {
        Game game = new Game();
        game.startNewGame();
        Level level = game.getLevel();

        // iluminamos todas las casillas excepto la de la Salida a la fuerza
        for (int i = 0; i < level.getSIZE(); i++) {
            for (int j = 0; j < level.getSIZE(); j++) {
                level.setCell(i, j, '*');
            }
        }
        // apagamos la casilla de la Salida
        level.setCell(0, 1, 'E');
        // ponemos al jugador al lado (0, 0)
        game.setPlayerPosition(0, 0);

        // lo movemos a la salida
        game.movePlayer(Direction.DOWN); // (0,0) -> (0,1)

        
        assertEquals(GameStatus.WON, game.getStatus(), "El juego debería ganarse al llegar a la salida con todo iluminado");
    }

    @Test
    void testIncompleteLevelFails() {
        Game game = new Game();
        game.startNewGame();
        Level level = game.getLevel();

        level.setCell(0, 1, 'E');
        game.setPlayerPosition(0, 0);
        game.movePlayer(Direction.DOWN); // (0,0) -> (0,1)

        // el estado debe ser LOST_INCOMPLETE porque quedan casillas oscuras en el mapa
        assertEquals(GameStatus.LOST_INCOMPLETE, game.getStatus(), 
            "Llegar a la salida sin iluminar todo debe resultar en derrota incompleta");
    }

    @Test
    void testWinConditionIgnoresAbbysTiles() {
        Game game = new Game();
        game.startNewGame();
        Level level = game.getLevel();

        // S E
        // X X  
        level.setCell(0, 0, 'S');
        level.setCell(1, 0, 'E');
        level.setCell(0, 1, 'X');
        level.setCell(1, 1, 'X');

        game.setPlayerPosition(0, 0); 
        game.movePlayer(Direction.RIGHT);

        assertEquals(GameStatus.WON, game.getStatus(), 
            "El juego debería ganarse ignorando las casillas AbbysTile (X) que no se pueden pisar");
    }

    @Test
    void testMovementBlockedByLockWithoutKey() {
        Game game = new Game();
        game.startNewGame();
        Level level = game.getLevel();

        // ponemos un Candado ('L') a la derecha del jugador (1, 0)
        level.setCell(1, 0, 'L');
        game.setPlayerPosition(0, 0);

        boolean moveResult = game.movePlayer(Direction.RIGHT);

        // el movimiento debe fallar porque no tenemos llave
        assertFalse(moveResult, "El jugador no debería poder moverse a una casilla con candado sin la llave");
        assertEquals(0, game.getPlayerX(), "La posición X no debería cambiar");
    }

    @Test
    void testPlayerCollectsKey() {
        Game game = new Game();
        game.startNewGame();
        Level level = game.getLevel();

        // colocamos una Llave a la derecha 
        level.setCell(1, 0, 'K');
        game.setPlayerPosition(0, 0);

        // movemos al jugador hacia la llave
        game.movePlayer(Direction.RIGHT);

        assertEquals(1, game.getKeysCollected(), "El jugador debería haber recogido 1 llave");
        assertEquals('*', level.getCell(1, 0), "La casilla de la llave debería estar ahora iluminada");
    }

    @Test
    void testPlayerOpensLockWithKey() {
        Game game = new Game();
        game.startNewGame();
        Level level = game.getLevel();

        // posiciones: Jugador(0,0) -> Llave(1,0) -> Candado(2,0)
        game.setPlayerPosition(0, 0);
        level.setCell(1, 0, 'K');
        level.setCell(2, 0, 'L');

        game.movePlayer(Direction.RIGHT); // jugador en (1,0), Llaves: 1
        boolean moveResult = game.movePlayer(Direction.RIGHT); // jugador intenta ir a (2,0)

        assertTrue(moveResult, "El jugador debería poder moverse al candado si tiene llave");
        assertEquals(2, game.getPlayerX(), "El jugador debería estar sobre la casilla del candado");
        assertEquals(0, game.getKeysCollected(), "Se debería haber gastado 1 llave");
        assertEquals('*', level.getCell(2, 0), "El candado debería reemplazarse por luz");
    }

    @Test
    void testFireTileKillsPlayerWhenActivated() {
        Game game = new Game();
        game.startNewGame();
        Level level = game.getLevel();

        // jugador en (0,0), FireTile en (0,1)
        game.setPlayerPosition(0, 0);
        level.setCell(0, 1, 'F'); // 'F' = FireTile

        boolean moveResult = game.movePlayer(Direction.DOWN);
        
        assertTrue(moveResult, "El jugador debería poder entrar en el fuego inactivo");
        assertEquals(GameStatus.PLAYING, game.getStatus(), "El juego sigue activo");

        // pasan mas d 2 segundos (fuego se activa)
        game.updateWorld(2.1); 

        assertEquals(GameStatus.LOST_FIRE, game.getStatus(), "El jugador debería morir si el fuego se activa bajo sus pies");
    }

    @Test
    void testTeleportTileMovesPlayerInstantly() {
        Game game = new Game();
        game.startNewGame();
        Level level = game.getLevel();

        // jugador (0,0) -> teleport (1,0) -> destino (5,5)
        game.setPlayerPosition(0, 0);
        
        // nuevo método para poner teleports
        level.addTeleport(1, 0, 5, 5); 

        game.movePlayer(Direction.RIGHT);

        assertEquals(5, game.getPlayerX(), "El jugador debería haber sido teletransportado a X=5");
        assertEquals(5, game.getPlayerY(), "El jugador debería haber sido teletransportado a Y=5");
        
        assertTrue(game.isTileLit(1, 0), "La casilla de entrada del teleport debe iluminarse");
        assertTrue(game.isTileLit(5, 5), "La casilla de destino debe iluminarse al aterrizar");
    }
}

