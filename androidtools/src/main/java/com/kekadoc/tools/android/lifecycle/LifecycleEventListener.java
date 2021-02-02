package com.kekadoc.tools.android.lifecycle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

public class LifecycleEventListener implements LifecycleEventObserver {

    @Override
    @CallSuper
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_START) onStart(source);
        if (event == Lifecycle.Event.ON_CREATE) onCreate(source);
        if (event == Lifecycle.Event.ON_RESUME) onResume(source);
        if (event == Lifecycle.Event.ON_PAUSE) onPause(source);
        if (event == Lifecycle.Event.ON_STOP) onStop(source);
        if (event == Lifecycle.Event.ON_DESTROY) onDestroy(source);
        if (event == Lifecycle.Event.ON_ANY) onAny(source);
    }

    public void onStart(@NonNull LifecycleOwner source) {}
    public void onCreate(@NonNull LifecycleOwner source) {}
    public void onResume(@NonNull LifecycleOwner source) {}
    public void onPause(@NonNull LifecycleOwner source) {}
    public void onStop(@NonNull LifecycleOwner source) {}
    public void onDestroy(@NonNull LifecycleOwner source) {}
    public void onAny(@NonNull LifecycleOwner source) {}

}
