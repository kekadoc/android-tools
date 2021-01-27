package com.qeapp.tools.android.sparcearray

import android.util.SparseArray
import androidx.core.util.isEmpty
import com.qeapp.tools.random.QeRandom
import java.util.*

object QeSparseArray {

    fun <T> sparseToList(sparseArray: SparseArray<T>, list: MutableList<T>): List<T> {
        for (i in 0 until sparseArray.size()) list.add(i, sparseArray.valueAt(i))
        return list
    }
    fun <T> sparseToList(sparseArray: SparseArray<T>): List<T> {
        return sparseToList(sparseArray, ArrayList())
    }

    fun <E> isEmpty(sparseArray: SparseArray<E>?): Boolean {
        return sparseArray == null || sparseArray.size() == 0
    }

    fun <E> getRandomFrom(array: SparseArray<E>?): E? {
        if (array == null) return null
        if (array.isEmpty()) return null
        val r = QeRandom.newRandom().nextInt(array.size())
        return array.get(array.keyAt(r))
    }

    fun <T, E> build(array: SparseArray<E>, builder: (item: E) -> T): List<T> {
        return build(ArrayList(), array, builder)
    }
    fun <T, E> build(list: MutableList<T>, array: SparseArray<E>, builder: (item: E) -> T): List<T> {
        for (i in 0 until array.size()) {
            val e: E = array.get(array.keyAt(i))
            val t: T = builder.invoke(e)
            if (t != null) list.add(t)
        }
        return list
    }

}