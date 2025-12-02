public class TeleportTile extends Tile 
{
    private final int destX;
    private final int destY;

    public TeleportTile(int destX, int destY) {
        this.destX = destX;
        this.destY = destY;
    }

    @Override
    public boolean isWalkable(Game game) {
        return true;
    }

    @Override
    public void onStep(Game game) {
        this.isLit = true;
        game.teleportTo(destX, destY);
    }

    @Override
    public char getCharacter() {
        return isLit ? '*' : 'T';
    }
}