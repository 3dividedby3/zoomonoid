package com.gh.game.platformer.simpletracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import static com.gh.game.platformer.simpletracker.Enemy.DIRECTION_LEFT;
import static com.gh.game.platformer.simpletracker.Enemy.DIRECTION_NONE;
import static com.gh.game.platformer.simpletracker.Enemy.DIRECTION_RIGHT;
import static com.gh.game.platformer.simpletracker.GlobalGameInfo.getInfo;
import static java.lang.String.format;

public abstract class DirectionalXDrawableGameElement<T extends DrawableGameElement> extends DrawableGameElement<T> {
    //<TYPE:1, 2,..>_<normal, left, right>
    public static final String KEY_DRAWABLE_ID_NORMAL = "drawable_%s_normal";
    public static final String KEY_DRAWABLE_ID_LEFT = "drawable_%s_left";
    public static final String KEY_DRAWABLE_ID_RIGHT = "drawable_%s_right";

    private int directionX = DIRECTION_NONE;
    private int inclineAngle;

    protected DirectionalXDrawableGameElement(int inclineAngle, int elementId) {
        super(elementId);
        this.inclineAngle = inclineAngle;
    }

    @Override
    protected void init(Activity activity) {
        //eager init for normal drawing, but for left and  right use lazy
        Drawable drawable = activity.getDrawable(getElementId());
        if (!getInfo().getDrawableCacheManager().contains(getKeyNormal())) {
            getInfo().getDrawableCacheManager().put(getKeyNormal(), drawable);
        }
        setWidth(drawable.getIntrinsicWidth() / 2);
        setHeight(drawable.getIntrinsicHeight() / 2);
    }

    protected String getKeyNormal() {
        return format(KEY_DRAWABLE_ID_NORMAL, getElementId());
    }

    protected String getKeyLeft() {
        return format(KEY_DRAWABLE_ID_LEFT, getElementId());
    }

    protected String getKeyRight() {
        return format(KEY_DRAWABLE_ID_RIGHT, getElementId());
    }

    public int getDirectionX() {
        return directionX;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public int getInclineAngle() {
        return inclineAngle;
    }

    public void setInclineAngle(int inclineAngle) {
        this.inclineAngle = inclineAngle;
    }

    @Override
    public Drawable getDrawable() {
        switch(getDirectionX()) {
            case DIRECTION_LEFT:
                Drawable drawableMoveLeft = getInfo().getDrawableCacheManager().get(getKeyLeft());
                if (drawableMoveLeft == null) {
                    drawableMoveLeft = computeTransformedDrawable(getInclineAngle());
                    getInfo().getDrawableCacheManager().put(getKeyLeft(), drawableMoveLeft);
                }
                return drawableMoveLeft;
            case DIRECTION_RIGHT:
                Drawable drawableMoveRight = getInfo().getDrawableCacheManager().get(getKeyRight());
                if (drawableMoveRight == null) {
                    drawableMoveRight = computeTransformedDrawable(-getInclineAngle());
                    getInfo().getDrawableCacheManager().put(getKeyRight(), drawableMoveRight);
                }
                return drawableMoveRight;
            case DIRECTION_NONE:
            default:
                return getInfo().getDrawableCacheManager().get(getKeyNormal());
        }
    }

    private Drawable computeTransformedDrawable(int angle) {
        Bitmap bitmap = convertNormalDrawableToBitmap();
        Matrix matrix = new Matrix();
        Camera camera = new Camera();
        int rotationAngle = angle;
        //TODO: update width of drawable game element according to inclination angle
//        int reductionInWidthPercent = rotationAngle * 100/89; //89 is the maximum rotation angle
//        int newWidth = (bitmap.getWidth() - bitmap.getWidth() * reductionInWidthPercent / 100)/2;
        camera.setLocation(1, 1, 8);
        camera.rotateY(rotationAngle);
        camera.getMatrix(matrix);

        Bitmap bitmapTransformed = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return new BitmapDrawable(getInfo().getActivity().getResources(), bitmapTransformed);
    }

    private Bitmap convertNormalDrawableToBitmap() {
        Drawable drawableNormal = getInfo().getDrawableCacheManager().get(getKeyNormal());
        Bitmap bitmap = Bitmap.createBitmap(drawableNormal.getIntrinsicWidth(), drawableNormal.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawableNormal.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawableNormal.draw(canvas);
        return bitmap;
    }
}
