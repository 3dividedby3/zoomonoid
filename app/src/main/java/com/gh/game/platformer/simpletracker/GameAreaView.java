package com.gh.game.platformer.simpletracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

import java.util.Timer;

import static com.gh.game.platformer.simpletracker.GlobalGameInfo.getInfo;

public class GameAreaView extends GLSurfaceView {

    private GameplayController gameplayController;
    private ControlAreaManager controlAreaManager;
    private Drawable shipControlArea;
    private Timer gameTimer;

    public GameAreaView(Context context, ControlAreaManager controlAreaManager, GameplayController gameplayController) {
        super(context);
        this.controlAreaManager = controlAreaManager;
        this.gameplayController = gameplayController;
        init();
    }

    protected void init() {
        getHolder().addCallback(this);
        GlobalGameInfo pi = getInfo();
        shipControlArea = pi.getActivity().getDrawable(R.drawable.ship_control_area);
        int radius = controlAreaManager.getRadius();
        shipControlArea.setBounds(controlAreaManager.getControlCenterX() - radius, controlAreaManager.getControlCenterY() - radius,
                controlAreaManager.getControlCenterX() + radius, controlAreaManager.getControlCenterY() + radius);
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void onDraw(Canvas canvas) {
        //shipControlArea is a visual cue for the player to know where to hold to control the ship
        shipControlArea.draw(canvas);
        for (DrawableGameElement drawable : gameplayController.getAllDrawables()) {
            drawable.draw(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameTimer = new Timer();
        GameTask gameTask = new GameTask(this, gameplayController);
        controlAreaManager.addObserver(gameTask);
        gameTimer.schedule(gameTask, 0, 1);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
