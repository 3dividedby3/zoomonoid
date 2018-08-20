package com.gh.game.platformer.simpletracker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import java.util.Iterator;

import static com.gh.game.platformer.simpletracker.GlobalGameInfo.getInfo;

public class Score extends DrawableGameElement<Score> {
    public static final String ZOOMONOID_PREFS_NAME = "zoomonoidprefs";
    public static final String HI_SCORE_SETTING = "hiScore";

    public static final int LEFT = 70;
    public static final int TOP = 90;
    public static final int TEXT_SIZE = 55;

    private int score;

    public Score() {
        super(0);
        setHeight(TEXT_SIZE);
        setWidth(10);
        setX(LEFT);
        setY(TOP);
    }

    @Override
    protected void init(Activity gameActivity) {
    }

    @Override
    public boolean move(ControlAreaManager.TouchEventResult touchEventResult, Iterator<Score> iteratorElements) {
        //nothing - stationary
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#bbffff33"));
//        paint.setShadowLayer(20f, 50, 50, Color.GRAY);
        paint.setTextSize(TEXT_SIZE);
        canvas.drawText("Score: " + score, getX(), getY(), paint);
    }

    @Override
    public Drawable getDrawable() {
        //no drawable
        return null;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void updateScoreWith(int updateWith) {
        score = score + updateWith;
    }


    public void saveScore() {
        SharedPreferences settings = getPreferences();
        if (settings.getInt(HI_SCORE_SETTING, 0)  < getScore()) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(HI_SCORE_SETTING, getScore());
            editor.apply();
        }
    }

    public int getHiScore() {
        SharedPreferences settings = getPreferences();
        return settings.getInt(HI_SCORE_SETTING, 0);
    }

    private SharedPreferences getPreferences() {
        return getInfo().getActivity().getSharedPreferences(ZOOMONOID_PREFS_NAME, 0);
    }
}
