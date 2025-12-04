public abstract class Tile 
{
    protected boolean isLit = false;

    public boolean isLit() { return isLit; }
    public void setLit(boolean lit) { this.isLit = lit; }

    public abstract boolean isWalkable(Game game);
    public abstract void onStep(Game game);
    public abstract char getCharacter();

    // actualiza el estado de la casilla, por defecto no hace nada
    public void update(double deltaTime) {} 

    // mata al jugador si esta encima, por defecto no es mortal
    public boolean isDeadly() { return false; }

    public boolean requiresLight() { return true; } // por defecto, las casillas requieren estar iluminadas para WIN
}