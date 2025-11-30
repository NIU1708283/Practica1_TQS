public class KeyTile extends Tile 
{
    private boolean collected = false;

    @Override
    public boolean isWalkable(Game game) {
        return true;
    }

    @Override
    public void onStep(Game game) {
        if (!collected) {
            game.addKey(); 
            collected = true;
        }
        this.isLit = true;
    }

    @Override
    public char getCharacter() {
        if (isLit) 
            return '*';
        return collected ? 'O' : 'K';
    }
}