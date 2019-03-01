package com.bignerdranch.android.adam_chess_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityGameResults extends Activity {

    TextView txtVictory;
    Button btnBackToGameSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_results);
        txtVictory = (TextView) findViewById(R.id.txtVictory);
        btnBackToGameSettings = (Button) findViewById(R.id.btnBackToGameSettings);
        Intent myIntent = getIntent();
        String winner = myIntent.getStringExtra("Winner");
        if (winner.equalsIgnoreCase("One"))
            txtVictory.setText(R.string.player_one_victory);
        else if (winner.equalsIgnoreCase("Two"))
            txtVictory.setText(R.string.player_two_victory);
        else
            txtVictory.setText(R.string.draw);
        final Intent intentMain = new Intent(ActivityGameResults.this, MainActivity.class);
        btnBackToGameSettings.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent me) {
                startActivity(intentMain);
                return true;
            }
        });

    }
}
