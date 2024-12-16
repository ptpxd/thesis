package console;

public class BoardModel {

    private int[][] board;

    public BoardModel(int rows, int columns) {
        board = new int[rows][columns];
    }

    public boolean isOnBoard(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    public boolean setElement(int row, int col, int value) {
        if (isOnBoard(row, col) && (value == 0 || value == 1)) {
            board[row][col] = value;
            return true;
        }
        return false;
    }

    public int[][] getBoard() {
        return board;
    }
}