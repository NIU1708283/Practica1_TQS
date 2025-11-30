public class FloorTile extends Tile 
{
    @Override
    public boolean isWalkable(Game game) {
        return true; // siempre se puede pisar
    }

    @Override
    public void onStep(Game game) {
        this.isLit = true; // se ilumina al pisar
    }

    @Override
    public char getCharacter() {
        return isLit ? '*' : 'O';
    }
}