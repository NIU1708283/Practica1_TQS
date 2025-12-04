import java.io.IOException;

public class Game 
{
    private final LevelLoader levelLoader = new LevelLoader();
    private Level level;
    private GameStatus status;
    private int positionX;
    private int positionY;
    private int keysCollected;

    public void addKey() {
        keysCollected++;
    }

    public void useKey() {
        if (keysCollected > 0) {
            keysCollected--;
        }
    }

    public void startNewGame() {
        positionX = 0;
        positionY = 0;
        level = new Level(); 
        status = GameStatus.PLAYING;
        keysCollected = 0;
    }

    public void setPlayerPosition(int x, int y) {
        positionX = x;
        positionY = y;
        level.setCell(positionY, positionX, '*'); // iluminar la casilla donde se coloca el jugador
    }

    public boolean movePlayer(Direction direction) {
        int nextX = positionX;
        int nextY = positionY;
        switch (direction) {
            case UP -> nextY -= 1;
            case DOWN -> nextY += 1;
            case LEFT -> nextX -= 1;
            case RIGHT -> nextX += 1;
        }

        // muros
        if (level.hasWall(positionX, positionY, nextX, nextY)) return false;
        // límites
        if (nextX < 0 || nextX >= level.getSIZE() || nextY < 0 || nextY >= level.getSIZE()) return false;
        
        Tile targetTile = level.getTile(nextY, nextX);

        if (targetTile.isLit()) {
            status = GameStatus.LOST_OVERHEAT;
            positionX = nextX;
            positionY = nextY;
            return true;
        }

        if (!targetTile.isWalkable(this)) { return false; }

        positionX = nextX;
        positionY = nextY;

        targetTile.onStep(this);
        
        return true;
    }

    public boolean isTileLit(int x, int y) {
        return level.getCell(y, x) == '*';
    }

    public void updateWorld(double deltaTime) {
        if (status != GameStatus.PLAYING) return;

        level.updateTiles(deltaTime);

        // mirar si el jugador está en una casilla letal AHORA
        Tile currentTile = level.getTile(positionY, positionX);
        if (currentTile.isDeadly()) {
            status = GameStatus.LOST_FIRE; // el jugador muere quemado
        }
    }

    public void teleportTo(int x, int y) {
        if (x < 0 || x >= level.getSIZE() || y < 0 || y >= level.getSIZE()) {
            return; // fuera de límites, no hacemos nada
        }
        if (positionX == x && positionY == y) {
            return; // ya estamos ahí, no hacemos nada
        }
        
        positionX = x;
        positionY = y;
        
        // al aterrizar, interactuamos con la casilla de destino (para que se ilumine o recojamos un objeto)
        Tile destTile = level.getTile(y, x);
        destTile.onStep(this);
        
    }

    public void loadLevel(int levelNumber) {
        startNewGame(); 
        
        try {
            String path = "src/maps/level" + levelNumber + ".txt";
            levelLoader.loadLevel(this.level, path);
            
            this.positionX = level.getStartX();
            this.positionY = level.getStartY();
            level.setCell(positionY, positionX, '*');
            
        } catch (IOException e) {
            System.err.println("Error cargando el nivel " + levelNumber + ": " + e.getMessage());
        }
    }

    public GameStatus getStatus() { return status; }
    public int getPlayerX() { return positionX; }
    public int getPlayerY() { return positionY; }
    public Level getLevel() { return level; }
    public int getKeysCollected() { return keysCollected; }

    public void setPlayerStartPosition(int x, int y) { level.setStart(x, y); }
    public void setStatus(GameStatus status) { this.status = status; }

}