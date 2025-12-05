public class GameController {
    private final Game game;
    private final GameView view;
    private boolean isLevelRunning;
    private int currentLevelNumber;

    public GameController(Game game, GameView view) {
        this.game = game;
        this.view = view;
    }

    public void startGame() {
        currentLevelNumber = view.askForLevel();
        
        boolean appRunning = true;

        while (appRunning) {
            playLevel(currentLevelNumber);

            GameStatus status = game.getStatus();
            if (status == GameStatus.WON) {
                if (currentLevelNumber >= 5) {
                    view.displayMessage("¡FELICIDADES! ¡HAS COMPLETADO TODOS LOS NIVELES!");
                    appRunning = false;
                } else {
                    char choice = view.askEndGameOption(true);
                    if (choice == 's') {
                        currentLevelNumber++; // si el jugador ha ganado y quiere seguir, avanza al siguiente nivel
                    } else {
                        appRunning = false; // sino, se sale
                    }
                }
            } else { // si ha perdido
                char choice = view.askEndGameOption(false);
                if (choice == 'r') {
                } else {
                    appRunning = false;
                }
            }
        }
        
        view.displayMessage("¡Gracias por jugar a LightyRoom!");
    }

    private void playLevel(int levelNum) {
        game.loadLevel(levelNum);
        isLevelRunning = true;
        
        long lastTime = System.currentTimeMillis();

        while (isLevelRunning) {
            view.render(game.getLevel(), game.getPlayerX(), game.getPlayerY());
            
            checkGameStatus(); 
            if (!isLevelRunning) break; 

            long currentTime = System.currentTimeMillis();
            double deltaTime = (currentTime - lastTime) / 1000.0;
            lastTime = currentTime;

            game.updateWorld(deltaTime);

            // control especial para muerte por tiempo (fuego)
            if (game.getStatus() == GameStatus.LOST_FIRE) {
                view.render(game.getLevel(), game.getPlayerX(), game.getPlayerY());
                view.displayMessage("¡TE HAS QUEMADO! (El fuego cambió mientras pensabas)");
                isLevelRunning = false;
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
                view.displayMessage("Saliendo al menú...");
                game.setStatus(GameStatus.LOST_INCOMPLETE);
                isLevelRunning = false;
            }
            case ' ' -> { /* por ahora se espera, más adelante se eliminará */ }
        }
    }

    private void checkGameStatus() {
        GameStatus status = game.getStatus();
        
        switch (status) {
            case WON -> {
                view.render(game.getLevel(), game.getPlayerX(), game.getPlayerY());
                view.displayMessage("¡NIVEL " + currentLevelNumber + " COMPLETADO!");
                isLevelRunning = false; 
            }
            case LOST_OVERHEAT -> {
                view.render(game.getLevel(), game.getPlayerX(), game.getPlayerY());
                view.displayMessage("GAME OVER: ¡Has pisado una casilla ya iluminada!");
                isLevelRunning = false;
            }
            case LOST_INCOMPLETE -> {
                if (isLevelRunning) { 
                    view.render(game.getLevel(), game.getPlayerX(), game.getPlayerY());
                    view.displayMessage("GAME OVER: Llegaste al final pero te dejaste luces apagadas.");
                    isLevelRunning = false;
                }
            }
            // LOST_FIRE ya se maneja en el loop principal
        }
    }
}