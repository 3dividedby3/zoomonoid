package com.gh.game.platformer.simpletracker;

import com.gh.game.platformer.simpletracker.ControlAreaManager.TouchEventResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.gh.game.platformer.simpletracker.GlobalGameInfo.getInfo;

public class Enemy extends DirectionalXDrawableGameElement<Enemy> implements Scorable {
    public static final int DIRECTION_LEFT = -1;
    public static final int DIRECTION_NONE = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final List<Integer> POSSIBLE_DIRECTIONS = Collections.unmodifiableList(Arrays.asList(DIRECTION_LEFT, DIRECTION_NONE, DIRECTION_RIGHT));

    /* once every BASE_FLIP_POINT + random * SENSIBILITY moves consider changing/flip direction - the bigger the number the lower the chance to flip */
    private static final int BASE_FLIP_POINT = 500;
    private static final int SENSIBILITY = 50;
    private static final int MOVE_INCLINE_ANGLE = 30;

    /* the higher the faster the enemy CAN move on both x and y axis - must be between 0 and 99, otherwise unexpected behaviour like missed collisions can occur */
    private static final int DEFAULT_TRAVEL_SPEED = 20;

    /* score value */
    private int valuedAt = 1;

    private int iteration;
    private int flipPoint;
    private float speedX;
    private float speedY;


    public Enemy() {
        this(0, 0, R.drawable.enemy_2_octopus, DEFAULT_TRAVEL_SPEED, 1);
    }

    public Enemy(float x, float y, int elementId, int baseTravelSpeed, int valuedAt) {
        super(MOVE_INCLINE_ANGLE, elementId);
        setX(x);
        setY(y);
        this.valuedAt = valuedAt;
        speedX = (float)(1 + baseTravelSpeed * Math.random())/100;
        speedY = (float)(1 + baseTravelSpeed * Math.random())/100;
        setNextBehaviorData();
    }

    @Override
    public boolean move(TouchEventResult touchEventResult, Iterator<Enemy> iteratorElements) {
        ++iteration;
        setY(getY() + speedY);
        if (iteration == flipPoint) {
            setNextBehaviorData();
            iteration = 0;
        }
        float newX = getX() + getDirectionX() * speedX;
        if(newX > 0 && newX < getInfo().getScreenWidth() - getWidth()) {
            setX(newX);
        }
        if (isMoreThanScreenHeight()) {
            iteratorElements.remove();
            return true;
        }

        return false;
    }

    protected void setValue(int value) {
        valuedAt = value;
    }

    @Override
    public int getValue() {
        return valuedAt;
    }

    private void setNextBehaviorData() {
        flipPoint = BASE_FLIP_POINT + (int)(SENSIBILITY * Math.random());
        setDirectionX(POSSIBLE_DIRECTIONS.get((int)(POSSIBLE_DIRECTIONS.size() * Math.random())));
    }
}
