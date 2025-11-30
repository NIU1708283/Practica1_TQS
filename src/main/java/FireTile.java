public class FireTile extends Tile {
    private double timeAccumulator = 0;
    private boolean isActive = false; // false = Apagado (Seguro), true = Fuego (Letal)
    private final double TOGGLE_TIME = 2.0;

    @Override
    public boolean isWalkable(Game game) { return !isActive; }

    @Override
    public void onStep(Game game) { this.isLit = true; }

    @Override
    public char getCharacter() {
        if (isLit) return '*';
        return isActive ? 'W' : 'f'; // 'W' para fuego activo, 'f' para fuego inactivo
    }

    @Override
    public void update(double deltaTime) {
        timeAccumulator += deltaTime;
        if (timeAccumulator >= TOGGLE_TIME) {
            timeAccumulator -= TOGGLE_TIME;
            isActive = !isActive;
        }
    }

    @Override
    public boolean isDeadly() {
        return isActive;
    }
}