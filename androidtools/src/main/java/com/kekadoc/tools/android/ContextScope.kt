package com.kekadoc.tools.android

import android.content.Context
import androidx.annotation.*

fun ContextScope.dpToPx(dp: Float) = getContext().dpToPx(dp)
fun ContextScope.spToPx(dp: Float) = getContext().spToPx(dp)
fun ContextScope.color(@ColorRes res: Int) = getContext().color(res)
fun ContextScope.drawable(@ColorRes res: Int) = getContext().drawable(res)
fun ContextScope.dimen(@DimenRes res: Int) = getContext().dimen(res)
fun ContextScope.value(@DimenRes res: Int) = AndroidUtils.getValue(getContext(), res)
fun ContextScope.text(res: Int) = AndroidUtils.getText(getContext(), res)
fun ContextScope.string(@StringRes res: Int) = AndroidUtils.getString(getContext(), res)
fun ContextScope.themeColor(@StyleableRes @AttrRes attrColor: Int) = AndroidUtils.getThemeColor(getContext(), attrColor)
fun ContextScope.themeDimen(@AttrRes attrDimen: Int) = AndroidUtils.getThemeDimension(getContext(), attrDimen)

interface ContextScope {
    fun getContext(): Context
}