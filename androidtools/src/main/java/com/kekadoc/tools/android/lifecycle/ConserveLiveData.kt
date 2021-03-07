package com.kekadoc.tools.android.lifecycle

import androidx.lifecycle.MutableLiveData

open class ConserveLiveData<T> : MutableLiveData<T>() {

    var oldData: T? = null
       private set

    override fun setValue(value: T) {
        oldData = getValue()
        super.setValue(value)
    }

}