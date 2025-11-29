import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GameTest {
    
    @BeforeEach
    void setUp() {
        // Setup code if needed before each test
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

}

