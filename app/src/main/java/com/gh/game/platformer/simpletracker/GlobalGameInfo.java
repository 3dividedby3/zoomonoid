package com.gh.game.platformer.simpletracker;

import android.app.Activity;
import android.util.DisplayMetrics;

public final class GlobalGameInfo {

    private final int screenHeight;
    private final int screenWidth;
    private final Activity gameActivity;
    private final DrawableCacheManager drawableCacheManager;

    private static GlobalGameInfo instance;

    private GlobalGameInfo(Activity gameActivity) {
        this.gameActivity = gameActivity;
        DisplayMetrics displayMetrics = gameActivity.getResources().getDisplayMetrics();
        screenHeight =  displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        drawableCacheManager = new DrawableCacheManager();
    }

    public static synchronized void init(Activity gameActivity) {
        GlobalGameInfo.instance = new GlobalGameInfo(gameActivity);
    }

    public static GlobalGameInfo getInfo() {
        if (instance == null) {
            throw new IllegalStateException("You forgot to init the global game info singleton");
        }

        return instance;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public Activity getActivity() {
        return gameActivity;
    }

    public DrawableCacheManager getDrawableCacheManager() {
        return drawableCacheManager;
    }
}
