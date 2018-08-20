package com.gh.game.platformer.simpletracker;

import com.gh.game.platformer.simpletracker.ControlAreaManager.ControlAreaReport;
import com.gh.game.platformer.simpletracker.ControlAreaManager.TouchEventResult;

import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

import static com.gh.game.platformer.simpletracker.ControlAreaManager.NO_TOUCH_EVENT;
import static com.gh.game.platformer.simpletracker.GlobalGameInfo.getInfo;

public class GameTask extends TimerTask implements Observer {

    /*once every ANIMATION_STEP tics update the graphic window with the new position of the elements */
    public static final int ANIMATION_STEP = 30;

    /* the bigger the value the lower the chance an enemy will spawn  */
    public static final int ENEMY_SPAWN_VALUE = 1000;

    private GameAreaView gameAreaView;
    private volatile TouchEventResult touchPadEvent;
    private volatile boolean fireTouchEvent;
    private int animPos;

    private GameplayController gameplayController;

    public GameTask(GameAreaView gameAreaView, GameplayController gameplayController) {
        this.gameAreaView = gameAreaView;
        this.gameplayController = gameplayController;
        touchPadEvent = NO_TOUCH_EVENT;
        fireTouchEvent = false;
    }

    @Override
    public void run() {
        doWork();
    }

    @Override
    public void update(Observable observable, Object controlAreaReportObject) {
        ControlAreaReport controlAreaReport = (ControlAreaReport)controlAreaReportObject;
        touchPadEvent = controlAreaReport.getTouchPadEvent();
        fireTouchEvent = controlAreaReport.isFire();
    }

    private void doWork() {
        boolean sendDrawCommand = false;

        //TODO: move most of this logic to GameplayController
        if ((int)(ENEMY_SPAWN_VALUE * Math.random()) == ENEMY_SPAWN_VALUE - 1) {
            gameplayController.spawnEnemy();
        }
        if (fireTouchEvent && animPos == ANIMATION_STEP - 1) {
            gameplayController.fireButtonPressed();
            fireTouchEvent = false;
        }

        gameplayController.doGameElementsInteractions(touchPadEvent);

        if (animPos == ANIMATION_STEP - 1) {
            sendDrawCommand = true;
        }

        if (sendDrawCommand) {
            sendDrawCommand();
        }

        if (animPos >= ANIMATION_STEP) {
            animPos = 0;
        } else {
            ++animPos;
        }
    }

    private void sendDrawCommand() {
        getInfo().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameAreaView.invalidate();
            }
        });
    }
}