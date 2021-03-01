package com.kekadoc.tools.android.graph.drawing

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.kekadoc.tools.android.graph.drawing.Drawing

open class DrawingCircle(var bounds: RectF = RectF(), var paint: Paint) : Drawing() {

    var startAngle = 270f
    var sweepAngle = 360f
    var useCenter = false

    fun setStartAngleFraction(fraction: Float) {
        startAngle = 360f * fraction
    }
    fun setSweepAngleFraction(fraction: Float) {
        sweepAngle = 360f * fraction
    }

    override fun draw(canvas: Canvas) {
        canvas.drawArc(bounds, startAngle, sweepAngle, useCenter, paint)
    }

    override fun toString(): String {
        return "DrawingCircle(bounds=$bounds, paint=$paint, startAngle=$startAngle, sweepAngle=$sweepAngle)"
    }

}