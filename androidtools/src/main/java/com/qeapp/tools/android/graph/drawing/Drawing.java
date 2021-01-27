package com.qeapp.tools.android.graph.drawing;

import android.graphics.Canvas;
import android.graphics.RectF;

import androidx.annotation.NonNull;

public abstract class Drawing {

    public abstract void draw(@NonNull Canvas canvas);

    @NonNull
    protected abstract RectF getBound();

}
