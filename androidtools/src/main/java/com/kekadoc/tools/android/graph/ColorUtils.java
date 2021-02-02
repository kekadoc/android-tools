package com.kekadoc.tools.android.graph;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;

import java.util.Random;

public final class ColorUtils {

    private ColorUtils() {}

    public static @ColorInt int getColorFraction(float fraction, @ColorInt int startColor, @ColorInt int endColor) {
        float startA = ((startColor >> 24) & 0xff) / 255.0f;
        float startR = ((startColor >> 16) & 0xff) / 255.0f;
        float startG = ((startColor >>  8) & 0xff) / 255.0f;
        float startB = ( startColor        & 0xff) / 255.0f;

        float endA = ((endColor >> 24) & 0xff) / 255.0f;
        float endR = ((endColor >> 16) & 0xff) / 255.0f;
        float endG = ((endColor >>  8) & 0xff) / 255.0f;
        float endB = ( endColor        & 0xff) / 255.0f;

        // convert from sRGB to linear
        startR = (float) Math.pow(startR, 2.2);
        startG = (float) Math.pow(startG, 2.2);
        startB = (float) Math.pow(startB, 2.2);

        endR = (float) Math.pow(endR, 2.2);
        endG = (float) Math.pow(endG, 2.2);
        endB = (float) Math.pow(endB, 2.2);

        // compute the interpolated color in linear space
        float a = startA + fraction * (endA - startA);
        float r = startR + fraction * (endR - startR);
        float g = startG + fraction * (endG - startG);
        float b = startB + fraction * (endB - startB);

        // convert back to sRGB in the [0..255] range
        a = a * 255.0f;
        r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
        g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
        b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;

        return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
    }

    @ColorInt
    public static int setAlpha(@ColorInt int color, @FloatRange(from = 0f, to = 1f) float alpha) {
        return androidx.core.graphics.ColorUtils.setAlphaComponent(color, (int) (255 * alpha));
    }

    @ColorInt
    public static int getRandomColor() {
        return getRandomColor(255);
    }
    @ColorInt
    public static int getRandomColor(int alpha) {
        Random random = new Random();
        return Color.argb(alpha,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
        );
    }


    /**  */
    @ColorInt
    public static int rgb(@IntRange(from = 0, to = 255) int red,
                          @IntRange(from = 0, to = 255) int green,
                          @IntRange(from = 0, to = 255) int blue) {
        return 0xff000000 | (red << 16) | (green << 8) | blue;
    }
    /**  */
    @ColorInt
    public static int rgb(@FloatRange(from = 0, to = 1f) float red,
                          @FloatRange(from = 0, to = 1f) float green,
                          @FloatRange(from = 0, to = 1f) float blue) {
        return 0xff000000 |
                ((int) (red * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) << 8) |
                (int) (blue * 255.0f + 0.5f);
    }
    /**  */
    @ColorInt
    public static int argb(@IntRange(from = 0, to = 255) int alpha,
                           @IntRange(from = 0, to = 255) int red,
                           @IntRange(from = 0, to = 255) int green,
                           @IntRange(from = 0, to = 255) int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
    /**  */
    @ColorInt
    public static int argb(@FloatRange(from = 0, to = 1f) float alpha,
                           @FloatRange(from = 0, to = 1f) float red,
                           @FloatRange(from = 0, to = 1f) float green,
                           @FloatRange(from = 0, to = 1f) float blue) {
        return ((int) (alpha * 255.0f + 0.5f) << 24) |
                ((int) (red * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) <<  8) |
                (int) (blue  * 255.0f + 0.5f);
    }

}
