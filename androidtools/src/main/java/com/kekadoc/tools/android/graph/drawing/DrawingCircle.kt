package com.kekadoc.tools.android.graph.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

public final class CircleDrawing extends Drawing {

    @NonNull
    private RectF rectF;
    @NonNull
    private Paint paint;
    private float startAngle = 270;
    private float sweepAngle = 360;

    public CircleDrawing(@NonNull RectF rectF, @NonNull Paint paint) {
        this.rectF = rectF;
        this.paint = paint;
    }

    @NonNull
    public final RectF getRectF() {
        return rectF;
    }
    public final void setRectF(@NonNull RectF rectF) {
        this.rectF = rectF;
    }

    @NonNull
    public final Paint getPaint() {
        return paint;
    }
    public final void setPaint(@NonNull Paint paint) {
        this.paint = paint;
    }

    public final float getStartAngle() {
        return startAngle;
    }
    public final void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public final float getSweepAngle() {
        return sweepAngle;
    }
    public final void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    @Override
    public final void draw(@NonNull Canvas canvas) {
        canvas.drawArc(getBound(), getStartAngle(), getSweepAngle(), false, paint);
    }

    @NonNull
    @Override
    protected final RectF getBound() {
        return rectF;
    }

}
