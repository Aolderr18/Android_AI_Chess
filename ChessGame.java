package com.bignerdranch.android.adam_chess_app;

public class ChessGame {

    private static ChessGame single_instance = null;
    private ChessBoard board;
    private boolean active;

    private ChessGame() {
        board = new ChessBoard();
        final int turn = (int) (Math.random() * 2);
        if (turn == 1)
            board.relocate(0, 0, 0, 0);
        active = true;
    }

    public boolean move(int x, int y, int u, int v) {
        return board.move(x, y, u, v);
    }

    public ChessBoard getBoard() {
        return board;
    }

    public char gameStatus() {
        return board.playerOneInCheckMate() ? '2' : board.playerTwoInCheckMate() ? '1' : '0';
    }

    public boolean isActive() {
        return active;
    }

    public static ChessGame getInstance() {
        if (single_instance == null)
            single_instance = new ChessGame();
        else
            single_instance.getBoard().factoryReset();
        return single_instance;
    }
}
