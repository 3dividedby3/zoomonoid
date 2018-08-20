package com.gh.game.platformer.simpletracker;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Observable;

public class ControlAreaManager extends Observable implements View.OnTouchListener {

    public static final TouchEventResult NO_TOUCH_EVENT = new TouchEventResult(false, -1, -1);

    public static final int DEFAULT_X_OFFSET = 200;
    public static final int DEFAULT_Y_OFFSET = 200;
    public static final int DEFAULT_RADIUS = 100;

    private int controlCenterX;
    private int controlCenterY;
    private int radius = DEFAULT_RADIUS;

    public ControlAreaManager(Context context) {
        init(context);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        notifyObservers(trackControlArea(motionEvent));

        return true;
    }

    @Override
    public void notifyObservers(Object controlAreaReport) {
        setChanged();
        super.notifyObservers(controlAreaReport);
    }

    protected void init(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        controlCenterX = displayMetrics.widthPixels - DEFAULT_X_OFFSET;
        controlCenterY = displayMetrics.heightPixels - DEFAULT_Y_OFFSET;
    }

    public int getControlCenterX() {
        return controlCenterX;
    }

    public int getControlCenterY() {
        return controlCenterY;
    }

    public int getRadius() {
        return radius;
    }

    private ControlAreaReport trackControlArea(MotionEvent motionEvent) {
        TouchEventResult touchPad = NO_TOUCH_EVENT;
        boolean fire = false;

        //go over all event and use last of each for pad touched or fire touched
        for (int eventIdx = 0; eventIdx < motionEvent.getPointerCount(); ++eventIdx) {
            TouchEventResult currentResult = extractDataForEvent(motionEvent, eventIdx);
            if (currentResult.isInsideControlArea()) {
                touchPad = currentResult;
            } else {
                fire = true;
            }
        }

        //overwrite pad or fire with active pointer
        int idxActivePointer = motionEvent.getActionIndex();
        int action = motionEvent.getActionMasked();
        TouchEventResult activePointerResult = extractDataForEvent(motionEvent, idxActivePointer);
        switch(action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (activePointerResult.isInsideControlArea()) {
                    touchPad = activePointerResult;
                } else {
                    fire = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (activePointerResult.isInsideControlArea()) {
                    touchPad = NO_TOUCH_EVENT;
                } else {
                    fire = false;
                }
                break;
            default:
        }

        return new ControlAreaReport(touchPad, fire);
    }

    private TouchEventResult extractDataForEvent(MotionEvent motionEvent, int idx) {
        int touchX = (int)motionEvent.getX(idx);
        int touchY = (int)motionEvent.getY(idx);
        int offsetX = touchX - controlCenterX;
        int offsetY = touchY - controlCenterY;
        boolean isInside = Math.pow(offsetX, 2) + Math.pow(offsetY, 2) < Math.pow(radius, 2);
        return new TouchEventResult(isInside, offsetX, offsetY);
    }

    //=============================//

    public static class TouchEventResult {
        private final boolean isInside;
        private final int offsetX;
        private final int offsetY;

        private TouchEventResult(boolean isInside, int offsetX, int offsetY) {
            this.isInside = isInside;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public boolean isInsideControlArea() {
            return isInside;
        }

        public int getOffsetX() {
            return offsetX;
        }

        public int getOffsetY() {
            return offsetY;
        }

        @Override
        public String toString() {
            return "TouchEventResult{" +
                    "isInside=" + isInside +
                    ", offsetX=" + offsetX +
                    ", offsetY=" + offsetY +
                    '}';
        }
    }

    public static class ControlAreaReport {
        private final TouchEventResult touchPadEvent;
        private final boolean fire;

        private  ControlAreaReport(TouchEventResult touchPadEvent, boolean fire) {
            this.touchPadEvent = touchPadEvent;
            this.fire = fire;
        }

        public TouchEventResult getTouchPadEvent() {
            return touchPadEvent;
        }

        public boolean isFire() {
            return fire;
        }
    }
}
