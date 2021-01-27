package com.qeapp.tools.android.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class ActivityResultTool (val activity: Activity, val activityResultObservable: ActivityResultObservable) {

    companion object {
        fun <T> create(activity: T): ActivityResultTool where T : Activity, T : ActivityResultObservable {
            return ActivityResultTool(activity, activity)
        }
    }

    fun startActivityForResult(intent: Intent, _requestCode: Int, bundle: Bundle? = null, callback: (resultCode: Int, data: Intent?) -> Unit) {
        val activityResultObserver = object : OnActivityResultObserver {
            override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {
                if (_requestCode == requestCode) callback.invoke(resultCode, data)
                activityResultObservable.removeActivityResultObserver(this)
            }
        }
        activityResultObservable.addActivityResultObserver(activityResultObserver)
        activity.startActivityForResult(intent, _requestCode, bundle)
    }

}