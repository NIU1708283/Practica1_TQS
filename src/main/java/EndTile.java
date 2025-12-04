public class EndTile extends Tile 
{
    @Override
    public boolean isWalkable(Game game) {
        return true;
    }

    @Override
    public void onStep(Game game) {
        this.isLit = true;
        if (!game.getLevel().hasUnlitTiles()) {
            game.setStatus(GameStatus.WON);
        }
    }

    @Override
    public char getCharacter() {
        return 'E';
    }
    
}
