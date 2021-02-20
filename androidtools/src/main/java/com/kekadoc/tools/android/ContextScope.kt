package com.kekadoc.tools.android

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.annotation.*
import androidx.fragment.app.Fragment

fun View.asContextScope(): ContextScope {
    return object : ContextScope {
        override fun getContext(): Context {
            return context
        }
    }
}
fun Activity.asContextScope(): ContextScope {
    return object : ContextScope {
        override fun getContext(): Context {
            return this@asContextScope
        }
    }
}
fun Fragment.asContextScope(): ContextScope {
    return object : ContextScope {
        override fun getContext(): Context {
            return requireContext()
        }
    }
}
fun Dialog.asContextScope(): ContextScope {
    return object : ContextScope {
        override fun getContext(): Context {
            return this@asContextScope.context
        }
    }
}

fun ContextScope.dpToPx(dp: Float) = getContext().dpToPx(dp)
fun ContextScope.spToPx(dp: Float) = getContext().spToPx(dp)
fun ContextScope.color(@ColorRes res: Int) = getContext().color(res)
fun ContextScope.drawable(@ColorRes res: Int) = getContext().drawable(res)
fun ContextScope.dimen(@DimenRes res: Int) = getContext().dimen(res)
fun ContextScope.value(@DimenRes res: Int) = AndroidUtils.getValue(getContext(), res)
fun ContextScope.text(res: Int) = AndroidUtils.getText(getContext(), res)
fun ContextScope.string(@StringRes res: Int) = AndroidUtils.getString(getContext(), res)
fun ContextScope.themeColor(@StyleableRes @AttrRes attrColor: Int) = AndroidUtils.getThemeColor(getContext(), attrColor)
fun ContextScope.themeColor(themeColor: ThemeColor) = AndroidUtils.getThemeColor(getContext(), themeColor)
fun ContextScope.themeDimen(@AttrRes attrDimen: Int) = AndroidUtils.getThemeDimension(getContext(), attrDimen)

interface ContextScope {
    fun getContext(): Context
}