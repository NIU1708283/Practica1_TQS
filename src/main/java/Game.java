public class Game 
{
    private int positionX;
    private int positionY;
    private Level level; // Añadimos la referencia al nivel (tablero del juego, q va del 1 al 5)

    public void startNewGame() {
        positionX = 0;
        positionY = 0;
        level = new Level(); // Inicializamos el tablero
        level.setCell(positionX, positionY, '*'); // iluminar la casilla inicial
    }

    public void setPlayerPosition(int x, int y) {
        positionX = x;
        positionY = y;
        level.setCell(positionX, positionY, '*'); // iluminar la casilla donde se coloca el jugador
    }

    public boolean movePlayer(Direction direction) {
        switch (direction) {
            case UP -> positionY -= 1;
            case DOWN -> positionY += 1;
            case LEFT -> positionX -= 1;
            case RIGHT -> positionX += 1;
        }
        
        // Lógica de Iluminación:
        // Marcamos la casilla actual en el nivel con '*' (representación de luz)
        try {
            level.setCell(positionX, positionY, '*');
        } catch (IndexOutOfBoundsException e) {
            // por si nos salimos del mapa, por ahora pasamos y simplemente devolvemos false
            // (se tratará en futuros tests de límites/muros)
            return false;
        }

        return true; 
    }

    // Método auxiliar para el test y la vista
    public boolean isTileLit(int x, int y) {
        return level.getCell(x, y) == '*';
    }

    public int getPlayerX() {
        return positionX;
    }

    public int getPlayerY() {
        return positionY;
    }
    
    // Getter útil para tests futuros
    public Level getLevel() {
        return level;
    }
}