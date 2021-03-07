package com.kekadoc.tools.android.databinding

import com.kekadoc.tools.android.AndroidUtils.getDrawable
import androidx.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.RatingBar
import androidx.databinding.InverseBindingAdapter
import com.google.android.material.textfield.TextInputEditText
import android.view.View
import android.widget.ImageView

object AndroidBinding {

    @BindingAdapter("android:background")
    fun setBackgroundColor(view: View, color: Int) {
        view.setBackgroundColor(color)
    }


    @BindingAdapter("tools:src")
    fun setImage(view: ImageView, drawable: Drawable?) {
        view.setImageDrawable(drawable)
    }
    @BindingAdapter("app:srcCompat")
    fun setImage(view: ImageView, res: Int) {
        view.setImageDrawable(getDrawable(view.context, res))
    }
    @BindingAdapter("app:srcCompat")
    fun setImageCompat(view: ImageView, drawable: Drawable?) {
        view.setImageDrawable(drawable)
    }


    @BindingAdapter("app:layout_height")
    fun setLayoutHeight(view: View, height: Float) {
        val layoutParams = view.layoutParams
        layoutParams.height = height.toInt()
        view.layoutParams = layoutParams
    }
    @BindingAdapter("app:layout_width")
    fun setLayoutWidth(view: View, width: Float) {
        val layoutParams = view.layoutParams
        layoutParams.width = width.toInt()
        view.layoutParams = layoutParams
    }


    @BindingAdapter("android:rating")
    fun setRatingView(view: RatingBar, rank: Int) {
        view.rating = rank.toFloat()
    }
    @InverseBindingAdapter(attribute = "android:rating")
    fun getRatingFromView(view: RatingBar): Int {
        return view.rating.toInt()
    }


    @BindingAdapter("android:text")
    fun setText(view: TextInputEditText, text: CharSequence?) {
        view.setText(text)
    }
    @InverseBindingAdapter(attribute = "android:text")
    fun getText(view: TextInputEditText): String? {
        val editable = view.text ?: return null
        return editable.toString()
    }

}