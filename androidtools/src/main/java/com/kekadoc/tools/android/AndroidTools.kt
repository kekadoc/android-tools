package com.kekadoc.tools.android

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.fragment.app.Fragment

fun Context.dpToPx(dp: Float) = AndroidUtils.dpToPx(this, dp)
fun Context.spToPx(dp: Float) = AndroidUtils.spToPx(this,dp)
fun Context.color(@ColorRes res: Int) = AndroidUtils.getColor(this, res)
fun Context.drawable(@DrawableRes res: Int) = AndroidUtils.getDrawable(this, res)
fun Context.dimen(@DimenRes res: Int) = AndroidUtils.getDimension(this, res)
fun Context.value(@DimenRes res: Int) = AndroidUtils.getValue(this, res)
fun Context.text(res: Int) = AndroidUtils.getText(this, res)
fun Context.string(@StringRes res: Int) = AndroidUtils.getString(this, res)
fun Context.themeColor(@StyleableRes @AttrRes attrColor: Int) = AndroidUtils.getThemeColor(this, attrColor)
fun Context.themeDimen(@AttrRes attrDimen: Int) = AndroidUtils.getThemeDimension(this, attrDimen)

/** String  */
fun Fragment.string(@StringRes res: Int): String? {
    return if (res == 0) null else requireContext().getString(res)
}
/** Drawable  */
fun Fragment.drawable(@DrawableRes res: Int): Drawable? {
    return if (res == 0) null else AndroidUtils.getDrawable(requireContext(), res)
}
/** Dimen  */
fun Fragment.dimen(@DimenRes res: Int): Float {
    return if (res == 0) 0f else requireContext().resources.getDimension(res)
}
/** Color  */
fun Fragment.color(@ColorRes res: Int): Int {
    return if (res == 0) Color.TRANSPARENT else AndroidUtils.getColor(requireContext(), res)
}
/** Strings  */
fun Fragment.stringArray(@ArrayRes res: Int): Array<String?>? {
    return if (res == 0) null else requireContext().resources.getStringArray(res)
}