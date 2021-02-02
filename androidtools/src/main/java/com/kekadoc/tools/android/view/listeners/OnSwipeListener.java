package com.kekadoc.tools.android.view.listeners;

import android.view.MotionEvent;
import android.view.View;

public interface OnSwipeListener {

    default boolean hasConsumed(View view, MotionEvent event) {
        return false;
    }

    void onUp(float x, float y, float dxStart, float dyStart);
    void onDown(float x, float y);

    void onMove(float dxStart, float dyStart, float dx, float dy);

    void onSwipeLeft(float startDistance, float distance);
    void onSwipeTop(float startDistance, float distance);
    void onSwipeRight(float startDistance, float distance);
    void onSwipeBottom(float startDistance, float distance);

}
