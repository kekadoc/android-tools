package com.kekadoc.tools.android.view

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

interface ViewsCollector {

    companion object {
        @JvmStatic fun asView(view: View): ViewsCollector {
            return object : ViewsCollector {
                override fun <T : View> findViewById(id: Int): T? {
                    return view.findViewById(id)
                }
            }
        }
        @JvmStatic fun asActivity(activity: Activity): ViewsCollector {
            return object : ViewsCollector {
                override fun <T : View> findViewById(id: Int): T? {
                    return activity.findViewById(id)
                }
            }
        }
    }

    fun <T : View> findViewById(@IdRes id: Int): T?

}