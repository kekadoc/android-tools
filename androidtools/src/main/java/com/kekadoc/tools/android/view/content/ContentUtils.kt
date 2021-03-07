package com.kekadoc.tools.android.view.content

import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.kekadoc.tools.android.view.ViewUtils.setProgress
import com.kekadoc.tools.ui.content.Content

open class ContentView <V : View?> (var view: V, shown: Boolean = true) : Content.SimpleInstance(shown) {

    override fun onHide() {
        view?.isVisible = false
    }
    override fun onShown() {
        view?.isVisible = true
    }

    override fun toString(): String {
        return super.toString() + "view: " + view
    }

    class Observable <V : View?>(view: V, shown: Boolean = true, var observer: Content.Observer? = null) : ContentView<V>(view, shown) {
        override fun onHide() {
            super.onHide()
            observer?.onHide()
        }
        override fun onShown() {
            super.onShown()
            observer?.onShow()
        }
    }

}

open class ContentProgressBar <V : ProgressBar?> (var view: V, shown: Boolean = true) : Content.Progress.SimpleInstance(shown) {

    override fun onProgress(old: Double, progress: Double) {
        view?.setProgress(progress.toFloat())
    }

    override fun onHide() {
        view?.isVisible = false
    }
    override fun onShown() {
        view?.isVisible = true
    }

    class Observable <V : ProgressBar>(view: V,
                                       shown: Boolean = true,
                                       var observer: Content.Observer? = null)
        : ContentProgressBar<V>(view, shown) {
        override fun onHide() {
            super.onHide()
            observer?.onHide()
        }
        override fun onShown() {
            super.onShown()
            observer?.onShow()
        }
    }

}

open class ContentProgressIndicator <Indicator : BaseProgressIndicator<*>?> (
    indicator: Indicator, shown: Boolean = true)
    : ContentProgressBar<Indicator>(indicator, shown) {

    override fun onProgress(old: Double, progress: Double) {
        super.onProgress(old, progress)
        view?.isIndeterminate = progress <= 0.0
    }
    override fun onHide() {
        view?.hide()
    }
    override fun onShown() {
        view?.show()
    }

}