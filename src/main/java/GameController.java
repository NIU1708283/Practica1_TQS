import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController implements KeyListener {
    private final Game game;
    private final GameView view;
    private Timer gameLoopTimer;
    private int currentLevelNumber;
    private boolean isGameActive = false;

    public GameController(Game game, GameView view) {
        this.game = game;
        this.view = view;
        
        // Añadir el controlador como listener de las teclas en la vista
        this.view.addKeyListener(this);
        
        // Configurar el bucle del juego (aprox 60 FPS)
        // Cada 16ms se ejecuta el código dentro del ActionListener
        gameLoopTimer = new Timer(16, e -> updateGameLoop());
    }

    public void startGame() {
        // Crear la ventana principal (JFrame) que alojará la View
        JFrame frame = new JFrame("LightyRoom");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrar en pantalla
        frame.setVisible(true);

        // Pedir nivel inicial
        int selectedLevel = view.askForLevel();
        if (selectedLevel < 1) System.exit(0); // Si cierra el diálogo
        
        currentLevelNumber = selectedLevel;
        startLevel(currentLevelNumber);
    }

    private void startLevel(int levelNum) {
        game.loadLevel(levelNum);
        isGameActive = true;
        gameLoopTimer.start();
        view.requestFocusInWindow(); // Asegurar foco para teclado
    }

    // Este método se ejecuta 60 veces por segundo
    private void updateGameLoop() {
        if (!isGameActive) return;

        // 1. Actualizar el modelo (Fuego, etc.)
        // Pasamos el tiempo fijo del frame en segundos (0.016s)
        game.updateWorld(0.016); 

        // 2. Verificar estado del juego
        checkGameStatus();

        // 3. Actualizar la vista
        view.updateData(game.getLevel(), game.getPlayerX(), game.getPlayerY());
    }

    private void checkGameStatus() {
        GameStatus status = game.getStatus();
        
        if (status == GameStatus.PLAYING) return;

        // Si el estado no es PLAYING, detenemos el loop un momento
        gameLoopTimer.stop();
        isGameActive = false;

        // Actualizamos vista una última vez para ver la causa de muerte/victoria
        view.updateData(game.getLevel(), game.getPlayerX(), game.getPlayerY());

        if (status == GameStatus.WON) {
            handleWin();
        } else {
            handleLoss(status);
        }
    }

    private void handleWin() {
        if (currentLevelNumber >= 5 && currentLevelNumber != 21) {
            view.showMessage("¡FELICIDADES! ¡JUEGO COMPLETADO!");
            System.exit(0);
        } else {
            char choice = view.askEndGameOption(true);
            if (choice == 's') {
                currentLevelNumber++;
                startLevel(currentLevelNumber);
            } else {
                System.exit(0);
            }
        }
    }

    private void handleLoss(GameStatus status) {
        String msg = switch (status) {
            case LOST_FIRE -> "¡TE HAS QUEMADO!";
            case LOST_OVERHEAT -> "¡SOBRECALENTAMIENTO! Pisaste luz.";
            case LOST_INCOMPLETE -> "¡INCOMPLETO! Faltan luces.";
            default -> "GAME OVER";
        };
        
        view.showMessage(msg);
        char choice = view.askEndGameOption(false);
        if (choice == 'r') {
            startLevel(currentLevelNumber); // Reiniciar mismo nivel
        } else {
            System.exit(0);
        }
    }

    // --- Manejo de Teclado (KeyListener) ---

    @Override
    public void keyPressed(KeyEvent e) {
        if (!isGameActive) return;

        int key = e.getKeyCode();
        boolean moved = false;

        switch (key) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> moved = game.movePlayer(Direction.UP);
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> moved = game.movePlayer(Direction.DOWN);
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> moved = game.movePlayer(Direction.LEFT);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> moved = game.movePlayer(Direction.RIGHT);
            case KeyEvent.VK_R -> {
                startLevel(currentLevelNumber);
                return;
            }
            case KeyEvent.VK_Q -> System.exit(0);
        }
        
        // No necesitamos repintar aquí explícitamente porque el Timer lo hará en el siguiente frame
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}