public final class Level {
    private final int size = 20; // Tauler de joc de 20x20
    private final char[][] board;
    
    public Level() {
        board = new char[size][size];
        reset();
    }
    
    public char getCell(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
        throw new IndexOutOfBoundsException("Cell index out of bounds");
        }
        return board[row][col];
    }
    
    public void setCell(int row, int col, char value) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
        throw new IndexOutOfBoundsException("Cell index out of bounds");
        }
        board[row][col] = value;
    }
    
    public void reset() {
        for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            board[i][j] = 'O';
        }
        }
    }
    
    public int getSize() {
        return size;
    }
}
