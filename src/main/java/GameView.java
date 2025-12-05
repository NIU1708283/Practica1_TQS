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
                char cellChar = level.getCell(x, y);
                
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

    public int askForLevel() {
        while (true) {
            System.out.print("\nSelecciona un nivel (1-5): ");
            try {
                String input = scanner.next();
                int level = Integer.parseInt(input);
                if ((level >= 1 && level <= 5)||(level==21)) {
                    return level;
                } else {
                    System.out.println("Por favor, introduce un número entre 1 y 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }
    }

    public char askEndGameOption(boolean won) {
        System.out.println("\n--------------------------------");
        if (won) {
            System.out.print("¿Qué quieres hacer? (S)iguiente Nivel / (Q)uit: ");
        } else {
            System.out.print("¿Qué quieres hacer? (R)eintentar / (Q)uit: ");
        }
        
        while (true) {
            String input = scanner.next().toLowerCase();
            char option = input.charAt(0);
            if (option == 's' || option == 'r' || option == 'q') {
                return option;
            }
            System.out.print("Opción no válida. Inténtalo de nuevo: ");
        }
    }
}