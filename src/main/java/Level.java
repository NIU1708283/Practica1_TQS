import java.util.HashSet;
import java.util.Set;

public final class Level {
    private final int SIZE = 20;
    private final Tile[][] board; 
    private final Set<String> walls;
    private int startX = 0;
    private int startY = 0;
    private int exitX = -1;
    private int exitY = -1;
    
    public Level() {
        board = new Tile[SIZE][SIZE];
        walls = new HashSet<>();
        reset();
    }
    
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            throw new IndexOutOfBoundsException("Coordenadas fuera de limites");
        }
        return board[y][x];
    }

    public char getCell(int x, int y) { return getTile(x, y).getCharacter(); }
    
    public void setCell(int x, int y, char value) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            throw new IndexOutOfBoundsException("Cell index out of bounds");
        }
        
        Tile newTile;
        switch (value) {
            case 'O' -> newTile = new FloorTile();
            case 'X' -> newTile = new AbbysTile();
            case 'K' -> newTile = new KeyTile();
            case 'L' -> newTile = new LockTile();
            case 'F' -> newTile = new FireTile();
            case 'S' -> {
                newTile = new StartTile();
                this.startX = x;
                this.startY = y;
            }
            case 'E' -> {
                newTile = new EndTile();
                this.exitX = x;
                this.exitY = y;
            }
            case '*' -> {
                newTile = new FloorTile();
                newTile.setLit(true);
            }
            default -> newTile = new FloorTile();
        }
        board[y][x] = newTile;
    }
    
    public void setTile(int x, int y, Tile tile) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            throw new IndexOutOfBoundsException("Cell index out of bounds");
        }
        board[y][x] = tile;
    }
    
    public void reset() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                board[y][x] = new FloorTile(); 
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

    public void setStart(int x, int y) { startX = x; startY = y; }
    public boolean isStart(int x, int y) { return x == startX && y == startY; }
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }

    public void setExit(int x, int y) { exitX = x; exitY = y; }
    public boolean isExit(int x, int y) { return x == exitX && y == exitY; }

    public boolean hasUnlitTiles() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (!board[y][x].isLit()) { 
                    return true;
                }
            }
        }
        return false;
    }

    public void updateTiles(double deltaTime) {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                board[y][x].update(deltaTime);
            }
        }
    }

    public void addTeleport(int x, int y, int destX, int destY) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return;
        setTile(x, y, new TeleportTile(destX, destY));
    }
}