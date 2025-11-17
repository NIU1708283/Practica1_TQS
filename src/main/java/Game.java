public class Game {
    private int positionX;
    private int positionY;

    public void startNewGame() {
      positionX = 0;
      positionY = 0;

    }
    public void setPlayerPosition(int x, int y) {
        // Set the player's starting position
        positionX = x;
        positionY = y;
    }
    public boolean movePlayer(Direction direction) {
        // Logic to move the player in the specified direction
        switch (direction) {
            case UP -> positionY -= 1;
            case DOWN -> positionY += 1;
            case LEFT -> positionX -= 1;
            case RIGHT -> positionX += 1;
        }
        return true; // Assume the move is always successful
    }
    public int getPlayerX() {
        // Return the player's current X position
        return positionX;
    }
    public int getPlayerY() {
        // Return the player's current Y position
        return positionY;
    }

}
