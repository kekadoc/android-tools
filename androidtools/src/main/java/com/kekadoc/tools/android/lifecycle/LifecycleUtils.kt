package com.kekadoc.tools.android.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.kekadoc.tools.observer.Observing

fun LifecycleOwner.onLifecycle(observing: Observing): Observing {
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) observing.remove()
        }
    })
    return observing
}

object LifecycleUtils {

    @JvmStatic
    fun Lifecycle.isCurrentState(event: Lifecycle.State): Boolean {
        return currentState == event
    }
    @JvmStatic
    fun Lifecycle.isAtLeast(event: Lifecycle.State): Boolean {
        return currentState.isAtLeast(event)
    }

}