import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LevelLoaderTest {

    @Test
    void testLoadLevelFromTextFormat() {
        // los caracteres estan separados por espacios
        // los WALLS con comas
        
        // S O X  (start, suelo, abbys)
        // O E L  (suelo, salida, lock)
        // K O O  (key, suelo, suelo)
        
        List<String> lines = Arrays.asList( // creación de un minimapa para hacer pruebas 
            "S O X", 
            "O E L", 
            "K O O",
            "# WALLS",
            "0,0,0,1", // muro vertical entre (0,0) y (0,1)
            "1,1,2,1"  // muro horizontal entre (1,1) y (2,1)
        );

        LevelLoader loader = new LevelLoader();
        Level level = new Level();

        // carga del nivel
        try {
            loader.loadLevelFromLines(level, lines);
        } catch (Exception e) {
            fail("La carga del nivel no debería lanzar excepciones: " + e.getMessage());
        }

        // --- verificaciones de la cuadrícula ---
        
        // verificación start ('S') en (0,0)
        assertEquals(0, level.getStartX(), "Debe cargar la coordenada X de inicio");
        assertEquals(0, level.getStartY(), "Debe cargar la coordenada Y de inicio");
        assertEquals('S', level.getCell(0, 0), "Bajo la 'S' debe haber un suelo transitable");

        // verificación abbys ('X') en (2,0) -> x=2, y=0
        assertEquals('X', level.getCell(2, 0));
        assertTrue(level.getTile(0, 2) instanceof AbbysTile, "La 'X' debe ser un AbbysTile (ojo: getTile usa fila,col -> y,x)");

        // verificación exit ('E') en (1,1)
        assertTrue(level.isExit(1, 1), "Debe marcar la casilla (1,1) como salida");
        
        // verificación lock ('L') en (2,1)
        assertTrue(level.getTile(1, 2) instanceof LockTile, "En (2,1) debe haber un LockTile");

        // verificación key ('K') en (0,2)
        assertTrue(level.getTile(2, 0) instanceof KeyTile, "En (0,2) debe haber un KeyTile");


        // --- verificaciones de walls ---
        
        // verificación muro 1: 0,0,0,1
        assertTrue(level.hasWall(0, 0, 0, 1), "Debe existir el muro cargado desde la sección # WALLS");

        // verificación muro 2: 1,1,2,1
        assertTrue(level.hasWall(1, 1, 2, 1), "Debe existir el muro horizontal cargado");

        // verificación que NO hay muros inventados
        assertFalse(level.hasWall(0, 0, 1, 0), "No debería haber muro donde no se definió");
    }
}