import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LevelTest {

  @Test
  void testBoardIsInitializedWithO() {
    Level board = new Level();

    char cellValue = board.getCell(0, 0);

    assertEquals('O', cellValue);
  }

  @Test
  void testSetCellValue() {
    Level board = new Level();

    board.setCell(1, 1, 'X');
    char cellValue = board.getCell(1, 1);

    assertEquals('X', cellValue);
  }

  @Test
  void testResetBoard() {
    Level board = new Level();

    board.setCell(2, 2, 'X');
    board.reset();
    char cellValue = board.getCell(2, 2);

    assertEquals('O', cellValue);
  }

  @Test
  void testBoardSize() {
    Level board = new Level();

    int size = board.getSize();

    assertEquals(20, size);
  }

  @Test
  void testInvalidCellAccess() {
    Level board = new Level();

    try {
      board.getCell(20, 20);
    } catch (IndexOutOfBoundsException e) {
      assertEquals("Cell index out of bounds", e.getMessage());
    }
  }
}