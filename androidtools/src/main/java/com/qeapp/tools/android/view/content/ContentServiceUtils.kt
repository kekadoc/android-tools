package com.qeapp.tools.android.view.content

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.qeapp.tools.android.R

class DefaultFragmentLoadingContent : Fragment(R.layout.fragment_content_service_loading_default), Content.Loading {

    private var progressBar: ProgressBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.progressBar = view.findViewById(R.id.progressBar)
    }

    override fun complete() {

    }
    override fun update(fraction: Float, updateData: String?) {

    }

}
class DefaultFragmentErrorContent : Fragment(R.layout.fragment_content_service_error_default) {

    companion object {
        fun newInstance(image: Drawable?, text: CharSequence?): DefaultFragmentErrorContent {
            val fragment = DefaultFragmentErrorContent()
            fragment.resImage = image
            fragment.resMessage = text
            return fragment
        }
    }

    private var resImage: Drawable? = null
        set(value) {
            field = value
            image?.setImageDrawable(value)
        }
    private var resMessage: CharSequence? = null
        set(value) {
            field = value
            text?.text = value
        }

    private var image: ImageView? = null
    private var text: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.image = view.findViewById(R.id.imageView_error)
        this.text = view.findViewById(R.id.textView_errorMessage)

        this.image!!.setImageDrawable(resImage)
        this.text!!.text = resMessage
    }

}