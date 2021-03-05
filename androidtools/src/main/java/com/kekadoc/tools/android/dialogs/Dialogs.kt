package com.kekadoc.tools.android.dialogs

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import com.kekadoc.tools.observable.ObservationManager
import com.kekadoc.tools.observable.Observing
import java.util.*

fun Dialog.observe(observer: DialogManagement.Observer): Observing {
    setOnShowListener {
        observer.onDialogShow(this)
    }
    setOnCancelListener {
        observer.onDialogCancel(this)
    }
    setOnDismissListener {
        observer.onDialogDismiss(this)
    }
    return object : Observing {
        override fun remove() {
            this@observe.setOnShowListener(null)
            this@observe.setOnCancelListener(null)
            this@observe.setOnDismissListener(null)
        }
    }
}

interface DialogManagement {

    interface Observable {

        fun addDialogObserver(observer: Observer): Observing

        class Management : ObservationManager<Observer>(), Observer {
            override fun onDialogShow(dialog: Dialog) {
                getIterationObservers().forEach {
                    it.onDialogShow(dialog)
                }
            }
            override fun onDialogDismiss(dialog: Dialog) {
                getIterationObservers().forEach {
                    it.onDialogDismiss(dialog)
                }
            }
            override fun onDialogCancel(dialog: Dialog) {
                getIterationObservers().forEach {
                    it.onDialogCancel(dialog)
                }
            }
        }

    }
    interface Observer {

        /**
         * onDialogShow
         * */
        fun onDialogShow(dialog: Dialog)
        /**
         * onDialogDismiss
         * */
        fun onDialogDismiss(dialog: Dialog)
        /**
         * onDialogCancel
         * */
        fun onDialogCancel(dialog: Dialog)

    }

    /**
     * Show this dialog
     */
    fun showDialog(dialog: Dialog)

}
interface DialogFragmentManagement {

    interface Observable {
        fun addDialogFragmentObserver(observer: Observer): Observing

        class Management : ObservationManager<Observer>(), Observer {
            override fun onDialogFragmentShow(dialog: DialogFragment) {
                getIterationObservers().forEach {
                    it.onDialogFragmentShow(dialog)
                }
            }
            override fun onDialogFragmentCancel(dialog: DialogFragment) {
                getIterationObservers().forEach {
                    it.onDialogFragmentCancel(dialog)
                }
            }
            override fun onDialogFragmentDismiss(dialog: DialogFragment) {
                getIterationObservers().forEach {
                    it.onDialogFragmentDismiss(dialog)
                }
            }

        }

    }
    interface Observer {
        /**
         * On dialog show
         */
        fun onDialogFragmentShow(dialog: DialogFragment)
        /**
         * On dialog cancel
         */
        fun onDialogFragmentCancel(dialog: DialogFragment)
        /**
         * On dialog dismiss
         */
        fun onDialogFragmentDismiss(dialog: DialogFragment)
    }

    /**
     * Show Dialog Fragment
     */
    fun showDialog(dialog: DialogFragment)

}