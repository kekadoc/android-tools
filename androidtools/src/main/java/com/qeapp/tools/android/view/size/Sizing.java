package com.qeapp.tools.android.view.size;

import android.content.Context;

import androidx.annotation.DimenRes;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

import com.qeapp.tools.android.QeAndroid;
import com.qeapp.tools.exeption.Wtf;

public class Sizing {

    public static @DimenRes int getDimensionRes(@SizeValue int size, @androidx.annotation.Size(value = 5) @DimenRes int... dimens) {
        ValueNotSizeException.requireSize(size);
        switch (size) {
            case SIZE_EXTRA_SMALL: return dimens[0];
            case SIZE_SMALL: return dimens[1];
            case SIZE_MEDIUM: return dimens[2];
            case SIZE_LARGE: return dimens[3];
            case SIZE_EXTRA_LARGE: return dimens[4];
            default: throw new Wtf();
        }
    }
    public static @Dimension(unit = Dimension.PX) float getDimension(@NonNull Context context, @SizeValue int size, @androidx.annotation.Size(value = 5) @DimenRes int... dimens) {
        return QeAndroid.getDimension(context, getDimensionRes(size, dimens));
    }

    public static final int SIZE_EXTRA_SMALL = -2;
    public static final int SIZE_SMALL = -1;
    public static final int SIZE_MEDIUM = 0;
    public static final int SIZE_LARGE = 1;
    public static final int SIZE_EXTRA_LARGE = 2;

}
