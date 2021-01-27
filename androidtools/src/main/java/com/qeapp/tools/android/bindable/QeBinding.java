package com.qeapp.tools.android.bindable;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

import com.google.android.material.textfield.TextInputEditText;
import com.qeapp.tools.android.QeAndroid;


public class QeBinding {

    @BindingAdapter({"android:background"})
    public static void setBackgroundColor(View view, int color) {
        view.setBackgroundColor(color);
    }

    @BindingAdapter({"tools:src"})
    public static void setImage(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter({"app:srcCompat"})
    public static void setImage(ImageView view, int res) {
        view.setImageDrawable(QeAndroid.getDrawable(view.getContext(), res));
    }

    @BindingAdapter({"app:srcCompat"})
    public static void setImageCompat(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("app:layout_height")
    public static void setLayoutHeight(View view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }
    @BindingAdapter("app:layout_width")
    public static void setLayoutWidth(View view, float width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) width;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:rating")
    public static void setRatingView(RatingBar view, int rank) {
        view.setRating(rank);
    }
    @InverseBindingAdapter(attribute = "android:rating")
    public static int getRatingFromView(RatingBar view) {
        return (int) view.getRating();
    }

    @BindingAdapter("android:text")
    public static void setText(@NonNull TextInputEditText view, CharSequence text) {
        view.setText(text);
    }
    @InverseBindingAdapter(attribute = "android:text")
    public static String getText(TextInputEditText view) {
        Editable editable = view.getText();
        if (editable == null) return null;
        return editable.toString();
    }

}
