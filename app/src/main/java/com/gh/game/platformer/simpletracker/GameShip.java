package com.gh.game.platformer.simpletracker;

import com.gh.game.platformer.simpletracker.ControlAreaManager.TouchEventResult;

import java.util.Iterator;

import static com.gh.game.platformer.simpletracker.ControlAreaManager.NO_TOUCH_EVENT;
import static com.gh.game.platformer.simpletracker.Enemy.DIRECTION_NONE;
import static java.lang.String.format;

public class GameShip extends DirectionalXDrawableGameElement<GameShip> {
    //ship_<TYPE:1, 2,..>_<normal, left, right>
    public static final String KEY_DRAWABLE_SHIP_ID_NORMAL = "drawable_ship_%s_normal";
    public static final String KEY_DRAWABLE_SHIP_ID_LEFT = "drawable_ship_%s_left";
    public static final String KEY_DRAWABLE_SHIP_ID_RIGHT = "drawable_ship_%s_right";

    //TODO: calculate angle according to touchEventResult.getOffsetX
    private static final int MOVE_INCLINE_ANGLE = 35;

    private float controlAreaRadius;

    public GameShip(float x, float y, int elementId) {
        super(MOVE_INCLINE_ANGLE, elementId);
        setX(x);
        setY(y - getHeight());
    }

    public void setControlAreaRadius(float controlAreaRadius) {
        this.controlAreaRadius = controlAreaRadius;
    }

    @Override
    protected String getKeyNormal() {
        return format(KEY_DRAWABLE_SHIP_ID_NORMAL, getElementId());
    }

    @Override
    protected String getKeyLeft() {
        return format(KEY_DRAWABLE_SHIP_ID_LEFT, getElementId());
    }

    @Override
    protected String getKeyRight() {
        return format(KEY_DRAWABLE_SHIP_ID_RIGHT, getElementId());
    }

    @Override
    public boolean move(TouchEventResult touchEventResult, Iterator<GameShip> iteratorElements) {
        if (touchEventResult == NO_TOUCH_EVENT) {
            setDirectionX(DIRECTION_NONE);
            return false;
        }

        setDirectionX((int)Math.signum(touchEventResult.getOffsetX()));
        float ratioX = getDirectionX() * 1 / (controlAreaRadius/Math.abs(touchEventResult.getOffsetX()));
        float ratioY = Math.signum(touchEventResult.getOffsetY()) * 1 / (controlAreaRadius/Math.abs(touchEventResult.getOffsetY()));

        if ((ratioX < 0 && getX() > 0) || (ratioX >= 0 && isLessThanScreenWidth())) {
            setX(getX() + ratioX);
        }
        if ((ratioY < 0 && getY() > 0) || (ratioY >= 0 && isLessThanScreenHeight())) {
            setY(getY() + ratioY);
        }

        return false;
    }

}
