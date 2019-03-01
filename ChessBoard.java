package com.bignerdranch.android.adam_chess_app;

public class ChessBoard {

    public static final int chessBoardSize = 8;
    public static final String playerOnePieceCodes = "PRNBQK";
    public static final String playerTwoPieceCodes = "prnbqk";
    public static final String allPieceCodes = playerOnePieceCodes + playerTwoPieceCodes;

    private char[][] pieceGrid;
    private boolean playerOneTurn;

    public void factoryReset() {
        int x, y;
        for (x = 0; x < 8; x++)
            for (y = 0; y < 8; y++)
                pieceGrid[x][y] = '0';
        pieceGrid[0][0] = 'r';
        pieceGrid[1][0] = 'n';
        pieceGrid[2][0] = 'b';
        pieceGrid[3][0] = 'q';
        pieceGrid[4][0] = 'k';
        pieceGrid[5][0] = 'b';
        pieceGrid[6][0] = 'n';
        pieceGrid[7][0] = 'r';
        for (int n = 0; n < chessBoardSize; n++) {
            pieceGrid[n][1] = 'p';
            pieceGrid[n][6] = 'P';
        }
        pieceGrid[0][7] = 'R';
        pieceGrid[1][7] = 'N';
        pieceGrid[2][7] = 'B';
        pieceGrid[3][7] = 'Q';
        pieceGrid[4][7] = 'K';
        pieceGrid[5][7] = 'B';
        pieceGrid[6][7] = 'N';
        pieceGrid[7][7] = 'R';
    }

    public ChessBoard() {
        pieceGrid = new char[chessBoardSize][chessBoardSize];
        playerOneTurn = true;
        factoryReset();
    }

