public abstract class Tile 
{
    protected boolean isLit = false;

    public boolean isLit() {
        return isLit;
    }

    public void setLit(boolean lit) {
        this.isLit = lit;
    }

    // Determina si el jugador puede pisar la casilla
    public abstract boolean isWalkable(Game game);

    // indica qué pasa cuando el jugador pisa esta casilla
    public abstract void onStep(Game game);

    // para representar la casilla en texto
    public abstract char getCharacter();
}