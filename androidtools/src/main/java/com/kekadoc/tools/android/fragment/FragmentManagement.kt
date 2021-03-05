package com.kekadoc.tools.android.fragment

import androidx.fragment.app.Fragment
import com.kekadoc.tools.observable.ObservationManager
import com.kekadoc.tools.observable.Observing

/**
 * FragmentManagement
 * */
interface FragmentManagement {

    interface Observable {

        fun addFragmentObserver(observer: Observer): Observing

        class Management : ObservationManager<Observer>(), Observer {
            override fun onFragmentAttached(fragment: Fragment) {
                getIterationObservers().forEach {
                    it.onFragmentAttached(fragment)
                }
            }
            override fun onFragmentDetached(fragment: Fragment) {
                getIterationObservers().forEach {
                    it.onFragmentDetached(fragment)
                }
            }
        }

    }

    interface Observer {
        /**
         *
         */
        fun onFragmentAttached(fragment: Fragment) {}

        /**
         *
         */
        fun onFragmentDetached(fragment: Fragment) {}
    }

    fun switchFragment(fragment: Fragment)

    fun previousFragment()
    fun hasBackFragment(): Boolean

}