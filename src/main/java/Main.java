import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game gameModel = new Game();
            GameView gameView = new GameView();
            GameController controller = new GameController(gameModel, gameView);

            controller.startGame();
        });
    }
}