    public boolean playerOneInCheckMate() {
        if (!playerOneInCheck())
            return false;
        ChessBoard playerOneBoard = ChessBoard.duplicate(this);
        if (!playerOneTurn)
            playerOneBoard.relocate(0, 0, 0, 0);
        final int[] knightMoves = new int[] { -2, -1, 1, 2 };
        final int[] kingMoves = new int[] { -1, 0, 1 };
        int x, y;
        int r, l;
        for (x = 0; x < chessBoardSize; x++)
            for (y = 0; y < chessBoardSize; y++)
                switch (pieceGrid[x][y]) {
                    case 'P':
                        if (x + 1 < chessBoardSize && y - 1 >= 0 && playerTwoPieceCodes.indexOf(pieceGrid[x + 1][y - 1]) > -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x + 1, y - 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x + 1, y - 1);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (x - 1 >= 0 && y - 1 >= 0 && playerTwoPieceCodes.indexOf(pieceGrid[x - 1][y - 1]) > -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x - 1, y - 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x - 1, y - 1);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (y == 6) {
                            if (allPieceCodes.indexOf(pieceGrid[x][5]) == -1) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, 5, 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                    next.relocate(x, y, x, 5);
                                    if (!next.playerOneInCheck())
                                        return false;
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (allPieceCodes.indexOf(pieceGrid[x][4]) == -1)
                                    try {
                                        new ChessMove(playerOneBoard, x, y, x, 4, 0, 0);
                                        ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                        next.relocate(x, y, x, 4);
                                        if (!next.playerOneInCheck())
                                            return false;
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                            }
                        } else if (y > 0 && allPieceCodes.indexOf(pieceGrid[x][y - 1]) == -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x, y - 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x, y - 1);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        break;
                    case 'R':
                        r = x;
                        while (r < chessBoardSize - 1) {
                            r++;
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            try {
                                new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, y);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            try {
                                new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, y);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            try {
                                new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            try {
                                new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        break;
                    case 'N':
                        for (r = 0; r < 4; r++)
                            for (l = 0; l < 4; l++) {
                                if (Math.abs(knightMoves[r] * knightMoves[l]) != 2 || x + knightMoves[r] < 0 ||
                                        x + knightMoves[r] >= chessBoardSize || y + knightMoves[l] < 0 ||
                                        y + knightMoves[l] >= chessBoardSize)
                                    continue;
                                if (playerOnePieceCodes.indexOf(pieceGrid[x + knightMoves[r]][y + knightMoves[l]]) > -1)
                                    continue;
                                try {
                                    new ChessMove(playerOneBoard, x, y, x + knightMoves[r], y + knightMoves[l], 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                    next.relocate(x, y, x + knightMoves[r], y + knightMoves[l]);
                                    if (!next.playerOneInCheck())
                                        return false;
                                } catch (InvalidChessMoveException ivcmexc) {

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
                            try {
                                new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                                break;
                        }
                        break;
                    case 'Q':
                        r = x;
                        while (r < chessBoardSize - 1) {
                            r++;
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            try {
                                new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, y);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            try {
                                new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, y);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            try {
                                new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            try {
                                new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerOneInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                                break;
                        }
                        break;
                    case 'K':
                        for (r = 0; r < 3; r++)
                            for (l = 0; l < 3; l++) {
                                if ((kingMoves[r] == 0 && kingMoves[l] == 0) || x + kingMoves[r] < 0 ||
                                        x + kingMoves[r] >= chessBoardSize || y + kingMoves[l] < 0 ||
                                        y + kingMoves[l] >= chessBoardSize)
                                    continue;
                                if (playerOnePieceCodes.indexOf(pieceGrid[x + kingMoves[r]][y + kingMoves[l]]) > -1)
                                    continue;
                                try {
                                    new ChessMove(playerOneBoard, x, y, x + kingMoves[r], y + kingMoves[l], 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                    next.relocate(x, y, x + kingMoves[r], y + kingMoves[l]);
                                    if (!next.playerOneInCheck())
                                        return false;
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                }
        return true;
    }

    public boolean playerOneInCheck() {
        ChessBoard playerTwoBoard = ChessBoard.duplicate(this);
        if (playerOneTurn)
            playerTwoBoard.relocate(0, 0, 0, 0);
        int x, y, u, v;
        for (u = 0; u < chessBoardSize; u++)
            for (v = 0; v < chessBoardSize; v++)
                for (x = 0; x < chessBoardSize; x++)
                    for (y = 0; y < chessBoardSize; y++)
                        if (pieceGrid[u][v] == 'K' && playerTwoPieceCodes.indexOf(pieceGrid[x][y]) > -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, u, v);
                                return true;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
        return false;
    }

    public boolean playerTwoInCheckMate() {
        if (!playerTwoInCheck())
            return false;
        ChessBoard playerTwoBoard = ChessBoard.duplicate(this);
        if (playerOneTurn)
            playerTwoBoard.relocate(0, 0, 0, 0);
        final int[] knightMoves = new int[] { -2, -1, 1, 2 };
        final int[] kingMoves = new int[] { -1, 0, 1 };
        int x, y;
        int r, l;
        for (x = 0; x < chessBoardSize; x++)
            for (y = 0; y < chessBoardSize; y++)
                switch (pieceGrid[x][y]) {
                    case 'p':
                        if (x + 1 < chessBoardSize && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x + 1][y + 1]) > -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x + 1, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x + 1, y + 1);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (x - 1 >= 0 && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x - 1][y + 1]) > -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x - 1, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x - 1, y + 1);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (y == 6) {
                            if (allPieceCodes.indexOf(pieceGrid[x][2]) == -1) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, 2, 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                    next.relocate(x, y, x, 2);
                                    if (!next.playerTwoInCheck())
                                        return false;
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (allPieceCodes.indexOf(pieceGrid[x][3]) == -1)
                                    try {
                                        new ChessMove(playerTwoBoard, x, y, x, 3, 0, 0);
                                        ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                        next.relocate(x, y, x, 3);
                                        if (!next.playerTwoInCheck())
                                            return false;
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                            }
                        } else if (y > 0 && allPieceCodes.indexOf(pieceGrid[x][y + 1]) == -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x, y + 1);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        break;
                    case 'r':
                        r = x;
                        while (r < chessBoardSize - 1) {
                            r++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, y);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, y);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            try {
                                new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            try {
                                new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        break;
                    case 'n':
                        for (r = 0; r < 4; r++)
                            for (l = 0; l < 4; l++) {
                                if (Math.abs(knightMoves[r] * knightMoves[l]) != 2 || x + knightMoves[r] < 0 ||
                                        x + knightMoves[r] >= chessBoardSize || y + knightMoves[l] < 0 ||
                                        y + knightMoves[l] >= chessBoardSize)
                                    continue;
                                if (playerTwoPieceCodes.indexOf(pieceGrid[x + knightMoves[r]][y + knightMoves[l]]) > -1)
                                    continue;
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x + knightMoves[r], y + knightMoves[l], 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                    next.relocate(x, y, x + knightMoves[r], y + knightMoves[l]);
                                    if (!next.playerTwoInCheck())
                                        return false;
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                                break;
                        }
                        break;
                    case 'q':
                        r = x;
                        while (r < chessBoardSize - 1) {
                            r++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, y);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, y);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            try {
                                new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            try {
                                new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        r = x;
                        l = y;
                        while (r < chessBoardSize - 1 && l < chessBoardSize - 1) {
                            r++;
                            l++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) > -1)
                                break;
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
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
                            try {
                                new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, r, l);
                                if (!next.playerTwoInCheck())
                                    return false;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) > -1)
                                break;
                        }
                        break;
                    case 'k':
                        for (r = 0; r < 3; r++)
                            for (l = 0; l < 3; l++) {
                                if ((kingMoves[r] == 0 && kingMoves[l] == 0) || x + kingMoves[r] < 0 ||
                                        x + kingMoves[r] >= chessBoardSize || y + kingMoves[l] < 0 ||
                                        y + kingMoves[l] >= chessBoardSize)
                                    continue;
                                if (playerTwoPieceCodes.indexOf(pieceGrid[x + kingMoves[r]][y + kingMoves[l]]) > -1)
                                    continue;
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x + kingMoves[r], y + kingMoves[l], 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                    next.relocate(x, y, x + kingMoves[r], y + kingMoves[l]);
                                    if (!next.playerTwoInCheck())
                                        return false;
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                }
        return true;
    }

    public boolean playerTwoInCheck() {
        ChessBoard playerOneBoard = ChessBoard.duplicate(this);
        if (!playerOneTurn)
            playerOneBoard.relocate(0, 0, 0, 0);
        int x, y, u, v;
        for (u = 0; u < chessBoardSize; u++)
            for (v = 0; v < chessBoardSize; v++)
                for (x = 0; x < chessBoardSize; x++)
                    for (y = 0; y < chessBoardSize; y++)
                        if (pieceGrid[u][v] == 'k' && playerOnePieceCodes.indexOf(pieceGrid[x][y]) > -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, u, v);
                                return true;
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
        return false;
    }

    private ChessBoard(char[][] pieceGrid, boolean playerOneTurn) {
        this.pieceGrid = pieceGrid.clone();
        this.playerOneTurn = playerOneTurn;
    }

    protected static ChessBoard duplicate(ChessBoard CB) {
        char[][] nextPieceGrid = new char[chessBoardSize][chessBoardSize];
        for (int i = 0; i < chessBoardSize; i++)
            for (int j = 0; j < chessBoardSize; j++)
                nextPieceGrid[i][j] = CB.getPieceGrid()[i][j];
        return new ChessBoard(nextPieceGrid, CB.isPlayerOneTurn());
    }

    protected void relocate(int x, int y, int u, int v) {
        if (!(x == u && y == v)) {
            pieceGrid[u][v] = pieceGrid[x][y];
            pieceGrid[x][y] = '0';
        }
        playerOneTurn = !playerOneTurn;
        if ("Pp".indexOf(pieceGrid[u][v]) > -1)
            if (v == chessBoardSize - 1 || v == 0) {
                if (playerOneTurn)
                    pieceGrid[u][v] = 'q';
                else
                    pieceGrid[u][v] = 'Q';
            }
    }

    public boolean move(int x, int y, int u, int v) {
        try {
            ChessMove move = new ChessMove(this, x, y, u, v);
            relocate(x, y, u, v);
            return true;
        } catch (InvalidChessMoveException ivcmexc) {
            return false;
        }
    }

    public boolean move(ChessMove M) {
        return move(M.getX(), M.getY(), M.getU(), M.getV());
    }

    public boolean vacantAt(int x, int y) {
        return pieceGrid[x][y] == '0';
    }

    public char[][] getPieceGrid() {
        return pieceGrid;
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

}
