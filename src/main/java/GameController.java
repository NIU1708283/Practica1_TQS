public class GameController {
    private final Game game;
    private final GameView view;
    private boolean isRunning;
    private int currentLevelNumber = 1;

    public GameController(Game game, GameView view) {
        this.game = game;
        this.view = view;
    }

    public void startGame() {
        // para cargar el primer nivel
        game.loadLevel(currentLevelNumber);
        isRunning = true;
        gameLoop();
    }

    private void gameLoop() {
        long lastTime = System.currentTimeMillis();

        while (isRunning) {
            view.render(game.getLevel(), game.getPlayerX(), game.getPlayerY());
            
            checkGameStatus();
            if (!isRunning) break; // salir si el juego terminó (Win/Loss)

            long currentTime = System.currentTimeMillis();
            double deltaTime = (currentTime - lastTime) / 1000.0;
            lastTime = currentTime;

            game.updateWorld(deltaTime);

            if (game.getStatus() == GameStatus.LOST_FIRE) {
                view.displayMessage("¡TE HAS QUEMADO! (El fuego cambió mientras pensabas)");
                isRunning = false;
                break;
            }

            char input = view.getInput();

            processInput(input);
        }
    }

    private void processInput(char input) {
        switch (input) {
            case 'w' -> game.movePlayer(Direction.UP);
            case 's' -> game.movePlayer(Direction.DOWN);
            case 'a' -> game.movePlayer(Direction.LEFT);
            case 'd' -> game.movePlayer(Direction.RIGHT);
            case 'r' -> {
                view.displayMessage("Reiniciando nivel...");
                game.loadLevel(currentLevelNumber);
            }
            case 'q' -> {
                view.displayMessage("Saliendo del juego...");
                isRunning = false;
            }
            // Truco para esperar sin mover (pasar turno)
            case ' ' -> { /* No hacer nada, solo pasa el tiempo */ }
        }
    }

    private void checkGameStatus() {
        GameStatus status = game.getStatus();
        
        switch (status) {
            case WON -> {
                view.render(game.getLevel(), game.getPlayerX(), game.getPlayerY());
                view.displayMessage("¡NIVEL COMPLETADO! ¡ERES UN SER DE LUZ!");
                isRunning = false; 
                // Aquí podrías sumar currentLevelNumber++ y cargar el siguiente.
            }
            case LOST_OVERHEAT -> {
                view.render(game.getLevel(), game.getPlayerX(), game.getPlayerY());
                view.displayMessage("GAME OVER: ¡Has pisado una casilla ya iluminada!");
                isRunning = false;
            }
            case LOST_INCOMPLETE -> {
                view.render(game.getLevel(), game.getPlayerX(), game.getPlayerY());
                view.displayMessage("GAME OVER: Llegaste al final pero te dejaste luces apagadas.");
                isRunning = false;
            }
            case LOST_FIRE -> {
                view.render(game.getLevel(), game.getPlayerX(), game.getPlayerY());
                view.displayMessage("GAME OVER: ¡Te ha alcanzado el fuego!");
                isRunning = false;
            }
            case LOST_STUCK -> {
                 view.displayMessage("GAME OVER: ¡Te has quedado atascado!");
                 isRunning = false;
            }
        }
    }
}