package com.qeapp.tools.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.qeapp.tools.android.R
import com.qeapp.tools.android.view.ViewById

class MainActivity : AppCompatActivity() {

    @ViewById(id = R.id.textView_hello)
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}