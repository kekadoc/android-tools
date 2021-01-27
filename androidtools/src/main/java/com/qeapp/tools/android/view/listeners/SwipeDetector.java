package com.qeapp.tools.android.view.listeners;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SwipeDetector implements View.OnTouchListener, OnSwipeListener {
    private static final String TAG = "SwipeDetector-TAG";

    public static void injectDetector(View view, OnSwipeListener listener) {
        view.setOnTouchListener(new SwipeDetector(listener));
    }

    private float touchStartX;
    private float touchStartY;
    private float oldMoveX;
    private float oldMoveY;

    private float alternativeDistY;
    private float alternativeDistX;

    @Nullable
    private OnSwipeListener onSwipeListener;

    public SwipeDetector(@Nullable OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }
    public SwipeDetector() {
    }

    //region Getters/Setters

    @Nullable
    public OnSwipeListener getOnSwipeListener() {
        return onSwipeListener;
    }
    public void setOnSwipeListener(@Nullable OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    //endregion

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartX = oldMoveX = x;
                touchStartY = oldMoveY = y;
                onDown(touchStartX, touchStartY);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                onUp(x, y, x - touchStartX, y - touchStartY);
                touchStartX = touchStartY = 0f;
                alternativeDistX = alternativeDistY = 0f;
                oldMoveX = oldMoveY = 0f;
                break;
            case MotionEvent.ACTION_MOVE:
                if (x < 0 || y < 0) break;
                float dxMove = x - oldMoveX;
                float dyMove = y - oldMoveY;

                this.alternativeDistX += dxMove;
                this.alternativeDistY += dyMove;
                onMove(this.alternativeDistX, this.alternativeDistY, dxMove, dyMove);

                if (Math.abs(dxMove) > Math.abs(dyMove)) {
                    if (dxMove > 0) onSwipeLeft(this.alternativeDistX, dxMove);
                    else onSwipeRight(this.alternativeDistX, dxMove);
                } else {
                    if (dyMove > 0) onSwipeBottom(this.alternativeDistY, dyMove);
                    else onSwipeTop(this.alternativeDistY, dyMove);
                }

                break;
        }
        this.oldMoveX = x;
        this.oldMoveY = y;
        return hasConsumed(v, event);
    }

    @Override
    public final boolean hasConsumed(View view, MotionEvent event) {
        return onSwipeListener != null && onSwipeListener.hasConsumed(view, event);
    }

    @Override
    public final void onMove(float dxStart, float dyStart, float dx, float dy) {
        if (onSwipeListener != null) onSwipeListener.onMove(dxStart, dyStart, dx, dy);
    }
    @Override
    public final void onUp(float x, float y, float dxStart, float dyStart) {
        if (onSwipeListener != null) onSwipeListener.onUp(x, y, dxStart, dyStart);
    }
    @Override
    public final void onDown(float x, float y) {
        if (onSwipeListener != null) onSwipeListener.onDown(x, y);
    }
    @Override
    public final void onSwipeLeft(float startDistance, float distance) {
        if (onSwipeListener != null) onSwipeListener.onSwipeLeft(startDistance, distance);
    }
    @Override
    public final void onSwipeTop(float startDistance, float distance) {
        if (onSwipeListener != null) onSwipeListener.onSwipeTop(startDistance, distance);
    }
    @Override
    public final void onSwipeRight(float startDistance, float distance) {
        if (onSwipeListener != null) onSwipeListener.onSwipeRight(startDistance, distance);
    }
    @Override
    public final void onSwipeBottom(float startDistance, float distance) {
        if (onSwipeListener != null) onSwipeListener.onSwipeBottom(startDistance, distance);
    }

}
