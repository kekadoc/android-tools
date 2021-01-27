package com.qeapp.tools.android.activity

import android.content.Intent

interface OnActivityResultObserver {
    fun onResult(requestCode: Int, resultCode: Int, data: Intent?)
}