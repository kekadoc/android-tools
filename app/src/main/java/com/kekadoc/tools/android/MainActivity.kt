package com.kekadoc.tools.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.kekadoc.tools.android.log.log
import com.kekadoc.tools.android.view.ViewUtils.findAllViews
import com.kekadoc.tools.android.view.ViewById
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MainActivity-TAG"
    }


    @ViewById(id = R.id.textView)
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        measureTimeMillis {
            findAllViews()
        }.log {
            it.toString()
        }.e(TAG, "Time: ")

        textView.log().e()

        Log.e(TAG, "onCreate: $textView")

        val dialog = AlertDialog.Builder(this)
                .setTitle("Dialog")
                .setIcon(R.drawable.none)
                .setMessage("Dialog")
                .setCancelable(false)
                .setPositiveButton("Positive") { _, _ -> Log.e(TAG, "onPositive: ") }
                .setNeutralButton("Neutral") { _, _ -> Log.e(TAG, "onNeutral: ", ) }
                .setNegativeButton("Negative") { _, _ -> Log.e(TAG, "onNegative: ", ) }
                .setOnCancelListener {
                    Log.e(TAG, "onCancel")
                }
                .setOnDismissListener {
                    Log.e(TAG, "onDismiss")
                }
                .setOnKeyListener { _, keyCode, event ->
                    Log.e(TAG, "onKey: $keyCode $event")
                    false
                }.setCancelable(true).create()

        dialog.setOnCancelListener {
            Log.e(TAG, "onDialogCancel: ")
        }
        dialog.setOnDismissListener {
            Log.e(TAG, "onDialogDismiss: ")
        }
        dialog.setOnShowListener {
            Log.e(TAG, "onDialogCancel: ")
        }
        dialog.setOnKeyListener { _, keyCode, event ->
            Log.e(TAG, "onDialogKey: $keyCode $event")
            false
        }
        dialog.setCancelable(true)
        dialog.show()

    }

}