package com.kekadoc.tools.android.view

import android.view.View
import com.kekadoc.tools.data.state.DataStatesCollector
import com.kekadoc.tools.data.state.StateKeeper
import com.kekadoc.tools.observer.ObserverManagement
import com.kekadoc.tools.observer.Observing

open class ViewFocusability<V : View> : DataStatesCollector<V, ViewFocusability.State, ViewFocusability<V>.StateView>() {

    enum class State {
        FOCUS,
        NORMAL,
        HIDED
    }

    private enum class GeneralState {
        FOCUSED,
        UNFOCUSED
    }

    interface Observer<V> {

        interface Simple<V> : Observer<V> {

            override fun onStateViewChange(view: V, oldState: State, newState: State) {
                when (newState) {
                    State.FOCUS -> onFocus(view)
                    State.HIDED -> onHide(view)
                    State.NORMAL -> onNormal(view)
                }
            }

            fun onFocus(view: V)
            fun onNormal(view: V)
            fun onHide(view: V)

        }

        fun onStateViewChange(view: V, oldState: State, newState: State)

        fun onFocused()
        fun onUnfocused()

    }

    private inner class Observers : ObserverManagement<Observer<V>>(), Observer<V> {

        private var lastStage = GeneralState.UNFOCUSED

        override fun onStateViewChange(view: V, oldState: State, newState: State) {
            getIterationObservers().forEach { it.onStateViewChange(view, oldState, newState) }
        }

        override fun onFocused() {
            lastStage = GeneralState.FOCUSED
            getIterationObservers().forEach { it.onFocused() }
        }
        override fun onUnfocused() {
            lastStage = GeneralState.UNFOCUSED
            getIterationObservers().forEach { it.onUnfocused() }
        }

        override fun addObserver(observer: Observer<V>): Observing {
            val obs = super.addObserver(observer)
            if (lastStage == GeneralState.UNFOCUSED) observer.onUnfocused()
            else observer.onFocused()
            return obs
        }
    }

    var maxSelected = 1

    protected val focusedViews = hashSetOf<StateView>()
    private val observers = Observers()

    fun addObserver(observer: Observer<V>): Observing = observers.addObserver(observer)

    fun isFocused(): Boolean = focusedViews.size > 0
    fun getCountFocused() = focusedViews.size

    fun focus(view: V): Boolean {
        val currentState: StateView = getStateKeeper(view) ?: return false
        currentState.focus()
        return true
    }
    fun reset(view: V): Boolean {
        val currentState: StateView = getStateKeeper(view) ?: return false
        currentState.reset()
        return true
    }
    fun hide(view: V): Boolean {
        val currentState: StateView = getStateKeeper(view) ?: return false
        currentState.hide()
        return true
    }

    private fun addFocus(stateView: StateView) {
        val focused = isFocused()
        if (this.focusedViews.add(stateView))
            if (!focused && isFocused()) {
                onFocused()
                observers.onFocused()
            }
    }
    private fun removeFocus(stateView: StateView) {
        val focused = isFocused()
        if (this.focusedViews.remove(stateView)) {
            if (focused && !isFocused()) {
                onUnfocused()
                observers.onUnfocused()
            }
        }

    }

    protected open fun onFocused() {}
    protected open fun onUnfocused() {}

    fun add(view: V) = add(view, State.NORMAL)

    override fun onDataStateChange(keeper: StateView, oldState: State, newState: State) {
        observers.onStateViewChange(keeper.data, oldState, newState)
        if (newState == State.FOCUS) addFocus(keeper)
        else if (oldState == State.FOCUS) removeFocus(keeper)
    }

    override fun onCreateStateKeeper(data: V, state: State): StateView {
        return StateView(data)
    }

    inner class StateView(data: V) : StateKeeper.Default<V, State>(data, State.NORMAL) {

        fun isHided(): Boolean = state == State.HIDED
        fun isFocused(): Boolean = state == State.FOCUS
        fun isNormal(): Boolean = state == State.NORMAL

        fun hide() {
            state = State.HIDED
        }
        fun focus() {
            state = State.FOCUS
        }
        fun reset() {
            state = State.NORMAL
        }

    }

    open class Auto<V : View> : ViewFocusability<V>() {

        companion object {
            fun <V : View> onClick(): Auto<V> {
                return object : Auto<V>() {
                    override fun onAdded(keeper: StateView) {
                        keeper.data.setOnClickListener {
                            invokeAutoFocus(keeper.data)
                        }
                    }
                    override fun onRemoved(keeper: StateView) {
                        keeper.data.setOnClickListener(null)
                    }
                }
            }
        }

        private fun hideAllNotFocused() {
            getAll().forEach {
                val stateView: StateView = it as StateView
                if (stateView.isNormal()) stateView.hide()
            }
        }
        private fun resetAll() {
            getAll().forEach {
                val stateView: StateView = it as StateView
                if (!stateView.isNormal()) stateView.reset()
            }
        }

        protected fun invokeAutoFocus(view: V) {
            val stateView = getStateKeeper(view)!!
            if (stateView.isFocused()) stateView.reset()
            else {
                if (getCountFocused() < maxSelected) stateView.focus()
                else if (isFocused() && maxSelected == 1) {
                    val focused = focusedViews.first()
                    stateView.focus()
                    focused.reset()
                }
            }
        }

        override fun onFocused() {
            hideAllNotFocused()
        }
        override fun onUnfocused() {
            resetAll()
        }

        override fun onDataStateChange(keeper: StateView, oldState: State, newState: State) {
            super.onDataStateChange(keeper, oldState, newState)
            if (isFocused()) hideAllNotFocused()
            else resetAll()
        }
    }

}

fun <V : View> ViewFocusability<V>.addObserver(onFocus: ((view: V) -> Unit)? = null,
                                               onNormal: ((view: V) -> Unit)? = null,
                                               onHide: ((view: V) -> Unit)? = null,
                                               onFocused: (() -> Unit)? = null,
                                               onUnfocused: (() -> Unit)? = null): Observing {
    return addObserver(object : ViewFocusability.Observer.Simple<V> {

        override fun onFocus(view: V) {
            onFocus?.invoke(view)
        }
        override fun onNormal(view: V) {
            onNormal?.invoke(view)
        }
        override fun onHide(view: V) {
            onHide?.invoke(view)
        }

        override fun onFocused() {
            onFocused?.invoke()
        }
        override fun onUnfocused() {
            onUnfocused?.invoke()
        }
    })
}

fun <V : View> ViewFocusability<V>.onFocus(onFocus: ((view: V) -> Unit)): Observing {
    return addObserver(onFocus = onFocus)
}
fun <V : View> ViewFocusability<V>.onNormal(onNormal: ((view: V) -> Unit)): Observing {
    return addObserver(onNormal = onNormal)
}
fun <V : View> ViewFocusability<V>.onHide(onHide: ((view: V) -> Unit)): Observing {
    return addObserver(onHide = onHide)
}
fun <V : View> ViewFocusability<V>.onFocused(onFocused: (() -> Unit)): Observing {
    return addObserver(onFocused = onFocused)
}
fun <V : View> ViewFocusability<V>.onUnfocused(onUnfocused: (() -> Unit)): Observing {
    return addObserver(onUnfocused = onUnfocused)
}


