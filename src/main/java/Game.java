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

        // Game Over x Sobrecalentamiento
        if (targetTile.isLit()) {
            status = GameStatus.LOST_OVERHEAT;
            positionX = nextX;
            positionY = nextY;
            return true; // movimiento permitido, pero juego terminado
        }
        // comprueba si se puede pasar
        if (!targetTile.isWalkable(this)) {
            return false;
        }
        // activa los efectos de la casilla
        targetTile.onStep(this);

        // Actualizar posición
        positionX = nextX;
        positionY = nextY;

        // Victoria??
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

    public GameStatus getStatus() { return status; }
    public int getPlayerX() { return positionX; }
    public int getPlayerY() { return positionY; }
    public Level getLevel() { return level; }
    public int getKeysCollected() { return keysCollected; }
}