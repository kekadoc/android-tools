package com.qeapp.tools.android.lifecycle

import androidx.lifecycle.Lifecycle

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