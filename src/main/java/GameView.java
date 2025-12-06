import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private final int CELL_SIZE = 25; // Tamaño de cada casilla en píxeles
    private Level level;
    private int playerX;
    private int playerY;
    
    // Variables para la animación del jugador
    private boolean playerBlinkState = false; // true = amarillo, false = gris
    private long lastBlinkTime = 0;

    public GameView() {
        this.setPreferredSize(new Dimension(500, 550)); // 20x25 + espacio extra
        this.setBackground(Color.BLACK);
        this.setFocusable(true); // Necesario para recibir teclas
    }

    // Método para actualizar los datos que la vista necesita pintar
    public void updateData(Level level, int pX, int pY) {
        this.level = level;
        this.playerX = pX;
        this.playerY = pY;
        
        // Lógica de parpadeo del jugador (cada 500ms)
        if (System.currentTimeMillis() - lastBlinkTime > 500) {
            playerBlinkState = !playerBlinkState;
            lastBlinkTime = System.currentTimeMillis();
        }
        
        this.repaint(); // Solicita a Java que vuelva a pintar el panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Si no hay nivel cargado, no pintamos nada
        if (level == null) return;

        Graphics2D g2 = (Graphics2D) g;

        int size = level.getSIZE();

        // 1. PINTAR EL TABLERO (CASILLAS)
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                drawTile(g, x, y);
            }
        }

        // 2. PINTAR LOS MUROS (Encima de las casillas)
        drawWalls(g2, size);

        // 3. PINTAR AL JUGADOR
        drawPlayer(g);
    }

    private void drawWalls(Graphics2D g2, int size) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3)); // Grosor elevado para muros

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int px = x * CELL_SIZE;
                int py = y * CELL_SIZE;

                // Muro a la derecha
                if (level.hasWall(x, y, x + 1, y)) {
                    g2.drawLine(px + CELL_SIZE, py, px + CELL_SIZE, py + CELL_SIZE);
                }
                // Muro abajo
                if (level.hasWall(x, y, x, y + 1)) {
                    g2.drawLine(px, py + CELL_SIZE, px + CELL_SIZE, py + CELL_SIZE);
                }
            }
        }
    }

    private void drawTile(Graphics g, int x, int y) {
        Tile tile = level.getTile(x, y);
        char type = tile.getCharacter();
        boolean isLit = tile.isLit();

        Color color;

        // Mapeo de colores según especificación
        if (type == 'X') {
            color = Color.BLACK; // Abismo
        } else if (type == 'F' || type == 'W') { 
            // Fuego: Si es mortal (W/Activo) -> Rojo, si no -> Gris/Amarillo
            if (tile.isDeadly()) {
                color = Color.RED;
            } else {
                color = isLit ? Color.YELLOW : Color.GRAY;
            }
        } else if (type == 'K') {
            color = new Color(128, 0, 128); // Morado (Llave)
        } else if (type == 'L') {
            color = Color.BLUE; // Candado
        } else if (type == 'T') {
            color = Color.GRAY; // Teleport (Gris según instrucciones, aunque esté iluminado)
            if(isLit) color = Color.YELLOW; // (Opcional: Si quieres que se ilumine al pisar)
        } else {
            // Suelo normal, Start, End
            color = isLit ? Color.YELLOW : Color.GRAY;
        }

        // Dibujar el rectángulo base
        g.setColor(color);
        g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // Borde apenas perceptible
        g.setColor(new Color(30, 30, 30)); // Gris casi negro
        ((Graphics2D) g).setStroke(new BasicStroke(0.5f));
        g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void drawPlayer(Graphics g) {
        // Color intermitente: Gris <-> Amarillo
        Color playerColor = playerBlinkState ? Color.YELLOW : Color.GRAY;
        
        g.setColor(playerColor);
        // Dibujamos un círculo un poco más pequeño que la celda
        g.fillOval(playerX * CELL_SIZE + 5, playerY * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        
        // Borde del jugador para verlo siempre
        g.setColor(Color.BLACK);
        g.drawOval(playerX * CELL_SIZE + 5, playerY * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
    }

    // --- Métodos de interacción con el usuario (Popups) ---

    public int askForLevel() {
        String[] options = {"Nivel 1", "Nivel 2", "Nivel 3", "Nivel 4", "Nivel 5", "Nivel Test (21)"};
        int choice = JOptionPane.showOptionDialog(this, 
                "Selecciona un nivel para comenzar:", 
                "LightyRoom - Inicio",
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, options, options[0]);
        
        if (choice == 5) return 21; // Nivel test
        return choice + 1; // Map 0 -> Nivel 1
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public char askEndGameOption(boolean won) {
        String title = won ? "¡Nivel Completado!" : "Game Over";
        String msg = won ? "¿Quieres pasar al siguiente nivel?" : "¿Quieres reintentar?";
        Object[] options = won ? new Object[]{"Siguiente", "Salir"} : new Object[]{"Reintentar", "Salir"};

        int n = JOptionPane.showOptionDialog(this,
                msg,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (n == JOptionPane.YES_OPTION) {
            return won ? 's' : 'r';
        } else {
            return 'q';
        }
    }
}