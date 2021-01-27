package com.qeapp.tools.android.view.size;

public final class ValueNotSizeException extends IndexOutOfBoundsException {

    public static void requireSize(@SizeValue int size) {
        if (size != Sizing.SIZE_EXTRA_SMALL && size != Sizing.SIZE_SMALL && size != Sizing.SIZE_MEDIUM
                && size != Sizing.SIZE_LARGE && size != Sizing.SIZE_EXTRA_LARGE) throw new ValueNotSizeException(size);
    }

    public ValueNotSizeException(int failSize) {
        this(failSize, null);
    }
    public ValueNotSizeException(int failSize, String s) {
        super("Size Error " + failSize + " - not @Size value. " + s);
    }

}
