public class StartTile extends Tile 
{

    @Override
    public boolean isWalkable(Game game) {
        return true;
    }

    @Override
    public void onStep(Game game) {
        this.isLit = true;
        game.setPlayerStartPosition(game.getPlayerX(), game.getPlayerY());
    }

    @Override
    public char getCharacter() {
        return 'S';
    }
}