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
        } else {
            game.setStatus(GameStatus.LOST_INCOMPLETE);
        }
    }

    @Override
    public char getCharacter() {
        return 'E';
    }
    
}
