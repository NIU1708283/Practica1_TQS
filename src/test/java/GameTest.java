import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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

        // definimos la Salida en (0, 1)
        level.setExit(0, 1);

        // iluminamos todas las casillas excepto la de la Salida a la fuerza
        for (int i = 0; i < level.getSize(); i++) {
            for (int j = 0; j < level.getSize(); j++) {
                level.setCell(i, j, '*');
            }
        }
        // apagamos la casilla de la Salida
        level.setCell(0, 1, 'O');

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

        level.setExit(0, 1);
        game.setPlayerPosition(0, 0);
        game.movePlayer(Direction.DOWN); // (0,0) -> (0,1)

        // el estado debe ser LOST_INCOMPLETE porque quedan casillas oscuras en el mapa
        assertEquals(GameStatus.LOST_INCOMPLETE, game.getStatus(), 
            "Llegar a la salida sin iluminar todo debe resultar en derrota incompleta");
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
}

