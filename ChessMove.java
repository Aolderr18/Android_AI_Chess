package com.bignerdranch.android.adam_chess_app;

public class ChessMove extends ChessBoard {

    private ChessBoard board;
    private final int x;
    private final int y;
    private final int u;
    private final int v;

    public ChessMove(ChessBoard board, int x, int y, int u, int v) throws InvalidChessMoveException {
        this.board = board;
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        if (!chessMoveRulesFollowed())
            throw new InvalidChessMoveException();
        ChessBoard afterMove = ChessBoard.duplicate(board);
        afterMove.relocate(x, y, u, v);
        final int chessBoardSize = ChessBoard.chessBoardSize;
        boolean inCheckAfterMove = false;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        String playerOnePiecesCodes = ChessBoard.playerOnePieceCodes;
        String playerTwoPieceCodes = ChessBoard.playerTwoPieceCodes;
        int r, l;
        final char[][] afterPieceGrid = afterMove.getPieceGrid();
        for (int a = 0; a < chessBoardSize; a++)
            for (int b = 0; b < chessBoardSize; b++) {
                if (board.isPlayerOneTurn()) {
                    if (playerOnePiecesCodes.indexOf(afterPieceGrid[a][b]) > -1)
                        continue;
                } else if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][b]) > -1)
                    continue;
                try {
                    switch (afterPieceGrid[a][b]) {
                        case 'P':
                            if (a + 1 < chessBoardSize && b - 1 >= 0 && playerTwoPieceCodes.indexOf(afterPieceGrid[a + 1][b - 1]) > -1)
                                try {
                                    new ChessMove(afterMove, a, b, a + 1, b - 1, 0);
                                    if (afterPieceGrid[a + 1][b - 1] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            if (a - 1 >= 0 && b - 1 >= 0 && playerTwoPieceCodes.indexOf(afterPieceGrid[a - 1][b - 1]) > -1)
                                try {
                                    new ChessMove(afterMove, a, b, a - 1, b - 1, 0);
                                    if (afterPieceGrid[a - 1][b - 1] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            break;
                        case 'R':
                            r = a;
                            while (r < chessBoardSize - 1) {
                                r++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            r = a;
                            while (r > 0) {
                                r--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            l = b;
                            while (l < chessBoardSize - 1) {
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            l = b;
                            while (l > 0) {
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            break;
                        case 'N':
                            for (r = 0; r < 4; r++)
                                for (l = 0; l < 4; l++) {
                                    if (Math.abs(knightMoves[r] * knightMoves[l]) != 2 || a + knightMoves[r] < 0 ||
                                            a + knightMoves[r] >= chessBoardSize || b + knightMoves[l] < 0 ||
                                            b + knightMoves[l] >= chessBoardSize)
                                        continue;
                                    if (playerOnePieceCodes.indexOf(afterPieceGrid[a + knightMoves[r]][b + knightMoves[l]]) > -1)
                                        continue;
                                    try {
                                        new ChessMove(afterMove, a, b, a + knightMoves[r], b + knightMoves[l], 0);
                                        if (afterPieceGrid[a + knightMoves[r]][b + knightMoves[l]] == 'k') {
                                            inCheckAfterMove = true;
                                            a = b = chessBoardSize;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                                }
                            break;
                        case 'B':
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                                r++;
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l > 0) {
                                r++;
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l < chessBoardSize - 1) {
                                r--;
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l > 0) {
                                r--;
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            break;
                        case 'Q':
                            r = a;
                            while (r < chessBoardSize - 1) {
                                r++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            r = a;
                            while (r > 0) {
                                r--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            l = b;
                            while (l < chessBoardSize - 1) {
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            l = b;
                            while (l > 0) {
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                                r++;
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l > 0) {
                                r++;
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l < chessBoardSize - 1) {
                                r--;
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l > 0) {
                                r--;
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            break;
                        case 'K':
                            for (r = 0; r < 3; r++)
                                for (l = 0; l < 3; l++) {
                                    if ((kingMoves[r] == 0 && kingMoves[l] == 0) || a + kingMoves[r] < 0 ||
                                            a + kingMoves[r] >= chessBoardSize || b + kingMoves[l] < 0 ||
                                            b + kingMoves[l] >= chessBoardSize)
                                        continue;
                                    if (playerOnePieceCodes.indexOf(afterPieceGrid[a + kingMoves[r]][b + kingMoves[l]]) > -1)
                                        continue;
                                    try {
                                        new ChessMove(afterMove, a, b, a + kingMoves[r], b + kingMoves[l], 0);
                                        if (afterPieceGrid[a + kingMoves[r]][b + kingMoves[l]] == 'k') {
                                            inCheckAfterMove = true;
                                            a = b = chessBoardSize;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                                }
                            break;
                        case 'p':
                            if (a + 1 < chessBoardSize && b + 1 < chessBoardSize && playerOnePieceCodes.indexOf(afterPieceGrid[a + 1][b + 1]) > -1)
                                try {
                                    new ChessMove(afterMove, a, b, a + 1, b + 1, 0);
                                    if (afterPieceGrid[a + 1][b + 1] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            if (a - 1 >= 0 && b + 1 < chessBoardSize && playerOnePieceCodes.indexOf(afterPieceGrid[a - 1][b + 1]) > -1)
                                try {
                                    new ChessMove(afterMove, a, b, a - 1, b + 1, 0);
                                    if (afterPieceGrid[a - 1][b + 1] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            break;
                        case 'r':
                            r = a;
                            while (r < chessBoardSize - 1) {
                                r++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            r = a;
                            while (r > 0) {
                                r--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            l = b;
                            while (l < chessBoardSize - 1) {
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            l = b;
                            while (l > 0) {
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            break;
                        case 'n':
                            for (r = 0; r < 4; r++)
                                for (l = 0; l < 4; l++) {
                                    if (Math.abs(knightMoves[r] * knightMoves[l]) != 2 || a + knightMoves[r] < 0 ||
                                            a + knightMoves[r] >= chessBoardSize || b + knightMoves[l] < 0 ||
                                            b + knightMoves[l] >= chessBoardSize)
                                        continue;
                                    if (playerTwoPieceCodes.indexOf(afterPieceGrid[a + knightMoves[r]][b + knightMoves[l]]) > -1)
                                        continue;
                                    try {
                                        new ChessMove(afterMove, a, b, a + knightMoves[r], b + knightMoves[l], 0);
                                        if (afterPieceGrid[a + knightMoves[r]][b + knightMoves[l]] == 'K') {
                                            inCheckAfterMove = true;
                                            a = b = chessBoardSize;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                                }
                            break;
                        case 'b':
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                                r++;
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l > 0) {
                                r++;
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l < chessBoardSize - 1) {
                                r--;
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l > 0) {
                                r--;
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            break;
                        case 'q':
                            r = a;
                            while (r < chessBoardSize - 1) {
                                r++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            r = a;
                            while (r > 0) {
                                r--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            l = b;
                            while (l < chessBoardSize - 1) {
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            l = b;
                            while (l > 0) {
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                                r++;
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l > 0) {
                                r++;
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l < chessBoardSize - 1) {
                                r--;
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l > 0) {
                                r--;
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            break;
                        case 'k':
                            for (r = 0; r < 3; r++)
                                for (l = 0; l < 3; l++) {
                                    if ((kingMoves[r] == 0 && kingMoves[l] == 0) || a + kingMoves[r] < 0 ||
                                            a + kingMoves[r] >= chessBoardSize || b + kingMoves[l] < 0 ||
                                            b + kingMoves[l] >= chessBoardSize)
                                        continue;
                                    if (playerTwoPieceCodes.indexOf(afterPieceGrid[a + kingMoves[r]][b + kingMoves[l]]) > -1)
                                        continue;
                                    try {
                                        new ChessMove(afterMove, a, b, a + kingMoves[r], b + kingMoves[l], 0);
                                        if (afterPieceGrid[a + kingMoves[r]][b + kingMoves[l]] == 'K') {
                                            inCheckAfterMove = true;
                                            a = b = chessBoardSize;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                                }
                    }
                } catch (ArrayIndexOutOfBoundsException aioobexc) {

                }
            }
        if (inCheckAfterMove)
            throw new InvalidChessMoveException();
    }

    protected ChessMove(ChessBoard board, int x, int y, int u, int v, int unused0, int unused1) throws InvalidChessMoveException {
        this.board = board;
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        ChessBoard afterMove = ChessBoard.duplicate(board);
        afterMove.relocate(x, y, u, v);
        boolean inCheckAfterMove = false;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        String playerOnePieceCodes = ChessBoard.playerOnePieceCodes;
        String playerTwoPieceCodes = ChessBoard.playerTwoPieceCodes;
        int r, l;
        final char[][] afterPieceGrid = afterMove.getPieceGrid();
        for (int a = 0; a < chessBoardSize; a++)
            for (int b = 0; b < chessBoardSize; b++) {
                if (board.isPlayerOneTurn()) {
                    if (playerOnePieceCodes.indexOf(afterPieceGrid[a][b]) > -1)
                        continue;
                } else if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][b]) > -1)
                    continue;
                try {
                    switch (afterPieceGrid[a][b]) {
                        case 'P':
                            if (a + 1 < chessBoardSize && b - 1 >= 0 && playerTwoPieceCodes.indexOf(afterPieceGrid[a + 1][b - 1]) > -1)
                                try {
                                    new ChessMove(afterMove, a, b, a + 1, b - 1, 0);
                                    if (afterPieceGrid[a + 1][b - 1] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            if (a - 1 >= 0 && b - 1 >= 0 && playerTwoPieceCodes.indexOf(afterPieceGrid[a - 1][b - 1]) > -1)
                                try {
                                    new ChessMove(afterMove, a, b, a - 1, b - 1, 0);
                                    if (afterPieceGrid[a - 1][b - 1] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            break;
                        case 'R':
                            r = a;
                            while (r < chessBoardSize - 1) {
                                r++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            r = a;
                            while (r > 0) {
                                r--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            l = b;
                            while (l < chessBoardSize - 1) {
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            l = b;
                            while (l > 0) {
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            break;
                        case 'N':
                            for (r = 0; r < 4; r++)
                                for (l = 0; l < 4; l++) {
                                    if (Math.abs(knightMoves[r] * knightMoves[l]) != 2 || a + knightMoves[r] < 0 ||
                                            a + knightMoves[r] >= chessBoardSize || b + knightMoves[l] < 0 ||
                                            b + knightMoves[l] >= chessBoardSize)
                                        continue;
                                    if (playerOnePieceCodes.indexOf(afterPieceGrid[a + knightMoves[r]][b + knightMoves[l]]) > -1)
                                        continue;
                                    try {
                                        new ChessMove(afterMove, a, b, a + knightMoves[r], b + knightMoves[l], 0);
                                        if (afterPieceGrid[a + knightMoves[r]][b + knightMoves[l]] == 'k') {
                                            inCheckAfterMove = true;
                                            a = b = chessBoardSize;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                                }
                            break;
                        case 'B':
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                                r++;
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l > 0) {
                                r++;
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l < chessBoardSize - 1) {
                                r--;
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l > 0) {
                                r--;
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            break;
                        case 'Q':
                            r = a;
                            while (r < chessBoardSize - 1) {
                                r++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            r = a;
                            while (r > 0) {
                                r--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            l = b;
                            while (l < chessBoardSize - 1) {
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            l = b;
                            while (l > 0) {
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                                r++;
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l > 0) {
                                r++;
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l < chessBoardSize - 1) {
                                r--;
                                l++;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l > 0) {
                                r--;
                                l--;
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'k') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            break;
                        case 'K':
                            for (r = 0; r < 3; r++)
                                for (l = 0; l < 3; l++) {
                                    if ((kingMoves[r] == 0 && kingMoves[l] == 0) || a + kingMoves[r] < 0 ||
                                            a + kingMoves[r] >= chessBoardSize || b + kingMoves[l] < 0 ||
                                            b + kingMoves[l] >= chessBoardSize)
                                        continue;
                                    if (playerOnePieceCodes.indexOf(afterPieceGrid[a + kingMoves[r]][b + kingMoves[l]]) > -1)
                                        continue;
                                    try {
                                        new ChessMove(afterMove, a, b, a + kingMoves[r], b + kingMoves[l], 0);
                                        if (afterPieceGrid[a + kingMoves[r]][b + kingMoves[l]] == 'k') {
                                            inCheckAfterMove = true;
                                            a = b = chessBoardSize;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                                }
                            break;
                        case 'p':
                            if (a + 1 < chessBoardSize && b + 1 < chessBoardSize && playerOnePieceCodes.indexOf(afterPieceGrid[a + 1][b + 1]) > -1)
                                try {
                                    new ChessMove(afterMove, a, b, a + 1, b + 1, 0);
                                    if (afterPieceGrid[a + 1][b + 1] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            if (a - 1 >= 0 && b + 1 < chessBoardSize && playerOnePieceCodes.indexOf(afterPieceGrid[a - 1][b + 1]) > -1)
                                try {
                                    new ChessMove(afterMove, a, b, a - 1, b + 1, 0);
                                    if (afterPieceGrid[a - 1][b + 1] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            break;
                        case 'r':
                            r = a;
                            while (r < chessBoardSize - 1) {
                                r++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            r = a;
                            while (r > 0) {
                                r--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            l = b;
                            while (l < chessBoardSize - 1) {
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            l = b;
                            while (l > 0) {
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            break;
                        case 'n':
                            for (r = 0; r < 4; r++)
                                for (l = 0; l < 4; l++) {
                                    if (Math.abs(knightMoves[r] * knightMoves[l]) != 2 || a + knightMoves[r] < 0 ||
                                            a + knightMoves[r] >= chessBoardSize || b + knightMoves[l] < 0 ||
                                            b + knightMoves[l] >= chessBoardSize)
                                        continue;
                                    if (playerTwoPieceCodes.indexOf(afterPieceGrid[a + knightMoves[r]][b + knightMoves[l]]) > -1)
                                        continue;
                                    try {
                                        new ChessMove(afterMove, a, b, a + knightMoves[r], b + knightMoves[l], 0);
                                        if (afterPieceGrid[a + knightMoves[r]][b + knightMoves[l]] == 'K') {
                                            inCheckAfterMove = true;
                                            a = b = chessBoardSize;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                                }
                            break;
                        case 'b':
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                                r++;
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l > 0) {
                                r++;
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l < chessBoardSize - 1) {
                                r--;
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l > 0) {
                                r--;
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            break;
                        case 'q':
                            r = a;
                            while (r < chessBoardSize - 1) {
                                r++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            r = a;
                            while (r > 0) {
                                r--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, b, 0);
                                    if (afterPieceGrid[r][b] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][b]) > -1)
                                    break;
                            }
                            l = b;
                            while (l < chessBoardSize - 1) {
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            l = b;
                            while (l > 0) {
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, a, l, 0);
                                    if (afterPieceGrid[a][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[a][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                                r++;
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r < chessBoardSize - 1 && l > 0) {
                                r++;
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l < chessBoardSize - 1) {
                                r--;
                                l++;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            r = a;
                            l = b;
                            while (r > 0 && l > 0) {
                                r--;
                                l--;
                                if (playerTwoPieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                                try {
                                    new ChessMove(afterMove, a, b, r, l, 0);
                                    if (afterPieceGrid[r][l] == 'K') {
                                        inCheckAfterMove = true;
                                        a = b = chessBoardSize;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (playerOnePieceCodes.indexOf(afterPieceGrid[r][l]) > -1)
                                    break;
                            }
                            break;
                        case 'k':
                            for (r = 0; r < 3; r++)
                                for (l = 0; l < 3; l++) {
                                    if ((kingMoves[r] == 0 && kingMoves[l] == 0) || a + kingMoves[r] < 0 ||
                                            a + kingMoves[r] >= chessBoardSize || b + kingMoves[l] < 0 ||
                                            b + kingMoves[l] >= chessBoardSize)
                                        continue;
                                    if (playerTwoPieceCodes.indexOf(afterPieceGrid[a + kingMoves[r]][b + kingMoves[l]]) > -1)
                                        continue;
                                    try {
                                        new ChessMove(afterMove, a, b, a + kingMoves[r], b + kingMoves[l], 0);
                                        if (afterPieceGrid[a + kingMoves[r]][b + kingMoves[l]] == 'K') {
                                            inCheckAfterMove = true;
                                            a = b = chessBoardSize;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                                }
                    }
                } catch (ArrayIndexOutOfBoundsException aioobexc) {

                }
            }
        if (inCheckAfterMove)
            throw new InvalidChessMoveException();
    }

    protected ChessMove() {
        x = y = u = v = 0;
    }

    private ChessMove(ChessBoard board, int x, int y, int u, int v, int unused) throws InvalidChessMoveException {
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
    }

    private boolean chessMoveRulesFollowed() {
        if (x < 0 || x >= chessBoardSize || y < 0 || y >= chessBoardSize || u < 0 || u >= chessBoardSize || v < 0 || v >= chessBoardSize)
            return false;
        final int chessBoardSize = ChessBoard.chessBoardSize;
        boolean[][] accessible = new boolean[chessBoardSize][chessBoardSize];
        int i, j;
        for (i = 0; i < chessBoardSize; i++)
            for (j = 0; j < chessBoardSize; j++)
                accessible[i][j] = false;
        final char[][] pieceGrid = board.getPieceGrid();
        final String playerOnePieceCodes = ChessBoard.playerOnePieceCodes;
        final String playerTwoPieceCodes = ChessBoard.playerTwoPieceCodes;
        if (board.isPlayerOneTurn()) {
            if (playerOnePieceCodes.indexOf(pieceGrid[x][y]) == -1)
                return false;
        } else if (playerTwoPieceCodes.indexOf(pieceGrid[x][y]) == -1)
            return false;
        final String allPieceCodes = ChessBoard.allPieceCodes;
        final char selectionCode = pieceGrid[x][y];
        final char destinationCode = pieceGrid[u][v];
        int r, l;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        switch (selectionCode) {
            case 'P':
                if (playerTwoPieceCodes.indexOf(destinationCode) > -1) {
                    if (x > 0 && y > 0)
                        accessible[x - 1][y - 1] = destinationCode == pieceGrid[x - 1][y - 1];
                    if (x < chessBoardSize - 1 && y > 0)
                        accessible[x + 1][y - 1] = destinationCode == pieceGrid[x + 1][y - 1];
                } else {
                    if (y == 6) {
                        accessible[x][y - 1] = allPieceCodes.indexOf(destinationCode) == -1;
                        accessible[x][y - 2] = allPieceCodes.indexOf(destinationCode) == -1 && accessible[x][y - 1];
                    } else if (y > 0) {
                        accessible[x][y - 1] = allPieceCodes.indexOf(destinationCode) == -1;
                    }
                }
                break;
            case 'p':
                if (playerOnePieceCodes.indexOf(destinationCode) > -1) {
                    if (x > 0 && y < chessBoardSize - 1)
                        accessible[x - 1][y + 1] = destinationCode == pieceGrid[x - 1][y + 1];
                    if (x < chessBoardSize - 1 && y < chessBoardSize - 1)
                        accessible[x + 1][y + 1] = destinationCode == pieceGrid[x + 1][y + 1];
                } else {
                    if (y == 1) {
                        accessible[x][y + 1] = allPieceCodes.indexOf(destinationCode) == -1;
                        accessible[x][y + 2] = allPieceCodes.indexOf(destinationCode) == -1 && accessible[x][y + 1];
                    } else if (y < chessBoardSize - 1) {
                        accessible[x][y + 1] = allPieceCodes.indexOf(destinationCode) == -1;
                    }
                }
                break;
            case 'R':
                r = x;
                while (r < chessBoardSize - 1) {
                    r++;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                    accessible[r][y] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                }
                r = x;
                while (r > 0) {
                    r--;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                    accessible[r][y] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                }
                l = y;
                while (l < chessBoardSize - 1) {
                    l++;
                    if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                    accessible[x][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                }
                l = y;
                while (l > 0) {
                    l--;
                    if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                    accessible[x][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                }
                break;
            case 'r':
                r = x;
                while (r < chessBoardSize - 1) {
                    r++;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                    accessible[r][y] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                }
                r = x;
                while (r > 0) {
                    r--;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                    accessible[r][y] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                }
                l = y;
                while (l < chessBoardSize - 1) {
                    l++;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                    accessible[x][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                }
                l = y;
                while (l > 0) {
                    l--;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                    accessible[x][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                }
                break;
            case 'N':
                for (r = 0; r < 4; r++)
                    for (l = 0; l < 4; l++) {
                        if (Math.abs(knightMoves[r] * knightMoves[l]) != 2)
                            continue;
                        try {
                            accessible[x + knightMoves[r]][y + knightMoves[l]] =
                                    playerOnePieceCodes.indexOf(pieceGrid[x + knightMoves[r]][y + knightMoves[l]]) == -1;
                        } catch (ArrayIndexOutOfBoundsException aioobexc) {

                        }
                    }
                break;
            case 'n':
                for (r = 0; r < 4; r++)
                    for (l = 0; l < 4; l++) {
                        if (Math.abs(knightMoves[r] * knightMoves[l]) != 2)
                            continue;
                        try {
                            accessible[x + knightMoves[r]][y + knightMoves[l]] =
                                    playerTwoPieceCodes.indexOf(pieceGrid[x + knightMoves[r]][y + knightMoves[l]]) == -1;
                        } catch (ArrayIndexOutOfBoundsException aioobexc) {

                        }
                    }
                break;
            case 'B':
                r = x;
                l = y;
                while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                    r++;
                    l++;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r < chessBoardSize - 1 && l > 0) {
                    r++;
                    l--;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r > 0 && l < chessBoardSize - 1) {
                    r--;
                    l++;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r > 0 && l > 0) {
                    r--;
                    l--;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                break;
            case 'b':
                r = x;
                l = y;
                while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                    r++;
                    l++;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r < chessBoardSize - 1 && l > 0) {
                    r++;
                    l--;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r > 0 && l < chessBoardSize - 1) {
                    r--;
                    l++;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r > 0 && l > 0) {
                    r--;
                    l--;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                break;
            case 'Q':
                r = x;
                while (r < chessBoardSize - 1) {
                    r++;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                    accessible[r][y] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                }
                r = x;
                while (r > 0) {
                    r--;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                    accessible[r][y] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                }
                l = y;
                while (l < chessBoardSize - 1) {
                    l++;
                    if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                    accessible[x][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                }
                l = y;
                while (l > 0) {
                    l--;
                    if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                    accessible[x][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                    r++;
                    l++;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r < chessBoardSize - 1 && l > 0) {
                    r++;
                    l--;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r > 0 && l < chessBoardSize - 1) {
                    r--;
                    l++;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r > 0 && l > 0) {
                    r--;
                    l--;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                break;
            case 'q':
                r = x;
                while (r < chessBoardSize - 1) {
                    r++;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                    accessible[r][y] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                }
                r = x;
                while (r > 0) {
                    r--;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                    accessible[r][y] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                        break;
                }
                l = y;
                while (l < chessBoardSize - 1) {
                    l++;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                    accessible[x][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                }
                l = y;
                while (l > 0) {
                    l--;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                    accessible[x][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r < chessBoardSize - 1&& l < chessBoardSize - 1) {
                    r++;
                    l++;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r < chessBoardSize - 1 && l > 0) {
                    r++;
                    l--;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r > 0 && l < chessBoardSize - 1) {
                    r--;
                    l++;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                r = x;
                l = y;
                while (r > 0 && l > 0) {
                    r--;
                    l--;
                    if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                    accessible[r][l] = true;
                    if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                        break;
                }
                break;
            case 'K':
                for (r = 0; r < 3; r++)
                    for (l = 0; l < 3; l++) {
                        try {
                            accessible[x + kingMoves[r]][y + kingMoves[l]] =
                                    playerOnePieceCodes.indexOf(pieceGrid[x + kingMoves[r]][y + kingMoves[l]]) == -1;
                        } catch (ArrayIndexOutOfBoundsException aioobexc) {

                        }
                    }
                break;
            case 'k':
                for (r = 0; r < 3; r++)
                    for (l = 0; l < 3; l++) {
                        try {
                            accessible[x + kingMoves[r]][y + kingMoves[l]] =
                                    playerTwoPieceCodes.indexOf(pieceGrid[x + kingMoves[r]][y + kingMoves[l]]) == -1;
                        } catch (ArrayIndexOutOfBoundsException aioobexc) {

                        }
                    }
        }
        return accessible[u][v];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }
}
