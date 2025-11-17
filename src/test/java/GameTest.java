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

}

