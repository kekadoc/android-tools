package com.kekadoc.tools.android.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.kekadoc.tools.observer.Observing

fun Lifecycle.addObserving(observer: LifecycleObserver): Observing {
    addObserver(observer)
    return object : Observing {
        override fun remove() {
            removeObserver(observer)
        }
    }
}

fun LifecycleOwner.onLifecycle(observing: Observing): Observing {
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) observing.remove()
        }
    })
    return observing
}

fun Lifecycle.Event.isOnStart(): Boolean {
    return this == Lifecycle.Event.ON_START
}
fun Lifecycle.Event.isOnCreate(): Boolean {
    return this == Lifecycle.Event.ON_CREATE
}
fun Lifecycle.Event.isOnResume(): Boolean {
    return this == Lifecycle.Event.ON_RESUME
}
fun Lifecycle.Event.isOnPause(): Boolean {
    return this == Lifecycle.Event.ON_PAUSE
}
fun Lifecycle.Event.isOnStop(): Boolean {
    return this == Lifecycle.Event.ON_STOP
}
fun Lifecycle.Event.isOnDestroy(): Boolean {
    return this == Lifecycle.Event.ON_DESTROY
}
fun Lifecycle.Event.isOnAny(): Boolean {
    return this == Lifecycle.Event.ON_ANY
}

fun Lifecycle.State.isCreated(): Boolean {
    return this == Lifecycle.State.CREATED
}
fun Lifecycle.State.isDestroyed(): Boolean {
    return this == Lifecycle.State.DESTROYED
}
fun Lifecycle.State.isInitialized(): Boolean {
    return this == Lifecycle.State.INITIALIZED
}
fun Lifecycle.State.isResumed(): Boolean {
    return this == Lifecycle.State.RESUMED
}
fun Lifecycle.State.isStarted(): Boolean {
    return this == Lifecycle.State.STARTED
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