package com.qeapp.tools.android.snack

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

interface SnackManagement {

    fun showSnack(text: CharSequence?, duration: Int = Snackbar.LENGTH_SHORT): Snackbar
    fun showSnack(@StringRes text: Int, duration: Int = Snackbar.LENGTH_LONG): Snackbar

}