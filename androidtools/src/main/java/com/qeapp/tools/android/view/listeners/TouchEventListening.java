package com.qeapp.tools.android.view.listeners;

import android.view.MotionEvent;

public interface TouchEventListening {

    static void invoke(TouchEventListening listener, MotionEvent event) {
        if (listener == null) return;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: listener.onTouchDown(event);
            case MotionEvent.ACTION_UP: listener.onTouchUp(event);
            case MotionEvent.ACTION_MOVE: listener.onTouchMove(event);
            case MotionEvent.ACTION_CANCEL: listener.onTouchCancel(event);
        }
    }

    default void onTouchDown(MotionEvent event) {}
    default void onTouchUp(MotionEvent event) {}
    default void onTouchMove(MotionEvent event) {}
    default void onTouchCancel(MotionEvent event) {}

}
