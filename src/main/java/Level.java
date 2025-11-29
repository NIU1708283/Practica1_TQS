import java.util.HashSet; // para almacenar muros que van entre casillas
import java.util.Set;

public final class Level {
    public static final int SIZE = 20; // tamaño fijo invariable del tablero 20x20
    private final char[][] board;
    private final Set<String> walls;
    private int exitX = -1;
    private int exitY = -1;

    public Level() {
        board = new char[SIZE][SIZE];
        walls = new HashSet<>();
        reset();
        
    }
    
    public char getCell(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
        throw new IndexOutOfBoundsException("Cell index out of bounds");
        }
        return board[row][col];
    }
    
    public void setCell(int row, int col, char value) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
        throw new IndexOutOfBoundsException("Cell index out of bounds");
        }
        board[row][col] = value;
    }
    
    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 'O';
            }
        }
    }
    
    public int getSize() {
        return SIZE;
    }
    
    public void setExit(int x, int y) {
        this.exitX = x;
        this.exitY = y;
    }

    public boolean isExit(int x, int y) {
        return x == exitX && y == exitY;
    }

    // comprueba si queda alguna casilla oscura en el tablero
    public boolean hasUnlitTiles() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 'O') {
                    return true;
                }
            }
        }
        return false;
    }

    // para añadir muro entre dos celdas adyacentes
    public void addWall(int x1, int y1, int x2, int y2) {
        walls.add(getWallKey(x1, y1, x2, y2));
    }

    public boolean hasWall(int x1, int y1, int x2, int y2) {
        return walls.contains(getWallKey(x1, y1, x2, y2));
    }

    // para generar una clave única para la conexión entre dos celdas
    private String getWallKey(int x1, int y1, int x2, int y2) {
        // Ordenamos las coordenadas para que el muro sea bidireccional (que "0,0-1,0" sea lo mismo que "1,0-0,0")
        if (x1 < x2 || (x1 == x2 && y1 < y2)) {
            return x1 + "," + y1 + "-" + x2 + "," + y2;
        }
        return x2 + "," + y2 + "-" + x1 + "," + y1;
    }


}
