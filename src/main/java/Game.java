public class Game 
{
    private int positionX;
    private int positionY;
    private Level level; // Añadimos la referencia al nivel (tablero del juego, q va del 1 al 5)
    private GameStatus status; // Nuevo campo para el estado del juego

    public void startNewGame() {
        positionX = 0;
        positionY = 0;
        level = new Level(); // Inicializamos el tablero
        status = GameStatus.PLAYING; // Estado inicial
    }

    public void setPlayerPosition(int x, int y) {
        positionX = x;
        positionY = y;
        level.setCell(positionX, positionY, '*'); // iluminar la casilla donde se coloca el jugador
    }

    public boolean movePlayer(Direction direction) {
        // calculamos la posible nueva posición 
        int nextX = positionX;
        int nextY = positionY;

        switch (direction) {
            case UP -> nextY -= 1;
            case DOWN -> nextY += 1;
            case LEFT -> nextX -= 1;
            case RIGHT -> nextX += 1;
        }
        // comprobamos si hay muro entre la posición actual y la siguiente
        if (level.hasWall(positionX, positionY, nextX, nextY)) {
            return false; // movimiento bloqueado por muro
        }
        // comprobamos límites del tablero
        if (nextX < 0 || nextX >= level.getSize() || nextY < 0 || nextY >= level.getSize()) {
            return false; // no se puede (choca con borde del mapa)
        }

        // comprobamos si la casilla YA está iluminada 
        if (level.getCell(nextX, nextY) == '*') {
            status = GameStatus.LOST_OVERHEAT; // ¡Game Over!
            // movemos al jugador a la casilla de muerte
            positionX = nextX;
            positionY = nextY;
            return true;
            // se añadirá cierto delay para q el jugador vea donde ha muerto
        }

        // si está todo bien, movemos y actualizamos
        positionX = nextX;
        positionY = nextY;
        level.setCell(positionX, positionY, '*');
        
        // comprobar condición de Victoria o Derrota incompleta
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

    public GameStatus getStatus() {
        return status;
    }

    // Getters
    public int getPlayerX() { return positionX; }
    public int getPlayerY() { return positionY; }
    public Level getLevel() { return level; }
}