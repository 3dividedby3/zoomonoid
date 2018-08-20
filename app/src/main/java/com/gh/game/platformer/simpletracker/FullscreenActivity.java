package com.gh.game.platformer.simpletracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.gh.game.platformer.simpletracker.Score.HI_SCORE_SETTING;
import static com.gh.game.platformer.simpletracker.Score.ZOOMONOID_PREFS_NAME;

public class FullscreenActivity extends Activity {

    private Score score;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (score != null) {
            score.saveScore();
        }
    }

    private void init() {
        //Remove title bar and set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //disable screen rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //do this before creating any views
        GlobalGameInfo.init(this);

        setContentView(R.layout.activity_fullscreen);

        TextView hiScore = findViewById(R.id.hiScore);
        score = new Score();
        hiScore.setText(hiScore.getText() + "" + score.getHiScore());

        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControlAreaManager controlAreaManager = new ControlAreaManager(FullscreenActivity.this);
                GameAreaView gameAreaView = new GameAreaView(FullscreenActivity.this, controlAreaManager, new GameplayController(controlAreaManager.getRadius(), score));
                gameAreaView.setBackground(FullscreenActivity.this.getDrawable(R.drawable.bkg_5));
                //the line below is very important
                setContentView(gameAreaView);
                gameAreaView.setOnTouchListener(controlAreaManager);
            }
        });
    }
}
