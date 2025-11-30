// para representar el estado del juego (el normal sera "PLAYING")
// seguramente se ampliará en el futuro
public enum GameStatus {
    PLAYING,
    WON,
    LOST_OVERHEAT,
    LOST_STUCK,
    LOST_INCOMPLETE,
    LOST_FIRE
}