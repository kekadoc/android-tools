package com.kekadoc.tools.android.view.size;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Sizing.SIZE_EXTRA_SMALL, Sizing.SIZE_SMALL, Sizing.SIZE_MEDIUM, Sizing.SIZE_LARGE, Sizing.SIZE_EXTRA_LARGE})
@Retention(RetentionPolicy.SOURCE)
public @interface SizeValue {}
