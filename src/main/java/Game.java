public class Game 
{
    private int positionX;
    private int positionY;
    private Level level; 
    private GameStatus status;
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
        level = new Level(); // Inicializamos el tablero
        status = GameStatus.PLAYING; // Estado inicial
        keysCollected = 0;
    }

    public void setPlayerPosition(int x, int y) {
        positionX = x;
        positionY = y;
        level.setCell(positionX, positionY, '*'); // iluminar la casilla donde se coloca el jugador
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
        
        Tile targetTile = level.getTile(nextX, nextY);

        if (targetTile.isLit()) {
            status = GameStatus.LOST_OVERHEAT;
            positionX = nextX;
            positionY = nextY;
            return true;
        }

        if (!targetTile.isWalkable(this)) {
            return false;
        }

        // actualizar posición PRIMERO
        positionX = nextX;
        positionY = nextY;

        // acción de entrada DESPUÉS
        // si es un Teleport, esto llama a teleportTo(), que vuelve a cambiar la posición del jugador
        targetTile.onStep(this);
        
        // victoria
        if (level.isExit(positionX, positionY)) {
            if (!level.hasUnlitTiles()) {
                status = GameStatus.WON;
            } else {
                status = GameStatus.LOST_INCOMPLETE;
            }
        }
        
        return true;
    }

    public boolean isTileLit(int x, int y) {
        return level.getCell(x, y) == '*';
    }

    public void updateWorld(double deltaTime) {
        if (status != GameStatus.PLAYING) return;

        level.updateTiles(deltaTime);

        // mirar si el jugador está en una casilla letal AHORA
        Tile currentTile = level.getTile(positionX, positionY);
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
        Tile destTile = level.getTile(x, y);
        destTile.onStep(this);
        
    }

    public GameStatus getStatus() { return status; }
    public int getPlayerX() { return positionX; }
    public int getPlayerY() { return positionY; }
    public Level getLevel() { return level; }
    public int getKeysCollected() { return keysCollected; }
}