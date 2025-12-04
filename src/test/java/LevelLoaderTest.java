import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LevelLoaderTest {

    @Test
    void testLoadLevelFromTextFormat() {
        // mapa de prueba:
        // S O X  (StartTile, FloorTile, AbbysTile)
        // O E L  (FloorTile, EndTile, LockTile)
        // K O O  (KeyTile, FloorTile, FloorTile)
        
        List<String> lines = Arrays.asList( 
            "S O X", 
            "O E L", 
            "K O O",
            "# WALLS",
            "0,0,0,1", 
            "1,1,2,1"
        );

        LevelLoader loader = new LevelLoader();
        Level level = new Level();

        try {
            loader.loadLevelFromLines(level, lines);
        } catch (Exception e) {
            fail("La carga del nivel no debería lanzar excepciones: " + e.getMessage());
        }

        // --- verificaciones de cuadrícula ---
        
        // verificación start ('S') en (0,0)
        assertEquals(0, level.getStartX());
        assertEquals(0, level.getStartY());
        assertTrue(level.getTile(0, 0) instanceof StartTile, "La casilla inicial debe ser StartTile");
        assertEquals('S', level.getCell(0, 0)); 

        // verificación abbys ('X') en (2,0)
        assertEquals('K', level.getCell(2, 0));
        assertTrue(level.getTile(0, 2) instanceof AbbysTile);

        // verificación exit ('E') en (1,1)
        assertTrue(level.isExit(1, 1));
        assertTrue(level.getTile(1, 1) instanceof EndTile, "La casilla de salida debe ser EndTile");
        assertEquals('E', level.getCell(1, 1));

        // verificación lock ('L') en (2,1)
        assertTrue(level.getTile(1, 2) instanceof LockTile);

        // verificación key ('K') en (0,2)
        assertTrue(level.getTile(2, 0) instanceof KeyTile);


        // --- verificaciones de walls ---
        assertTrue(level.hasWall(0, 0, 0, 1));
        assertTrue(level.hasWall(1, 1, 2, 1));
        assertFalse(level.hasWall(0, 0, 1, 0));
    }

    @Test
    void testWallCoordinatesAreRowCol() {
        // simulamos un archivo con un muro definido como: 0,5,1,5
        List<String> lines = Arrays.asList(
            "O O O O O O", 
            "O O O O O O",
            "# WALLS",
            "0,5,1,5" 
        );

        LevelLoader loader = new LevelLoader();
        Level level = new Level();

        try {
            loader.loadLevelFromLines(level, lines);
        } catch (Exception e) {
            fail("Excepción cargando nivel: " + e.getMessage());
        }
        
        boolean hasVerticalWall = level.hasWall(5, 0, 5, 1);
        assertTrue(hasVerticalWall, "El muro 0,5,1,5 debería bloquear el movimiento vertical en la columna 5 (x=5)");
        
        boolean hasHorizontalWall = level.hasWall(0, 5, 1, 5);
        assertFalse(hasHorizontalWall, "No debería haber creado un muro horizontal interpretando coordenadas al revés");
    }
}