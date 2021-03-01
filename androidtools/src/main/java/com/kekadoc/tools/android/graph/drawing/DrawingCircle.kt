package com.kekadoc.tools.android.graph.drawing

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.kekadoc.tools.android.graph.drawing.Drawing

class DrawingCircle(var bounds: RectF, var paint: Paint) : Drawing() {

    var startAngle = 270f
    var sweepAngle = 360f

    fun setStartAngleFraction(fraction: Float) {
        startAngle = 360f * fraction
    }
    fun setSweepAngleFraction(fraction: Float) {
        sweepAngle *= 360f * fraction
    }

    override fun draw(canvas: Canvas) {
        canvas.drawArc(bounds, startAngle, sweepAngle, false, paint)
    }

}