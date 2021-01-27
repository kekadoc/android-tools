package com.qeapp.tools.android.sparcearray;

import com.qeapp.tools.storage.Iteration;

public interface SparseArrayInterface<E> {

    E get(int key);

    E get(int key, E valueIfKeyNotFound);

    void remove(int key);

    void removeAt(int index);

    void removeAtRange(int index, int size);

    void put(int key, E value);

    int size();

    int keyAt(int index);

    E valueAt(int index);

    void setValueAt(int index, E value);

    int indexOfKey(int key);

    int indexOfValue(E value);

    void clear();

    void append(int key, E value);

    default void foreach(Iteration.Single<E> iteration) {
        for (int i = 0; i < size(); i++) iteration.iteration(get(keyAt(i)));
    }

    default boolean containKey(int key) {
        return get(key, null) != null;
    }

}
