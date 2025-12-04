import java.util.Scanner;

public class GameView {
    private final Scanner scanner = new Scanner(System.in); // para leer el input del usuario

    public void render(Level level, int playerX, int playerY) {
        int size = level.getSIZE();
        StringBuilder buffer = new StringBuilder();

        buffer.append("=== LIGHTY ROOM ===\n");

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (x == playerX && y == playerY) {
                    buffer.append("P "); // busca al jugador e imprime 'P'
                    continue;
                }

                // 2. Obtener el carácter base del Tile
                char cellChar = level.getCell(y, x);
                
                // 3. Traducción visual (Internal -> Visual)
                char visualChar = switch (cellChar) {
                    case 'O' -> '.'; // Suelo oscuro
                    case '*' -> '*'; // Suelo iluminado
                    case 'X' -> ' '; // Abismo
                    case 'K' -> 'K'; // Llave
                    case 'L' -> '#'; // Candado
                    case 'S' -> 'S'; // Start
                    case 'E' -> 'E'; // Exit
                    case 'W' -> '!'; // Fuego activo
                    case 'f' -> '.'; // Fuego apagado
                    default -> '?';
                };
                
                buffer.append(visualChar).append(" ");
            }
            buffer.append("\n");
        }
        
        System.out.print(buffer.toString());
    }

    public void displayMessage(String message) {
        System.out.println("\n>> " + message);
    }
    
    public char getInput() {
        System.out.print("Mover (WASD) o (Q)uit: ");
        String input = scanner.next().toLowerCase();
        if (input.isEmpty()) return ' ';
        return input.charAt(0);
    }
}