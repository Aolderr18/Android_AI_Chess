package com.bignerdranch.android.adam_chess_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import static android.graphics.BitmapFactory.decodeResource;
import static com.bignerdranch.android.adam_chess_app.ChessBoard.chessBoardSize;

public class ChessBoardActivity extends Activity {

    private final static String TAG = ChessBoardActivity.class.getSimpleName();

    private boolean playerOneHuman;
    private boolean playerTwoHuman;
    private int select_x, select_y, destination_x, destination_y;
    private boolean human_player_moved;

    private Bitmap cyan_bishop_green_background;
    private Bitmap cyan_bishop_orange_background;
    private Bitmap cyan_king_green_background;
    private Bitmap cyan_king_orange_background;
    private Bitmap cyan_knight_green_background;
    private Bitmap cyan_knight_orange_background;
    private Bitmap cyan_pawn_green_background;
    private Bitmap cyan_pawn_orange_background;
    private Bitmap cyan_queen_green_background;
    private Bitmap cyan_queen_orange_background;
    private Bitmap cyan_rook_green_background;
    private Bitmap cyan_rook_orange_background;
    private Bitmap purple_bishop_green_background;
    private Bitmap purple_bishop_orange_background;
    private Bitmap purple_king_green_background;
    private Bitmap purple_king_orange_background;
    private Bitmap purple_knight_green_background;
    private Bitmap purple_knight_orange_background;
    private Bitmap purple_pawn_green_background;
    private Bitmap purple_pawn_orange_background;
    private Bitmap purple_queen_green_background;
    private Bitmap purple_queen_orange_background;
    private Bitmap purple_rook_green_background;
    private Bitmap purple_rook_orange_background;
    private Bitmap just_orange;
    private Bitmap just_green;

    private ChessGame game;
    private MyMotion myMotion;

    private int x, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent myIntent = getIntent();
        playerOneHuman = myIntent.getBooleanExtra("playerOneHuman", true);
        playerTwoHuman = myIntent.getBooleanExtra("playerTwoHuman", false);
        myMotion = new MyMotion(this);
        setContentView(myMotion);
        game = ChessGame.getInstance();
        select_x = -1;
        select_y = -1;
        human_player_moved = false;

