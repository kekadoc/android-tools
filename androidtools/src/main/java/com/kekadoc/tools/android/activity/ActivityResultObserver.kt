package com.kekadoc.tools.android.activity

import android.content.Intent

interface ActivityResultObserver {
    fun onResult(requestCode: Int, resultCode: Int, data: Intent?)
}