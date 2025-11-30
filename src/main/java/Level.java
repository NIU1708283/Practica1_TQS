import java.util.HashSet;
import java.util.Set;

public final class Level {
    private final int SIZE = 20;
    private final Tile[][] board; 
    private final Set<String> walls;
    private int exitX = -1;
    private int exitY = -1;
    
    public Level() {
        board = new Tile[SIZE][SIZE];
        walls = new HashSet<>();
        reset();
    }
    
    public Tile getTile(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IndexOutOfBoundsException("Cell index out of bounds");
        }
        return board[row][col];
    }

    public char getCell(int row, int col) {
        return getTile(row, col).getCharacter();
    }
    
    public void setCell(int row, int col, char value) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IndexOutOfBoundsException("Cell index out of bounds");
        }
        
        // Factoría simple para convertir char -> Tile
        Tile newTile;
        switch (value) {
            case 'K' -> newTile = new KeyTile();
            case 'L' -> newTile = new LockTile();
            case 'F' -> newTile = new FireTile();
            case '*' -> {
                newTile = new FloorTile();
                newTile.setLit(true);
            }
            default -> newTile = new FloorTile();
        }
        board[row][col] = newTile;
    }
    
    public void setTile(int row, int col, Tile tile) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IndexOutOfBoundsException("Cell index out of bounds");
        }
        board[row][col] = tile;
    }
    
    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = new FloorTile(); 
            }
        }
        walls.clear();
    }
    
    public int getSIZE() { return SIZE; }
    
    public void addWall(int x1, int y1, int x2, int y2) {
        walls.add(getWallKey(x1, y1, x2, y2));
    }
    public boolean hasWall(int x1, int y1, int x2, int y2) {
        return walls.contains(getWallKey(x1, y1, x2, y2));
    }
    private String getWallKey(int x1, int y1, int x2, int y2) {
        if (x1 < x2 || (x1 == x2 && y1 < y2)) {
            return x1 + "," + y1 + "-" + x2 + "," + y2;
        }
        return x2 + "," + y2 + "-" + x1 + "," + y1;
    }

    public void setExit(int x, int y) { exitX = x; exitY = y; }
    public boolean isExit(int x, int y) { return x == exitX && y == exitY; }

    public boolean hasUnlitTiles() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!board[i][j].isLit()) { 
                    return true;
                }
            }
        }
        return false;
    }

    public void updateTiles(double deltaTime) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j].update(deltaTime);
            }
        }
    }
}