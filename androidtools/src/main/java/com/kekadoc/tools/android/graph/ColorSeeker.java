package com.kekadoc.tools.android.graph;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

public interface ColorSeeker {

    interface Res {

        @ColorRes
        int getColorResId();

    }

    @ColorInt int getColor();

}
