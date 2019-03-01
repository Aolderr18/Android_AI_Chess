package com.bignerdranch.android.adam_chess_app;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public abstract class SmartChessStrategySelector extends ChessMove {

    protected ChessBoard playerOneBoard;
    protected ChessBoard playerTwoBoard;
    protected Integer[] choice;

    public abstract int getPlayerAdvantage();

    protected SmartChessStrategySelector() {
        super();
    }

    public final Integer[] getChoice() {
        return choice;
    }

}

final class SmartChessStrategySelectorP1 extends SmartChessStrategySelector {

    private int playerOneAdvantage;

    public SmartChessStrategySelectorP1(ChessBoard board, boolean mandatoryAttack) {
        super();
        playerOneBoard = ChessBoard.duplicate(board);
        if (!playerOneBoard.isPlayerOneTurn())
            playerOneBoard.relocate(0, 0, 0, 0);
        playerTwoBoard = ChessBoard.duplicate(board);
        if (playerTwoBoard.isPlayerOneTurn())
            playerTwoBoard.relocate(0, 0, 0, 0);
        final char[][] pieceGrid = board.getPieceGrid();
        int x, y;
        Map<Integer, Integer[]> moveChoices = new HashMap<Integer, Integer[]>();
        Map<Integer, Integer[]> moveChoices_attack = new HashMap<Integer, Integer[]>();
        final String playerOnePieceCodes = ChessBoard.playerOnePieceCodes;
        final String playerTwoPieceCodes = ChessBoard.playerTwoPieceCodes;
        final String allPieceCodes = ChessBoard.allPieceCodes;
        int highestAdvantage = Integer.MIN_VALUE;
        int highestAdvantage_attack = Integer.MIN_VALUE;
        LinkedList<Integer[]> preventionMoveChoices = checkMatePreventionMoves();
        Iterator<Integer[]> itr = preventionMoveChoices.iterator();
        while (itr.hasNext()) {
            Integer[] choice_prevent = itr.next();
            assert choice_prevent.length == 4;
            ChessBoard next = ChessBoard.duplicate(board);
            next.relocate(choice_prevent[0], choice_prevent[1], choice_prevent[2], choice_prevent[3]);
            SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
            moveChoices.put(future.getPlayerAdvantage(), new Integer[] { choice_prevent[0], choice_prevent[1], choice_prevent[2], choice_prevent[3] });
            if (future.getPlayerAdvantage() > highestAdvantage)
                highestAdvantage = future.getPlayerAdvantage();
            if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                choice = choice_prevent;
                return;
            }
        }
        if (!moveChoices.isEmpty()) {
            choice = moveChoices.get(highestAdvantage);
            return;
        }
        int r, l;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        final int[] x_indices = MainActivity.generateRandomIntegerIndices(chessBoardSize);
        final int[] y_indices = MainActivity.generateRandomIntegerIndices(chessBoardSize);
        for (int X = 0; X < chessBoardSize; X++)
            for (int Y = 0; Y < chessBoardSize; Y++) {
                x = x_indices[X];
                y = y_indices[Y];
                switch (pieceGrid[x][y]) {
                    case 'P':
                        if (x + 1 < chessBoardSize && y - 1 >= 0 && playerTwoPieceCodes.indexOf(pieceGrid[x + 1][y - 1]) > -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x + 1, y - 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x + 1, y - 1);
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x + 1, y - 1};
                                    return;
                                }
                                moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x + 1, y - 1});
                                if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                    highestAdvantage_attack = future.getPlayerAdvantage();
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (x - 1 >= 0 && y - 1 >= 0 && playerTwoPieceCodes.indexOf(pieceGrid[x - 1][y - 1]) > -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x - 1, y - 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x - 1, y - 1);
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x - 1, y - 1};
                                    return;
                                }
                                moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x - 1, y - 1});
                                if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                    highestAdvantage_attack = future.getPlayerAdvantage();
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (y == 6) {
                            if (allPieceCodes.indexOf(pieceGrid[x][5]) == -1) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, 5, 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                    next.relocate(x, y, x, 5);
                                    SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                    if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                        choice = new Integer[]{x, y, x, 5};
                                        return;
                                    }
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, 5});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (allPieceCodes.indexOf(pieceGrid[x][4]) == -1)
                                    try {
                                        new ChessMove(playerOneBoard, x, y, x, 4, 0, 0);
                                        ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                        next.relocate(x, y, x, 4);
                                        SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                        if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                            choice = new Integer[]{x, y, x, 4};
                                            return;
                                        }
                                        moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, 4});
                                        if (future.getPlayerAdvantage() > highestAdvantage)
                                            highestAdvantage = future.getPlayerAdvantage();
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                            }
                        } else if (y > 0 && allPieceCodes.indexOf(pieceGrid[x][y - 1]) == -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x, y - 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x, y - 1);
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x, y - 1};
                                    return;
                                }
                                moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, y - 1});
                                if (future.getPlayerAdvantage() > highestAdvantage)
                                    highestAdvantage = future.getPlayerAdvantage();
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, y};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, y};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                    SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                    if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                        choice = new Integer[]{x, y, x + knightMoves[r], y + knightMoves[l]};
                                        return;
                                    }
                                    if (playerTwoPieceCodes.indexOf(pieceGrid[x + knightMoves[r]][y + knightMoves[l]]) == -1) {
                                        moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x + knightMoves[r], y + knightMoves[l]});
                                        if (future.getPlayerAdvantage() > highestAdvantage)
                                            highestAdvantage = future.getPlayerAdvantage();
                                    } else {
                                        moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x + knightMoves[r], y + knightMoves[l]});
                                        if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                            highestAdvantage_attack = future.getPlayerAdvantage();
                                    }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, y};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, y};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerTwoPieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                    SmartChessStrategySelectorP1 future = new SmartChessStrategySelectorP1(next, 0);
                                    if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                        choice = new Integer[]{x, y, x + kingMoves[r], y + kingMoves[l]};
                                        return;
                                    }
                                    if (playerTwoPieceCodes.indexOf(pieceGrid[x + kingMoves[r]][y + kingMoves[l]]) == -1) {
                                        moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x + kingMoves[r], y + kingMoves[l]});
                                        if (future.getPlayerAdvantage() > highestAdvantage)
                                            highestAdvantage = future.getPlayerAdvantage();
                                    } else {
                                        moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x + kingMoves[r], y + kingMoves[l]});
                                        if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                            highestAdvantage_attack = future.getPlayerAdvantage();
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                }
            }
        if ((mandatoryAttack && !moveChoices_attack.isEmpty()) || highestAdvantage_attack >= highestAdvantage)
            choice = moveChoices_attack.get(highestAdvantage_attack);
        else
            choice = moveChoices.get(highestAdvantage);
    }

    private SmartChessStrategySelectorP1(ChessBoard board, int unused) {
        if (board.playerTwoInCheckMate()) {
            playerOneAdvantage = Integer.MAX_VALUE;
            return;
        }
        playerOneBoard = ChessBoard.duplicate(board);
        if (!playerOneBoard.isPlayerOneTurn())
            playerOneBoard.relocate(0, 0, 0, 0);
        playerTwoBoard = ChessBoard.duplicate(board);
        if (playerTwoBoard.isPlayerOneTurn())
            playerTwoBoard.relocate(0, 0, 0, 0);
        playerOneAdvantage = board.playerTwoInCheck() ? 3 : 0;
        final char[][] pieceGrid = board.getPieceGrid();
        int x, y;
        for (x = 0; x < 8; x++)
            for (y = 0; y < 8; y++)
                switch (pieceGrid[x][y]) {
                    case 'P':
                        playerOneAdvantage += 3;
                        break;
                    case 'R':
                    case 'N':
                    case 'B':
                        playerOneAdvantage += 8;
                        break;
                    case 'Q':
                        playerOneAdvantage += 16;
                        break;
                    case 'p':
                        playerOneAdvantage -= 3;
                        break;
                    case 'r':
                    case 'n':
                    case 'b':
                        playerOneAdvantage -= 8;
                        break;
                    case 'q':
                        playerOneAdvantage -= 16;
                }
        boolean[][] alreadyPutInDanger = new boolean[chessBoardSize][chessBoardSize];
        for (x = 0; x < chessBoardSize; x++)
            for (y = 0; y < chessBoardSize; y++)
                alreadyPutInDanger[x][y] = false;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        int r, l;
        for (x = 0; x < chessBoardSize; x++)
            for (y = 0; y < chessBoardSize; y++)
                switch (pieceGrid[x][y]) {
                    case 'P':
                        if (x + 1 < chessBoardSize && y - 1 >= 0 && playerTwoPieceCodes.indexOf(pieceGrid[x + 1][y - 1]) > -1)
                            if (!alreadyPutInDanger[x + 1][y - 1]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x + 1, y - 1, 0, 0);
                                    alreadyPutInDanger[x + 1][y - 1] = true;
                                    switch (pieceGrid[x + 1][y - 1]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                        if (x - 1 >= 0 && y - 1 >= 0 && playerTwoPieceCodes.indexOf(pieceGrid[x - 1][y - 1]) > -1)
                            if (!alreadyPutInDanger[x - 1][y - 1]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x - 1, y - 1, 0, 0);
                                    alreadyPutInDanger[x - 1][y - 1] = true;
                                    switch (pieceGrid[x - 1][y - 1]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                        break;
                    case 'R':
                        r = x;
                        while (r < chessBoardSize - 1) {
                            r++;
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                                    if (!alreadyPutInDanger[x + knightMoves[r]][y + knightMoves[l]]) {
                                        try {
                                            new ChessMove(playerOneBoard, x, y, x + knightMoves[r], y + knightMoves[l], 0, 0);
                                            alreadyPutInDanger[x + knightMoves[r]][y + knightMoves[l]] = true;
                                            ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                            switch (pieceGrid[x + knightMoves[r]][y + knightMoves[l]]) {
                                                case 'p':
                                                    playerOneAdvantage++;
                                                    break;
                                                case 'r':
                                                case 'n':
                                                case 'b':
                                                    playerOneAdvantage += 3;
                                                    break;
                                                case 'q':
                                                    playerOneAdvantage += 12;
                                            }
                                        } catch (InvalidChessMoveException ivcmexc) {

                                        }
                                    }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerOneAdvantage++;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerOneAdvantage += 3;
                                            break;
                                        case 'q':
                                            playerOneAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                                    if (!alreadyPutInDanger[x + kingMoves[r]][y + kingMoves[l]]) {
                                        try {
                                            new ChessMove(playerOneBoard, x, y, x + kingMoves[r], y + kingMoves[l], 0, 0);
                                            alreadyPutInDanger[x + kingMoves[r]][y + kingMoves[l]] = true;
                                            switch (pieceGrid[x + kingMoves[r]][y + kingMoves[l]]) {
                                                case 'p':
                                                    playerOneAdvantage++;
                                                    break;
                                                case 'r':
                                                case 'n':
                                                case 'b':
                                                    playerOneAdvantage += 3;
                                                    break;
                                                case 'q':
                                                    playerOneAdvantage += 12;
                                            }
                                        } catch (InvalidChessMoveException ivcmexc) {

                                        }
                                    }
                                } catch (ArrayIndexOutOfBoundsException aioobexc) {

                                }
                            }
                        break;
                    case 'p':
                        if (x + 1 < chessBoardSize && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x + 1][y + 1]) > -1)
                            if (!alreadyPutInDanger[x + 1][y + 1]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x + 1, y + 1, 0, 0);
                                    alreadyPutInDanger[x + 1][y + 1] = true;
                                    switch (pieceGrid[x + 1][y + 1]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                        if (x - 1 >= 0 && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x - 1][y + 1]) > -1)
                            if (!alreadyPutInDanger[x - 1][y + 1]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x - 1, y + 1, 0, 0);
                                    alreadyPutInDanger[x - 1][y + 1] = true;
                                    switch (pieceGrid[x - 1][y + 1]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                        break;
                    case 'r':
                        r = x;
                        while (r < chessBoardSize - 1) {
                            r++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                                    if (!alreadyPutInDanger[x + knightMoves[r]][y + knightMoves[l]]) {
                                        try {
                                            new ChessMove(playerTwoBoard, x, y, x + knightMoves[r], y + knightMoves[l], 0, 0);
                                            alreadyPutInDanger[x + knightMoves[r]][y + knightMoves[l]] = true;
                                            switch (pieceGrid[x + knightMoves[r]][y + knightMoves[l]]) {
                                                case 'P':
                                                    playerOneAdvantage -= 5;
                                                    break;
                                                case 'R':
                                                case 'N':
                                                case 'B':
                                                    playerOneAdvantage -= 20;
                                                    break;
                                                case 'Q':
                                                    playerOneAdvantage -= 48;
                                            }
                                        } catch (InvalidChessMoveException ivcmexc) {

                                        }
                                    }
                                } catch (ArrayIndexOutOfBoundsException aioobexc) {

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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerOneAdvantage -= 5;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerOneAdvantage -= 20;
                                            break;
                                        case 'Q':
                                            playerOneAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                                    if (!alreadyPutInDanger[x + kingMoves[r]][y + kingMoves[l]]) {
                                        try {
                                            new ChessMove(playerTwoBoard, x, y, x + kingMoves[r], y + kingMoves[l], 0, 0);
                                            alreadyPutInDanger[x + kingMoves[r]][y + kingMoves[l]] = true;
                                            switch (pieceGrid[x + kingMoves[r]][y + kingMoves[l]]) {
                                                case 'P':
                                                    playerOneAdvantage -= 5;
                                                    break;
                                                case 'R':
                                                case 'N':
                                                case 'B':
                                                    playerOneAdvantage -= 20;
                                                    break;
                                                case 'Q':
                                                    playerOneAdvantage -= 48;
                                            }
                                        } catch (InvalidChessMoveException ivcmexc) {

                                        }
                                    }
                                } catch (ArrayIndexOutOfBoundsException aioobexc) {

                                }
                            }
                }
    }

    private LinkedList<Integer[]> movesMakingMoveIllegal(int a, int b, int c, int d, ChessBoard playerOneBoard) {
        final char[][] pieceGrid = playerOneBoard.getPieceGrid();
        int x, y;
        int r, l;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        LinkedList<Integer[]> moveChoices = new LinkedList<Integer[]>();
        for (x = 0; x < 8; x++)
            for (y = 0; y < 8; y++)
                switch (pieceGrid[x][y]) {
                    case 'P':
                        if (x + 1 < chessBoardSize && y - 1 >= 0 && playerTwoPieceCodes.indexOf(pieceGrid[x + 1][y - 1]) > -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x + 1, y - 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x + 1, y - 1);
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x + 1, y - 1 });
                                }
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (x - 1 >= 0 && y - 1 >= 0 && playerTwoPieceCodes.indexOf(pieceGrid[x - 1][y - 1]) > -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x - 1, y - 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x - 1, y - 1);
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x - 1, y - 1 });
                                }
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (y == 6) {
                            if (allPieceCodes.indexOf(pieceGrid[x][5]) == -1) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, 5, 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                    next.relocate(x, y, x, 5);
                                    assert !next.isPlayerOneTurn();
                                    try {
                                        new ChessMove(next, a, b, c, d);
                                    } catch (InvalidChessMoveException ivcmexc1) {
                                        moveChoices.add(new Integer[] { x, y, x, 5 });
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (allPieceCodes.indexOf(pieceGrid[x][4]) == -1)
                                    try {
                                        new ChessMove(playerOneBoard, x, y, x, 4, 0, 0);
                                        ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                        next.relocate(x, y, x, 4);
                                        assert !next.isPlayerOneTurn();
                                        try {
                                            new ChessMove(next, a, b, c, d);
                                        } catch (InvalidChessMoveException ivcmexc1) {
                                            moveChoices.add(new Integer[] { x, y, x, 4 });
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                            }
                        } else if (y > 0 && allPieceCodes.indexOf(pieceGrid[x][y - 1]) == -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x, y - 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x, y - 1);
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x, y - 1 });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, y });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, y });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x, l });
                                }
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
                                    assert !next.isPlayerOneTurn();
                                    try {
                                        new ChessMove(next, a, b, c, d);
                                    } catch (InvalidChessMoveException ivcmexc1) {
                                        moveChoices.add(new Integer[]{x, y, x + knightMoves[r], y + knightMoves[l]});
                                    }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, y });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, y });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                    assert !next.isPlayerOneTurn();
                                    try {
                                        new ChessMove(next, a, b, c, d);
                                    } catch (InvalidChessMoveException ivcmexc1) {
                                        moveChoices.add(new Integer[]{x, y, x + kingMoves[r], y + kingMoves[l]});
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                }
        return moveChoices;
    }

    private LinkedList<Integer[]> checkMatePreventionMoves() {
        final char[][] pieceGrid = playerTwoBoard.getPieceGrid();
        int x, y;
        int r, l;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        LinkedList<Integer[]> preventionMoveChoices = new LinkedList<Integer[]>();
        for (x = 0; x < chessBoardSize; x++)
            for (y = 0; y < chessBoardSize; y++)
                switch (pieceGrid[x][y]) {
                    case 'p':
                        if (x + 1 < chessBoardSize && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x + 1][y + 1]) > -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x + 1, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x + 1, y + 1);
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x + 1, y + 1, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (x - 1 >= 0 && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x - 1][y + 1]) > -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x - 1, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x - 1, y + 1);
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x - 1, y + 1, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (y == 6) {
                            if (allPieceCodes.indexOf(pieceGrid[x][2]) == -1) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, 2, 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                    next.relocate(x, y, x, 2);
                                    if (next.playerOneInCheckMate()) {
                                        LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, 2, playerOneBoard);
                                        Iterator<Integer[]> itr = makeIllegal.iterator();
                                        while (itr.hasNext())
                                            preventionMoveChoices.add(itr.next());
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (allPieceCodes.indexOf(pieceGrid[x][3]) == -1)
                                    try {
                                        new ChessMove(playerTwoBoard, x, y, x, 3, 0, 0);
                                        ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                        next.relocate(x, y, x, 3);
                                        if (next.playerOneInCheckMate()) {
                                            LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, 3, playerOneBoard);
                                            Iterator<Integer[]> itr = makeIllegal.iterator();
                                            while (itr.hasNext())
                                                preventionMoveChoices.add(itr.next());
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                            }
                        } else if (y > 0 && allPieceCodes.indexOf(pieceGrid[x][y + 1]) == -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x, y + 1);
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, y + 1, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, y, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, y, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                    if (next.playerOneInCheckMate()) {
                                        LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x + knightMoves[r], y + knightMoves[l], playerOneBoard);
                                        Iterator<Integer[]> itr = makeIllegal.iterator();
                                        while (itr.hasNext())
                                            preventionMoveChoices.add(itr.next());
                                    }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, y, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, y, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerOneInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerOneBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                    if (next.playerOneInCheckMate()) {
                                        LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x + kingMoves[r], y + kingMoves[l], playerOneBoard);
                                        Iterator<Integer[]> itr = makeIllegal.iterator();
                                        while (itr.hasNext())
                                            preventionMoveChoices.add(itr.next());
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                }
        return preventionMoveChoices;
    }

    public ChessBoard getPlayerOneBoard() {
        return playerOneBoard;
    }

    @Override
    public int getPlayerAdvantage() {
        return playerOneAdvantage;
    }
}