        cyan_bishop_green_background = decodeResource(getResources(), R.drawable.cyan_bishop_green_background);
        cyan_bishop_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_bishop_orange_background);
        cyan_king_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_king_green_background);
        cyan_king_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_king_orange_background);
        cyan_knight_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_knight_green_background);
        cyan_knight_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_knight_orange_background);
        cyan_pawn_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_pawn_green_background);
        cyan_pawn_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_pawn_orange_background);
        cyan_queen_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_queen_green_background);
        cyan_queen_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_queen_orange_background);
        cyan_rook_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_rook_green_background);
        cyan_rook_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_rook_orange_background);
        purple_bishop_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_bishop_green_background);
        purple_bishop_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_bishop_orange_background);
        purple_king_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_king_green_background);
        purple_king_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_king_orange_background);
        purple_knight_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_knight_green_background);
        purple_knight_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_knight_orange_background);
        purple_pawn_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_pawn_green_background);
        purple_pawn_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_pawn_orange_background);
        purple_queen_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_queen_green_background);
        purple_queen_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_queen_orange_background);
        purple_rook_green_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_rook_green_background);
        purple_rook_orange_background = BitmapFactory.decodeResource(getResources(), R.drawable.purple_rook_orange_background);
        just_orange = BitmapFactory.decodeResource(getResources(), R.drawable.just_orange);
        just_green = BitmapFactory.decodeResource(getResources(), R.drawable.just_green);

        myMotion.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent me) {
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    x = (int) ((me.getX() / 110) - 1);
                    y = (int) ((me.getY() / 110) - 1);
                    if (select_x == -1 && select_y == -1) {
                        select_x = x;
                        select_y = y;
                    } else {
                        destination_x = x;
                        destination_y = y;
                        human_player_moved = true;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        myMotion.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        myMotion.resume();
    }

    private class MyMotion extends SurfaceView implements Runnable {

        private int i, j;

        private boolean isRunning = true;
        private boolean firstCycle;
        private int[] previous_p1_piece_counts;
        private int[] previous_p2_piece_counts;

        private SurfaceHolder holder;
        private Thread thread = null;

        public MyMotion(Context context) {
            super(context);
            holder = getHolder();
            x = -1;
            y = -1;
            firstCycle = true;
            previous_p1_piece_counts = new int[] {16, 16, 16, 16, 16, 16, 16, 16};
            previous_p2_piece_counts = new int[] {16, 16, 16, 16, 16, 16, 16, 16};
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void run() {
            Looper.prepare();
            while (isRunning) {
                if (!holder.getSurface().isValid())
                    continue;
                final Canvas canvas = holder.lockCanvas();
                canvas.drawColor(Color.BLACK);
                int p1_piece_count = 16;
                int p2_piece_count = 16;
                float X, Y;
                final char[][] pieceGrid = game.getBoard().getPieceGrid();
                for (i = 0; i < 8; i++)
                    for (j = 0; j < 8; j++) {
                        X = 110 + (i * 110);
                        Y = 110 + (j * 110);
                        if ((i + j) % 2 == 0) {
                            switch (pieceGrid[i][j]) {
                                case 'P':
                                    canvas.drawBitmap(getResizedBitmap(cyan_pawn_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'R':
                                    canvas.drawBitmap(getResizedBitmap(cyan_rook_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'N':
                                    canvas.drawBitmap(getResizedBitmap(cyan_knight_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'B':
                                    canvas.drawBitmap(getResizedBitmap(cyan_bishop_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'Q':
                                    canvas.drawBitmap(getResizedBitmap(cyan_queen_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'K':
                                    canvas.drawBitmap(getResizedBitmap(cyan_king_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'p':
                                    canvas.drawBitmap(getResizedBitmap(purple_pawn_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'r':
                                    canvas.drawBitmap(getResizedBitmap(purple_rook_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'n':
                                    canvas.drawBitmap(getResizedBitmap(purple_knight_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'b':
                                    canvas.drawBitmap(getResizedBitmap(purple_bishop_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'q':
                                    canvas.drawBitmap(getResizedBitmap(purple_queen_green_background, 110, 110), X, Y, null);
                                    break;
                                case 'k':
                                    canvas.drawBitmap(getResizedBitmap(purple_king_green_background, 110, 110), X, Y, null);
                                    break;
                                default:
                                    canvas.drawBitmap(getResizedBitmap(just_green, 110, 110), X, Y, null);
                            }
                        } else {
                            switch (pieceGrid[i][j]) {
                                case 'P':
                                    canvas.drawBitmap(getResizedBitmap(cyan_pawn_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'R':
                                    canvas.drawBitmap(getResizedBitmap(cyan_rook_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'N':
                                    canvas.drawBitmap(getResizedBitmap(cyan_knight_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'B':
                                    canvas.drawBitmap(getResizedBitmap(cyan_bishop_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'Q':
                                    canvas.drawBitmap(getResizedBitmap(cyan_queen_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'K':
                                    canvas.drawBitmap(getResizedBitmap(cyan_king_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'p':
                                    canvas.drawBitmap(getResizedBitmap(purple_pawn_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'r':
                                    canvas.drawBitmap(getResizedBitmap(purple_rook_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'n':
                                    canvas.drawBitmap(getResizedBitmap(purple_knight_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'b':
                                    canvas.drawBitmap(getResizedBitmap(purple_bishop_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'q':
                                    canvas.drawBitmap(getResizedBitmap(purple_queen_orange_background, 110, 110), X, Y, null);
                                    break;
                                case 'k':
                                    canvas.drawBitmap(getResizedBitmap(purple_king_orange_background, 110, 110), X, Y, null);
                                    break;
                                default:
                                    canvas.drawBitmap(getResizedBitmap(just_orange, 110, 110), X, Y, null);
                            }
                        }
                    }
                if (!firstCycle) {
                    if ((game.getBoard().isPlayerOneTurn() && playerOneHuman) || (!game.getBoard().isPlayerOneTurn() && playerTwoHuman)) {
                        while (!human_player_moved)
                            try {
                                if (game.getBoard().isPlayerOneTurn()) {
                                    SmartChessStrategySelector testSelector = new SmartChessStrategySelectorP1(game.getBoard(), false);
                                    final Integer[] testChoice = testSelector.getChoice();
                                    try {
                                        int test = testChoice[0];
                                    } catch (NullPointerException npexc) {
                                        isRunning = false;
                                        endGame();
                                        break;
                                    }
                                } else {
                                    SmartChessStrategySelector testSelector = new SmartChessStrategySelectorP2(game.getBoard(), false);
                                    final Integer[] testChoice = testSelector.getChoice();
                                    try {
                                        int test = testChoice[0];
                                    } catch (NullPointerException npexc) {
                                        isRunning = false;
                                        endGame();
                                        break;
                                    }
                                }
                                //Thread.sleep(4000L);
                                Thread.sleep(1L);
                            } catch (InterruptedException iexc) {

                            }
                        game.move(select_x, select_y, destination_x, destination_y);
                        human_player_moved = false;
                        select_x = -1;
                        select_y = -1;
                    } else {
                        firstCycle = true;
                        try {
                            final int[] choice = ChessMoveDispatcher.generateChessMove(game.getBoard(), p1_piece_count, p2_piece_count, previous_p1_piece_counts, previous_p2_piece_counts);
                            game.move(choice[0], choice[1], choice[2], choice[3]);
                            /*try {
                                Thread.sleep(500L);
                            } catch (InterruptedException iexc) {

                            }*/
                        } catch (NullPointerException npexc) {
                            isRunning = false;
                            endGame();
                            break;
                        }
                    }
                } else
                    firstCycle = false;
                int current_p1_piece_count = 0;
                int current_p2_piece_count = 0;
                for (x = 0; x < chessBoardSize; x++)
                    for (y = 0; y < chessBoardSize; y++)
                        switch (pieceGrid[x][y]) {
                            case 'P':
                            case 'R':
                            case 'N':
                            case 'B':
                            case 'Q':
                            case 'K':
                                ++current_p1_piece_count;
                                break;
                            case 'p':
                            case 'r':
                            case 'n':
                            case 'b':
                            case 'q':
                            case 'k':
                                ++current_p2_piece_count;
                        }
                if (current_p1_piece_count < 2 || current_p2_piece_count < 2) {
                    isRunning = false;
                    endGame();
                    break;
                }
                int p;
                for (p = 0; p < 7; p++) {
                    previous_p1_piece_counts[p] = previous_p1_piece_counts[p + 1];
                    previous_p2_piece_counts[p] = previous_p2_piece_counts[p + 1];
                }
                previous_p1_piece_counts[7] = current_p1_piece_count;
                previous_p2_piece_counts[7] = current_p2_piece_count;
                holder.unlockCanvasAndPost(canvas);
            }
        }

        public void pause() {
            isRunning = false;
            while (true) {
                try {
                    thread.join();
                } catch (InterruptedException ie) {
                    Log.e(TAG, ie.getMessage());
                }
                break;
            }
            thread = null;
        }

        public void resume() {
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        }

        private void endGame() {
            try {
                Thread.sleep(8000L);
            } catch (InterruptedException iexc) {

            }
            final char victory_code = game.gameStatus();
            Intent intentGameResults = new Intent(ChessBoardActivity.this, ActivityGameResults.class);
            switch (victory_code) {
                case '1':
                    intentGameResults.putExtra("Winner", "One");
                    break;
                case '2':
                    intentGameResults.putExtra("Winner", "Two");
                    break;
                default:
                    intentGameResults.putExtra("Winner", "None");
            }
            startActivity(intentGameResults);
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}
