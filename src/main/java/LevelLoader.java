import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LevelLoader {

    public void loadLevel(Level level, String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filePath));
        loadLevelFromLines(level, lines);
    }

    public void loadLevelFromLines(Level level, List<String> lines) {
        int y = 0;
        boolean readingWalls = false;

        for (String line : lines) {
            line = line.trim();
            
            if (line.isEmpty()) continue;
            if (line.startsWith("[source")) continue;

            if (line.startsWith("# WALLS")) {
                readingWalls = true;
                continue;
            }

            if (readingWalls) {
                processWallLine(level, line);
            } else {
                String cleanLine = line.replace(" ", "");
                processGridLine(level, y, cleanLine);
                y++;
            }
        }
    }

    private void processGridLine(Level level, int y, String line) {
        if (y >= level.getSIZE()) return;

        for (int x = 0; x < line.length(); x++) {
            if (x >= level.getSIZE()) break;
                        
            char charCode = line.charAt(x);
            level.setCell(x, y, charCode);
        }
    }

    private void processWallLine(Level level, String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length == 4) {
                int y1 = Integer.parseInt(parts[0].trim());
                int x1 = Integer.parseInt(parts[1].trim());
                int y2 = Integer.parseInt(parts[2].trim());
                int x2 = Integer.parseInt(parts[3].trim());
                
                level.addWall(x1, y1, x2, y2);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parseando muro: " + line);
        }
    }
}