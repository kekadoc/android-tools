package com.kekadoc.tools.android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.DrawableMarginSpan;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Отображение иконки относительно текста
 */
public class IconSpan extends DrawableMarginSpan {
    private static final String TAG = "IconSpan-TAG";

    public static final int ALIGN_DEFAULT = 0;
    public static final int ALIGN_TOP = 1;
    public static final int ALIGN_CENTER = 2;
    public static final int ALIGN_BASELINE = 3;
    public static final int ALIGN_BOTTOM = 4;

    @IntDef({ALIGN_DEFAULT, ALIGN_TOP, ALIGN_CENTER, ALIGN_BASELINE, ALIGN_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AlignType {}

    private final Drawable mDrawable;
    private final  @AlignType int verticalAlignment;
    private final int imageWidth, imageHeight, padding;

    public IconSpan(@NonNull Drawable drawable, @AlignType int verticalAlignment, int imageWidth, int imageHeight, int padding) {
        super(drawable, padding);
        this.padding = padding;
        this.verticalAlignment = verticalAlignment;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.mDrawable = drawable;
    }
    public IconSpan(@NonNull Drawable drawable, @AlignType int verticalAlignment, int imageSize, int padding) {
        this(drawable, verticalAlignment, imageSize, imageSize, padding);
    }

    public IconSpan(@NonNull Drawable drawable, @AlignType int verticalAlignment, int padding) {
        this(drawable, verticalAlignment, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), padding);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return imageWidth + padding;
    }

    @Override
    public void drawLeadingMargin(@NonNull Canvas c, @NonNull Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  @NonNull CharSequence text, int start, int end,
                                  boolean first, @NonNull Layout layout) {

        int st = ((Spanned) text).getSpanStart(this);
        int dLeft = x;
        int line = layout.getLineForOffset(st);
        if (layout.getAlignment() == Layout.Alignment.ALIGN_CENTER)
            dLeft = layout.getWidth() / 2 - (int) (layout.getLineWidth(line) / 2);
        int dTop = layout.getLineTop(line);

        int dw = imageWidth;
        int dh = imageHeight;

        switch (verticalAlignment) {
            case ALIGN_DEFAULT: break;
            case ALIGN_TOP: dTop = 0; break;
            case ALIGN_CENTER: dTop += ((bottom - top) / 2) - (dh / 2); break;
            case ALIGN_BASELINE: dTop += baseline - dh; break;
            case ALIGN_BOTTOM: dTop += bottom - dh; break;
        }
        mDrawable.setBounds(dLeft, dTop, dLeft + dw, dTop + dh);
        mDrawable.draw(c);
    }
    @Override
    public void chooseHeight(@NonNull CharSequence text, int start, int end, int istartv, int v, @NonNull Paint.FontMetricsInt fm) {
        if (end == ((Spanned) text).getSpanEnd(this)) {
            int ht = imageHeight;

            int need = ht - (v + fm.descent - fm.ascent - istartv);
            if (need > 0) fm.descent += need;

            need = ht - (v + fm.bottom - fm.top - istartv);
            if (need > 0) fm.bottom += need;
        }
    }


}