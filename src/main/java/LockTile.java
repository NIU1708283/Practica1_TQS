public class LockTile extends Tile 
{
    private boolean isOpen = false;

    @Override
    public boolean isWalkable(Game game) {
        // si ya está abierto, se puede pasar. Si no, necesita llave
        return isOpen || game.getKeysCollected() > 0;
    }

    @Override
    public void onStep(Game game) {
        if (!isOpen) {
            game.useKey(); // gastamos la llave
            isOpen = true;
        }
        this.isLit = true;
    }

    @Override
    public char getCharacter() {
        if (isLit) return '*';
        return isOpen ? 'O' : 'L';
    }
}