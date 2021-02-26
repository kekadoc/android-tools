package com.kekadoc.tools.android.fragment

import androidx.annotation.*
import androidx.fragment.app.Fragment
import com.kekadoc.tools.android.AndroidUtils
import com.kekadoc.tools.android.ThemeColor

fun Fragment.stringArray(@ArrayRes res: Int): Array<String>? = AndroidUtils.getStringArray(requireContext(), res)
fun Fragment.dpToPx(dp: Float) = AndroidUtils.dpToPx(requireContext(), dp)
fun Fragment.spToPx(dp: Float) = AndroidUtils.spToPx(requireContext(),dp)
fun Fragment.color(@ColorRes res: Int) = AndroidUtils.getColor(requireContext(), res)
fun Fragment.drawable(@DrawableRes res: Int) = AndroidUtils.getDrawable(requireContext(), res)
fun Fragment.dimen(@DimenRes res: Int) = AndroidUtils.getDimension(requireContext(), res)
fun Fragment.value(@DimenRes res: Int) = AndroidUtils.getValue(requireContext(), res)
fun Fragment.text(res: Int) = AndroidUtils.getText(requireContext(), res)
fun Fragment.string(@StringRes res: Int) = AndroidUtils.getString(requireContext(), res)
fun Fragment.themeColor(@StyleableRes @AttrRes attrColor: Int) = AndroidUtils.getThemeColor(requireContext(), attrColor)
fun Fragment.themeColor(themeColor: ThemeColor) = AndroidUtils.getThemeColor(requireContext(), themeColor)
fun Fragment.themeDimen(@AttrRes attrDimen: Int) = AndroidUtils.getThemeDimension(requireContext(), attrDimen)