package com.bignerdranch.android.adam_chess_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CheckBox playerOneHuman;
    private CheckBox playerTwoHuman;
    private Button btnBeginGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intentBoard = new Intent(MainActivity.this, ChessBoardActivity.class);
        playerOneHuman = (CheckBox) findViewById(R.id.checkPlayerOneHuman);
        playerTwoHuman = (CheckBox) findViewById(R.id.checkPlayerTwoHuman);
        btnBeginGame = (Button) findViewById(R.id.btnBeginGame);
        btnBeginGame.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent me) {
                if (playerOneHuman.isChecked())
                    intentBoard.putExtra("playerOneHuman", true);
                else
                    intentBoard.putExtra("playerOneHuman", false);
                if (playerTwoHuman.isChecked())
                    intentBoard.putExtra("playerTwoHuman", true);
                else
                    intentBoard.putExtra("playerTwoHuman", false);
                startActivity(intentBoard);
                return true;
            }
        });
    }

    public static int[] generateRandomIntegerIndices(int n) {
        int[] A = new int[n];
        int i;
        for (i = 0; i < n; i++)
            A[i] = i;
        int rand, temp;
        for (i = 0; i < n; i++) {
            rand = (int) (Math.random() * n);
            temp = A[i];
            A[i] = A[rand];
            A[rand] = temp;
        }
        return A;
    }

}
