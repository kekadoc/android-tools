package com.kekadoc.tools.android.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle

fun <T> T.startActivityForResult(intent: Intent,
                                 _requestCode: Int,
                                 bundle: Bundle? = null,
                                 callback: ActivityResultObserver) where T: Activity,
                                                                         T: ActivityResultObservable
{
    addActivityResultObserver(callback)
    startActivityForResult(intent, _requestCode, bundle)
}

class ActivityResultTool (private val activity: Activity, val activityResultObservable: ActivityResultObservable) {

    companion object {

        fun <T> create(activity: T): ActivityResultTool where T : Activity, T : ActivityResultObservable {
            return ActivityResultTool(activity, activity)
        }
    }

    fun startActivityForResult(intent: Intent, _requestCode: Int, bundle: Bundle? = null, callback: (resultCode: Int, data: Intent?) -> Unit) {
        val activityResultObserver = object : ActivityResultObserver {
            override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {
                if (_requestCode == requestCode) callback.invoke(resultCode, data)
                activityResultObservable.removeActivityResultObserver(this)
            }
        }
        activityResultObservable.addActivityResultObserver(activityResultObserver)
        activity.startActivityForResult(intent, _requestCode, bundle)
    }

}
