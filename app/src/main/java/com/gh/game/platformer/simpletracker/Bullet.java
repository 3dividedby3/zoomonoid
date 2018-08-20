package com.gh.game.platformer.simpletracker;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.gh.game.platformer.simpletracker.ControlAreaManager.TouchEventResult;

import java.util.Iterator;

import static com.gh.game.platformer.simpletracker.GlobalGameInfo.getInfo;

public class Bullet extends DrawableGameElement<Bullet> {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;

    //only 1 type of bullet available for now
    public static final String KEY_DRAWABLE_BULLET = "drawable_bullet";

    public Bullet(float x, float y) {
        super(R.drawable.bullet_type_1);
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setX(x - WIDTH/2);
        setY(y - HEIGHT);
    }

    @Override
    protected void init(Activity activity) {
        if (!getInfo().getDrawableCacheManager().contains(KEY_DRAWABLE_BULLET)) {
            getInfo().getDrawableCacheManager().put(KEY_DRAWABLE_BULLET, activity.getDrawable(getElementId()));
        }
    }

    @Override
    public boolean move(TouchEventResult touchEventResult, Iterator<Bullet> iteratorElements) {
        setY(getY() - 1);

        if (getY() <= -getHeight() - 1) {
            iteratorElements.remove();
            return true;
        }

        return false;
    }

    @Override
    public Drawable getDrawable() {
        return getInfo().getDrawableCacheManager().get(KEY_DRAWABLE_BULLET);
    }
}
