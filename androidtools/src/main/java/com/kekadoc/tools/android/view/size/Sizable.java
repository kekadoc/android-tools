package com.kekadoc.tools.android.view.size;

/**
 * Интерфейс описывающий объкт который функционирует с {@link SizeValue}
 */
public interface Sizable {

    @SizeValue
    int getSize();
    void setSize(@SizeValue int size);

    void applySize(@SizeValue int size);

}
