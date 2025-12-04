import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameViewTest 
{
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testRenderDrawsBoardAndPlayer() {
        // nivel pequeño 5x5 lleno de suelo
        Level level = new Level(); 
        level.setCell(0, 0, 'O'); 
        level.setCell(0, 1, 'O');
        
        GameView view = new GameView();

        // jugador en (0, 0)
        view.render(level, 0, 0);

        String output = outContent.toString();

        assertTrue(output.contains("P"), "Debe dibujar al jugador como 'P'");
        
        assertTrue(output.contains("."), "Debe dibujar el suelo vacío como '.'");
    }
}