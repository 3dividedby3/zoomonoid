package com.gh.game.platformer.simpletracker;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.gh.game.platformer.simpletracker.ControlAreaManager.TouchEventResult;
import static java.lang.Math.round;
import java.util.Iterator;

import static com.gh.game.platformer.simpletracker.GlobalGameInfo.getInfo;

public abstract class DrawableGameElement<T extends DrawableGameElement> {
    private float x;
    private float y;
    private int width;
    private int height;
    /* does not have to be unique */
    private int elementId;

    protected DrawableGameElement(int elementId) {
        this.elementId = elementId;
        init(getInfo().getActivity());
    }

    protected abstract void init(Activity gameActivity);

    /* return true if elements was removed after move */
    public abstract boolean move(TouchEventResult touchEventResult, Iterator<T> iteratorElements);

    public void draw(Canvas canvas) {
        Drawable drawable = getDrawable();
        updateBounds(drawable);
        drawable.draw(canvas);
    }

    public Rect toRect() {
        int x = round(getX());
        int y = round(getY());
        return new Rect(x, y, x + getWidth(), y + getHeight());
    }

    public boolean isLessThanScreenWidth() {
        return getX() + getWidth() < getInfo().getScreenWidth();
    }

    public boolean isLessThanScreenHeight() {
        return getY() + getHeight() < getInfo().getScreenHeight();
    }

    public boolean isMoreThanScreenHeight() {
        return !isLessThanScreenHeight();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public abstract Drawable getDrawable();

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public int getElementId() {
        return elementId;
    }

    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    public void updateBounds(Drawable drawable) {
        int roundedX = round(getX());
        int roundedY = round(getY());
        drawable.setBounds(roundedX, roundedY, getWidth() + roundedX, getHeight() + roundedY);
    }

    @Override
    public String toString() {
        return "DrawableGameElement{" +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", elementId=" + elementId +
                '}';
    }
}
