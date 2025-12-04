public class AbbysTile extends Tile 
{
    @Override
    public boolean isWalkable(Game game) {
        return false; // nunca se puede pisar
    }
    @Override
    public void onStep(Game game) {
        // No hace nada, no se puede pisar
    }
    @Override
    public char getCharacter() {
        return 'X';
    }

    @Override
    public boolean requiresLight() {
        return false; // abismos no cuentan para la condición de victoria
    }
}
