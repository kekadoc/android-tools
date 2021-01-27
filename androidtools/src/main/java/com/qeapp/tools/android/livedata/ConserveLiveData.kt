package com.qeapp.tools.android.livedata

import androidx.lifecycle.MutableLiveData

class ConserveLiveData<T> : MutableLiveData<T>() {

    var oldData: T? = null
       private set

    override fun setValue(value: T) {
        oldData = getValue()
        super.setValue(value)
    }

}