final class SmartChessStrategySelectorP2 extends SmartChessStrategySelector {

    private int playerTwoAdvantage;

    public SmartChessStrategySelectorP2(ChessBoard board, boolean mandatoryAttack) {
        super();
        playerOneBoard = ChessBoard.duplicate(board);
        if (!playerOneBoard.isPlayerOneTurn())
            playerOneBoard.relocate(0, 0, 0, 0);
        playerTwoBoard = ChessBoard.duplicate(board);
        if (playerTwoBoard.isPlayerOneTurn())
            playerTwoBoard.relocate(0, 0, 0, 0);
        final char[][] pieceGrid = board.getPieceGrid();
        int x, y;
        Map<Integer, Integer[]> moveChoices = new HashMap<Integer, Integer[]>();
        Map<Integer, Integer[]> moveChoices_attack = new HashMap<Integer, Integer[]>();
        final String playerOnePieceCodes = ChessBoard.playerOnePieceCodes;
        final String playerTwoPieceCodes = ChessBoard.playerTwoPieceCodes;
        final String allPieceCodes = ChessBoard.allPieceCodes;
        int highestAdvantage = Integer.MIN_VALUE;
        int highestAdvantage_attack = Integer.MIN_VALUE;
        LinkedList<Integer[]> preventionMoveChoices = checkMatePreventionMoves();
        Iterator<Integer[]> itr = preventionMoveChoices.iterator();
        while (itr.hasNext()) {
            Integer[] choice_prevent = itr.next();
            assert choice_prevent.length == 4;
            ChessBoard next = ChessBoard.duplicate(board);
            next.relocate(choice_prevent[0], choice_prevent[1], choice_prevent[2], choice_prevent[3]);
            SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
            moveChoices.put(future.getPlayerAdvantage(), new Integer[] { choice_prevent[0], choice_prevent[1], choice_prevent[2], choice_prevent[3] });
            if (future.getPlayerAdvantage() > highestAdvantage)
                highestAdvantage = future.getPlayerAdvantage();
            if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                choice = choice_prevent;
                return;
            }
        }
        if (!moveChoices.isEmpty()) {
            choice = moveChoices.get(highestAdvantage);
            return;
        }
        int r, l;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        final int[] x_indices = MainActivity.generateRandomIntegerIndices(chessBoardSize);
        final int[] y_indices = MainActivity.generateRandomIntegerIndices(chessBoardSize);
        for (int X = 0; X < chessBoardSize; X++)
            for (int Y = 0; Y < chessBoardSize; Y++) {
                x = x_indices[X];
                y = y_indices[Y];
                switch (pieceGrid[x][y]) {
                    case 'p':
                        if (x + 1 < chessBoardSize && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x + 1][y + 1]) > -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x + 1, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x + 1, y + 1);
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x + 1, y + 1});
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x + 1, y + 1};
                                    return;
                                }
                                if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                    highestAdvantage_attack = future.getPlayerAdvantage();
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (x - 1 >= 0 && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x - 1][y + 1]) > -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x - 1, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x - 1, y + 1);
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x - 1, y + 1});
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x - 1, y + 1};
                                    return;
                                }
                                if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                    highestAdvantage_attack = future.getPlayerAdvantage();
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (y == 6) {
                            if (allPieceCodes.indexOf(pieceGrid[x][5]) == -1) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, 5, 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                    next.relocate(x, y, x, 5);
                                    SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, 5});
                                    if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                        choice = new Integer[]{x, y, x, 5};
                                        return;
                                    }
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (allPieceCodes.indexOf(pieceGrid[x][4]) == -1)
                                    try {
                                        new ChessMove(playerTwoBoard, x, y, x, 4, 0, 0);
                                        ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                        next.relocate(x, y, x, 4);
                                        SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                        moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, 4});
                                        if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                            choice = new Integer[]{x, y, x, 4};
                                            return;
                                        }
                                        if (future.getPlayerAdvantage() > highestAdvantage)
                                            highestAdvantage = future.getPlayerAdvantage();
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                            }
                        } else if (y < chessBoardSize - 1 && allPieceCodes.indexOf(pieceGrid[x][y + 1]) == -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x, y + 1);
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, y + 1});
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x, y + 1};
                                    return;
                                }
                                if (future.getPlayerAdvantage() > highestAdvantage)
                                    highestAdvantage = future.getPlayerAdvantage();
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, y};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, y};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                    SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                    if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                        choice = new Integer[]{x, y, x + knightMoves[r], y + knightMoves[l]};
                                        return;
                                    }
                                    if (playerOnePieceCodes.indexOf(pieceGrid[x + knightMoves[r]][y + knightMoves[l]]) == -1) {
                                        moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x + knightMoves[r], y + knightMoves[l]});
                                        if (future.getPlayerAdvantage() > highestAdvantage)
                                            highestAdvantage = future.getPlayerAdvantage();
                                    } else {
                                        moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x + knightMoves[r], y + knightMoves[l]});
                                        if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                            highestAdvantage_attack = future.getPlayerAdvantage();
                                    }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, y};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, y};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, y});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, x, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][l]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                    choice = new Integer[]{x, y, r, l};
                                    return;
                                }
                                if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) == -1) {
                                    moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage)
                                        highestAdvantage = future.getPlayerAdvantage();
                                } else {
                                    moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, r, l});
                                    if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                        highestAdvantage_attack = future.getPlayerAdvantage();
                                }
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
                                    SmartChessStrategySelectorP2 future = new SmartChessStrategySelectorP2(next, 0);
                                    if (future.getPlayerAdvantage() == Integer.MAX_VALUE) {
                                        choice = new Integer[]{x, y, x + kingMoves[r], y + kingMoves[l]};
                                        return;
                                    }
                                    if (playerOnePieceCodes.indexOf(pieceGrid[x + kingMoves[r]][y + kingMoves[l]]) == -1) {
                                        moveChoices.put(future.getPlayerAdvantage(), new Integer[]{x, y, x + kingMoves[r], y + kingMoves[l]});
                                        if (future.getPlayerAdvantage() > highestAdvantage)
                                            highestAdvantage = future.getPlayerAdvantage();
                                    } else {
                                        moveChoices_attack.put(future.getPlayerAdvantage(), new Integer[]{x, y, x + kingMoves[r], y + kingMoves[l]});
                                        if (future.getPlayerAdvantage() > highestAdvantage_attack)
                                            highestAdvantage_attack = future.getPlayerAdvantage();
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                }
            }
        if ((mandatoryAttack && !moveChoices_attack.isEmpty()) || highestAdvantage_attack >= highestAdvantage)
            choice = moveChoices_attack.get(highestAdvantage_attack);
        else
            choice = moveChoices.get(highestAdvantage);
    }

    private SmartChessStrategySelectorP2(ChessBoard board, int unused) {
        if (board.playerOneInCheckMate()) {
            playerTwoAdvantage = Integer.MAX_VALUE;
            return;
        }
        playerOneBoard = ChessBoard.duplicate(board);
        if (!playerOneBoard.isPlayerOneTurn())
            playerOneBoard.relocate(0, 0, 0, 0);
        playerTwoBoard = ChessBoard.duplicate(board);
        if (playerTwoBoard.isPlayerOneTurn())
            playerTwoBoard.relocate(0, 0, 0, 0);
        playerTwoAdvantage = board.playerOneInCheck() ? 3 : 0;
        final char[][] pieceGrid = board.getPieceGrid();
        int x, y;
        for (x = 0; x < 8; x++)
            for (y = 0; y < 8; y++)
                switch (pieceGrid[x][y]) {
                    case 'P':
                        playerTwoAdvantage -= 3;
                        break;
                    case 'R':
                    case 'N':
                    case 'B':
                        playerTwoAdvantage -= 8;
                        break;
                    case 'Q':
                        playerTwoAdvantage -= 16;
                        break;
                    case 'p':
                        playerTwoAdvantage += 3;
                        break;
                    case 'r':
                    case 'n':
                    case 'b':
                        playerTwoAdvantage += 8;
                        break;
                    case 'q':
                        playerTwoAdvantage += 16;
                }
        boolean[][] alreadyPutInDanger = new boolean[chessBoardSize][chessBoardSize];
        for (x = 0; x < chessBoardSize; x++)
            for (y = 0; y < chessBoardSize; y++)
                alreadyPutInDanger[x][y] = false;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        int r, l;
        for (x = 0; x < chessBoardSize; x++)
            for (y = 0; y < chessBoardSize; y++)
                switch (pieceGrid[x][y]) {
                    case 'P':
                        if (x + 1 < chessBoardSize && y - 1 >= 0 && playerTwoPieceCodes.indexOf(pieceGrid[x + 1][y - 1]) > -1)
                            if (!alreadyPutInDanger[x + 1][y - 1]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x + 1, y - 1, 0, 0);
                                    alreadyPutInDanger[x + 1][y - 1] = true;
                                    switch (pieceGrid[x + 1][y - 1]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                        if (x - 1 >= 0 && y - 1 >= 0 && playerTwoPieceCodes.indexOf(pieceGrid[x - 1][y - 1]) > -1)
                            if (!alreadyPutInDanger[x - 1][y - 1]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x - 1, y - 1, 0, 0);
                                    alreadyPutInDanger[x - 1][y - 1] = true;
                                    switch (pieceGrid[x - 1][y - 1]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                        break;
                    case 'R':
                        r = x;
                        while (r < chessBoardSize - 1) {
                            r++;
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                                if (!alreadyPutInDanger[x + knightMoves[r]][y + knightMoves[l]]) {
                                    try {
                                        new ChessMove(playerOneBoard, x, y, x + knightMoves[r], y + knightMoves[l], 0, 0);
                                        alreadyPutInDanger[x + knightMoves[r]][y + knightMoves[l]] = true;
                                        switch (pieceGrid[x + knightMoves[r]][y + knightMoves[l]]) {
                                            case 'p':
                                                playerTwoAdvantage -= 5;
                                                break;
                                            case 'r':
                                            case 'n':
                                            case 'b':
                                                playerTwoAdvantage -= 20;
                                                break;
                                            case 'q':
                                                playerTwoAdvantage -= 48;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'p':
                                            playerTwoAdvantage -= 5;
                                            break;
                                        case 'r':
                                        case 'n':
                                        case 'b':
                                            playerTwoAdvantage -= 20;
                                            break;
                                        case 'q':
                                            playerTwoAdvantage -= 48;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                                if (!alreadyPutInDanger[x + kingMoves[r]][y + kingMoves[l]]) {
                                    try {
                                        new ChessMove(playerOneBoard, x, y, x + kingMoves[r], y + kingMoves[l], 0, 0);
                                        alreadyPutInDanger[x + kingMoves[r]][y + kingMoves[l]] = true;
                                        switch (pieceGrid[x + kingMoves[r]][y + kingMoves[l]]) {
                                            case 'p':
                                                playerTwoAdvantage -= 5;
                                                break;
                                            case 'r':
                                            case 'n':
                                            case 'b':
                                                playerTwoAdvantage -= 20;
                                                break;
                                            case 'q':
                                                playerTwoAdvantage -= 48;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                                }
                            }
                        break;
                    case 'p':
                        if (x + 1 < chessBoardSize && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x + 1][y + 1]) > -1)
                            if (!alreadyPutInDanger[x + 1][y + 1]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x + 1, y + 1, 0, 0);
                                    alreadyPutInDanger[x + 1][y + 1] = true;
                                    switch (pieceGrid[x + 1][y + 1]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                        if (x - 1 >= 0 && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x - 1][y + 1]) > -1)
                            if (!alreadyPutInDanger[x - 1][y + 1]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x - 1, y + 1, 0, 0);
                                    alreadyPutInDanger[x - 1][y + 1] = true;
                                    switch (pieceGrid[x - 1][y + 1]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                        break;
                    case 'r':
                        r = x;
                        while (r < chessBoardSize - 1) {
                            r++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                                if (!alreadyPutInDanger[x + knightMoves[r]][y + knightMoves[l]]) {
                                    try {
                                        new ChessMove(playerTwoBoard, x, y, x + knightMoves[r], y + knightMoves[l], 0, 0);
                                        alreadyPutInDanger[x + knightMoves[r]][y + knightMoves[l]] = true;
                                        switch (pieceGrid[x + knightMoves[r]][y + knightMoves[l]]) {
                                            case 'P':
                                                playerTwoAdvantage++;
                                                break;
                                            case 'R':
                                            case 'N':
                                            case 'B':
                                                playerTwoAdvantage += 3;
                                                break;
                                            case 'Q':
                                                playerTwoAdvantage += 12;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        r = x;
                        while (r > 0) {
                            r--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                            if (!alreadyPutInDanger[r][y]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, y, 0, 0);
                                    alreadyPutInDanger[r][y] = true;
                                    switch (pieceGrid[r][y]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[r][y]) > -1)
                                break;
                        }
                        l = y;
                        while (l < chessBoardSize - 1) {
                            l++;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                            if (playerOnePieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                        }
                        l = y;
                        while (l > 0) {
                            l--;
                            if (playerTwoPieceCodes.indexOf(pieceGrid[x][l]) > -1)
                                break;
                            if (!alreadyPutInDanger[x][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, l, 0, 0);
                                    alreadyPutInDanger[x][l] = true;
                                    switch (pieceGrid[x][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                            if (!alreadyPutInDanger[r][l]) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, r, l, 0, 0);
                                    alreadyPutInDanger[r][l] = true;
                                    switch (pieceGrid[r][l]) {
                                        case 'P':
                                            playerTwoAdvantage++;
                                            break;
                                        case 'R':
                                        case 'N':
                                        case 'B':
                                            playerTwoAdvantage += 3;
                                            break;
                                        case 'Q':
                                            playerTwoAdvantage += 12;
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
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
                                if (!alreadyPutInDanger[x + kingMoves[r]][y + kingMoves[l]]) {
                                    try {
                                        new ChessMove(playerTwoBoard, x, y, x + kingMoves[r], y + kingMoves[l], 0, 0);
                                        alreadyPutInDanger[x + kingMoves[r]][y + kingMoves[l]] = true;
                                        switch (pieceGrid[x + kingMoves[r]][y + kingMoves[l]]) {
                                            case 'P':
                                                playerTwoAdvantage++;
                                                break;
                                            case 'R':
                                            case 'N':
                                            case 'B':
                                                playerTwoAdvantage += 3;
                                                break;
                                            case 'Q':
                                                playerTwoAdvantage += 12;
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                                }
                            }
                }
    }

    private LinkedList<Integer[]> movesMakingMoveIllegal(int a, int b, int c, int d, ChessBoard playerTwoBoard) {
        final char[][] pieceGrid = playerTwoBoard.getPieceGrid();
        int x, y;
        int r, l;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        LinkedList<Integer[]> moveChoices = new LinkedList<Integer[]>();
        for (x = 0; x < 8; x++)
            for (y = 0; y < 8; y++)
                switch (pieceGrid[x][y]) {
                    case 'p':
                        if (x + 1 < chessBoardSize && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x + 1][y + 1]) > -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x + 1, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x + 1, y + 1);
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x + 1, y + 1 });
                                }
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (x - 1 >= 0 && y + 1 < chessBoardSize && playerOnePieceCodes.indexOf(pieceGrid[x - 1][y + 1]) > -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x - 1, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x - 1, y + 1);
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x - 1, y + 1 });
                                }
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (y == 1) {
                            if (allPieceCodes.indexOf(pieceGrid[x][2]) == -1) {
                                try {
                                    new ChessMove(playerTwoBoard, x, y, x, 2, 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                    next.relocate(x, y, x, 2);
                                    assert !next.isPlayerOneTurn();
                                    try {
                                        new ChessMove(next, a, b, c, d);
                                    } catch (InvalidChessMoveException ivcmexc1) {
                                        moveChoices.add(new Integer[] { x, y, x, 2 });
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (allPieceCodes.indexOf(pieceGrid[x][3]) == -1)
                                    try {
                                        new ChessMove(playerTwoBoard, x, y, x, 3, 0, 0);
                                        ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                        next.relocate(x, y, x, 3);
                                        assert !next.isPlayerOneTurn();
                                        try {
                                            new ChessMove(next, a, b, c, d);
                                        } catch (InvalidChessMoveException ivcmexc1) {
                                            moveChoices.add(new Integer[] { x, y, x, 3 });
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                            }
                        } else if (y + 1 < chessBoardSize && allPieceCodes.indexOf(pieceGrid[x][y + 1]) == -1)
                            try {
                                new ChessMove(playerTwoBoard, x, y, x, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerTwoBoard);
                                next.relocate(x, y, x, y + 1);
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x, y + 1 });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, y });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, y });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x, l });
                                }
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
                                    assert !next.isPlayerOneTurn();
                                    try {
                                        new ChessMove(next, a, b, c, d);
                                    } catch (InvalidChessMoveException ivcmexc1) {
                                        moveChoices.add(new Integer[]{x, y, x + knightMoves[r], y + knightMoves[l]});
                                    }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, y });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, y });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, x, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                assert !next.isPlayerOneTurn();
                                try {
                                    new ChessMove(next, a, b, c, d);
                                } catch (InvalidChessMoveException ivcmexc1) {
                                    moveChoices.add(new Integer[] { x, y, r, l });
                                }
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
                                    assert !next.isPlayerOneTurn();
                                    try {
                                        new ChessMove(next, a, b, c, d);
                                    } catch (InvalidChessMoveException ivcmexc1) {
                                        moveChoices.add(new Integer[]{x, y, x + kingMoves[r], y + kingMoves[l]});
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                }
        Iterator<Integer[]> prevz = moveChoices.iterator();
        while (prevz.hasNext()) {
            Integer[] PV = prevz.next();
        }
        return moveChoices;
    }

    private LinkedList<Integer[]> checkMatePreventionMoves() {
        final char[][] pieceGrid = playerOneBoard.getPieceGrid();
        int x, y;
        int r, l;
        final int[] knightMoves = { -2, -1, 1, 2 };
        final int[] kingMoves = { -1, 0, 1 };
        LinkedList<Integer[]> preventionMoveChoices = new LinkedList<Integer[]>();
        for (x = 0; x < chessBoardSize; x++)
            for (y = 0; y < chessBoardSize; y++)
                switch (pieceGrid[x][y]) {
                    case 'P':
                        if (x + 1 < chessBoardSize && y + 1 < chessBoardSize && playerTwoPieceCodes.indexOf(pieceGrid[x + 1][y + 1]) > -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x + 1, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x + 1, y + 1);
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x + 1, y + 1, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (x - 1 >= 0 && y + 1 < chessBoardSize && playerTwoPieceCodes.indexOf(pieceGrid[x - 1][y + 1]) > -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x - 1, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x - 1, y + 1);
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x - 1, y + 1, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
                            } catch (InvalidChessMoveException ivcmexc) {

                            }
                        if (y == 6) {
                            if (allPieceCodes.indexOf(pieceGrid[x][2]) == -1) {
                                try {
                                    new ChessMove(playerOneBoard, x, y, x, 2, 0, 0);
                                    ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                    next.relocate(x, y, x, 2);
                                    if (next.playerTwoInCheckMate()) {
                                        LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, 2, playerTwoBoard);
                                        Iterator<Integer[]> itr = makeIllegal.iterator();
                                        while (itr.hasNext())
                                            preventionMoveChoices.add(itr.next());
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                                if (allPieceCodes.indexOf(pieceGrid[x][3]) == -1)
                                    try {
                                        new ChessMove(playerOneBoard, x, y, x, 3, 0, 0);
                                        ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                        next.relocate(x, y, x, 3);
                                        if (next.playerTwoInCheckMate()) {
                                            LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, 3, playerTwoBoard);
                                            Iterator<Integer[]> itr = makeIllegal.iterator();
                                            while (itr.hasNext())
                                                preventionMoveChoices.add(itr.next());
                                        }
                                    } catch (InvalidChessMoveException ivcmexc) {

                                    }
                            }
                        } else if (y > 0 && allPieceCodes.indexOf(pieceGrid[x][y + 1]) == -1)
                            try {
                                new ChessMove(playerOneBoard, x, y, x, y + 1, 0, 0);
                                ChessBoard next = ChessBoard.duplicate(playerOneBoard);
                                next.relocate(x, y, x, y + 1);
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, y + 1, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, y, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, y, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                    if (next.playerTwoInCheckMate()) {
                                        LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x + knightMoves[r], y + knightMoves[l], playerTwoBoard);
                                        Iterator<Integer[]> itr = makeIllegal.iterator();
                                        while (itr.hasNext())
                                            preventionMoveChoices.add(itr.next());
                                    }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, y, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, y, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                if (next.playerTwoInCheckMate()) {
                                    LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, r, l, playerTwoBoard);
                                    Iterator<Integer[]> itr = makeIllegal.iterator();
                                    while (itr.hasNext())
                                        preventionMoveChoices.add(itr.next());
                                }
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
                                    if (next.playerTwoInCheckMate()) {
                                        LinkedList<Integer[]> makeIllegal = movesMakingMoveIllegal(x, y, x + kingMoves[r], y + kingMoves[l], playerTwoBoard);
                                        Iterator<Integer[]> itr = makeIllegal.iterator();
                                        while (itr.hasNext())
                                            preventionMoveChoices.add(itr.next());
                                    }
                                } catch (InvalidChessMoveException ivcmexc) {

                                }
                            }
                }
        return preventionMoveChoices;
    }

    public ChessBoard getPlayerOneBoard() {
        return playerOneBoard;
    }

    @Override
    public int getPlayerAdvantage() {
        return playerTwoAdvantage;
    }
}
