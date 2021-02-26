package com.kekadoc.tools.android.view

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.*
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.kekadoc.tools.fraction.Fraction
import com.kekadoc.tools.reflection.ReflectionUtils
import com.kekadoc.tools.shape.square.Square
import com.kekadoc.tools.storage.Iteration
import com.kekadoc.tools.text.CharSequenceUtils.createSequence
import com.kekadoc.tools.android.AndroidUtils
import com.kekadoc.tools.android.ThemeColor
import com.kekadoc.tools.android.annotation.FractionValue
import com.kekadoc.tools.value.ValueUtils
import java.lang.reflect.Field
import java.util.*

fun View.dpToPx(dp: Float) = AndroidUtils.dpToPx(context, dp)
fun View.spToPx(dp: Float) = AndroidUtils.spToPx(context,dp)
fun View.color(@ColorRes res: Int) = AndroidUtils.getColor(context, res)
fun View.drawable(@DrawableRes res: Int) = AndroidUtils.getDrawable(context, res)
fun View.dimen(@DimenRes res: Int) = AndroidUtils.getDimension(context, res)
fun View.value(@DimenRes res: Int) = AndroidUtils.getValue(context, res)
fun View.text(res: Int) = AndroidUtils.getText(context, res)
fun View.string(@StringRes res: Int) = AndroidUtils.getString(context, res)
fun View.themeColor(@StyleableRes @AttrRes attrColor: Int) = AndroidUtils.getThemeColor(context, attrColor)
fun View.themeColor(themeColor: ThemeColor) = AndroidUtils.getThemeColor(context, themeColor)
fun View.themeDimen(@AttrRes attrDimen: Int) = AndroidUtils.getThemeDimension(context, attrDimen)

object ViewUtils {
    private const val TAG: String = "ViewUtils-TAG"

    @JvmStatic fun ViewsCollector.findAllViews(fields: Any? = null) {
        val clazz: Class<*> = fields?.javaClass ?: javaClass
        ReflectionUtils.getAllFields(clazz, object : Iteration.Single<Field> {
            override fun iteration(item: Field) {
                if (item.isAnnotationPresent(ViewById::class.java)) {
                    val id: Int =
                            Objects.requireNonNull(item.getAnnotation(ViewById::class.java)).id
                    val access = item.isAccessible
                    item.isAccessible = true
                    try {
                        item[fields] = findViewById(id)
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                        Log.e(TAG, "findAllViews Error: ", e)
                    } finally {
                        item.isAccessible = access
                    }
                }
            }
        })
    }

    @JvmStatic fun Activity.findAllViews() {
        if (this is ViewsCollector) (this as ViewsCollector).findAllViews()
        else ViewsCollector.asActivity(this).findAllViews(this)
    }
    @JvmStatic fun View.findAllViews() {
        if (this is ViewsCollector) (this as ViewsCollector).findAllViews()
        else ViewsCollector.asView(this).findAllViews(this)
    }

    @JvmStatic fun setImageDrawableAndVisible(imageView: ImageView, drawable: Drawable?) {
        setImageDrawableAndVisible(imageView, drawable, View.GONE)
    }
    @JvmStatic fun setImageDrawableAndVisible(imageView: ImageView, drawable: Drawable?, visibleIfNull: Int) {
        imageView.setImageDrawable(drawable)
        imageView.visibility = if (drawable == null) visibleIfNull else View.VISIBLE
    }
    @JvmStatic fun setTextAndVisible(textView: TextView, sequence: CharSequence?) {
        setTextAndVisible(textView, sequence, View.GONE)
    }
    @JvmStatic fun setTextAndVisible(textView: TextView, sequence: CharSequence?, visibleIfNull: Int) {
        textView.text = sequence
        textView.visibility = if (sequence == null) visibleIfNull else View.VISIBLE
    }

    @JvmStatic fun viewsTreeInformation(view: View?): String {
        return viewsTreeInformation(view, 1, true)
    }
    private fun viewsTreeInformation(view: View?, tabCount: Int, nl: Boolean): String {
        if (view == null) return "null"
        if (view !is ViewGroup) return view.toString()
        val TABULATION = "____"
        val viewGroup = view
        val childCount = viewGroup.childCount
        val builder = StringBuilder()
        builder.append(if (nl) '\n' else "").append(createSequence(TABULATION, tabCount))
        builder.append(viewGroup).append(" ChildsCount: ").append(childCount)
        if (childCount == 0) return builder.toString()
        builder.append('\n')
        for (i in 0 until childCount) {
            val v = viewGroup.getChildAt(i)
            builder.append(i).append(") ")
            if (v is ViewGroup) builder.append(
                viewsTreeInformation(
                    v,
                    tabCount + 1,
                    false
                )
            ) else builder.append(createSequence(TABULATION, tabCount + 1)).append(v)
            builder.append('\n')
        }
        return builder.toString()
    }

    @JvmStatic fun View.setWidth(width: Int) {
        val lp = layoutParams ?: return
        lp.width = width
        layoutParams = lp
    }
    @JvmStatic fun View.setHeight(height: Int) {
        val lp = layoutParams ?: return
        lp.height = height
        layoutParams = lp
    }

    @JvmStatic fun View.setSize(width: Int, height: Int) {
        var lp = layoutParams
        if (lp == null) lp = ViewGroup.LayoutParams(width, height)
        lp.width = width
        lp.height = height
        layoutParams = lp
    }

    /** Получить коллекцию View  */
    @JvmStatic fun ViewGroup.getAllViews(): List<View> {
        val views: MutableList<View> = ArrayList()
        for (i in 0 until childCount) views.add(getChildAt(i))
        return views
    }

    /** Проверяет лежать ли координаты X и Y в пределах данного View  */
    @JvmStatic fun isPointInBounds(view: View, x: Int, y: Int): Boolean {
        val viewX = view.x.toInt()
        val viewXEnd = viewX + view.width
        val viewY = view.y.toInt()
        val viewYEnd = viewY + view.height
        return viewX <= x && x <= viewXEnd && viewY <= y && y <= viewYEnd
    }

    @JvmStatic fun ProgressBar.setProgress(@FractionValue k: Float): Int {
        val p = (max * k).toInt()
        progress = p
        return p
    }
    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic fun ProgressBar.setProgress(@FractionValue k: Float, animate: Boolean): Int {
        val p = (max * k).toInt()
        setProgress(p, animate)
        return p
    }
    @JvmStatic fun ProgressBar.setProgress(fraction: Fraction): Int {
        val p = (max * fraction.getFraction()).toInt()
        progress = p
        return p
    }
    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic fun ProgressBar.setProgress(fraction: Fraction, animate: Boolean): Int {
        val p = (max * fraction.getFraction()).toInt()
        setProgress(p, animate)
        return p
    }

    @JvmStatic fun <T : View> T.doOnMeasureView(onMeasure: (view: T) -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                onMeasure.invoke(this@doOnMeasureView)
            }
        })
    }

    @JvmStatic fun View.getMargin(side: Square.Edge): Int {
        val lp = layoutParams as? MarginLayoutParams ?: return 0
        return when (side) {
            Square.Edge.LEFT -> lp.leftMargin
            Square.Edge.TOP -> lp.topMargin
            Square.Edge.RIGHT -> lp.rightMargin
            Square.Edge.BOTTOM -> lp.bottomMargin
            else -> 0
        }
    }
    @Size(value = 4)
    @JvmStatic fun View.getMargins(): IntArray {
        val lp = layoutParams as? MarginLayoutParams ?: return intArrayOf(0, 0, 0, 0)
        return intArrayOf(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin)
    }

    @JvmStatic fun View.getMarginLeft(): Int = marginLeft
    @JvmStatic fun View.getMarginTop(): Int = marginTop
    @JvmStatic fun View.getMarginRight(): Int = marginRight
    @JvmStatic fun View.getMarginBottom(): Int = marginBottom

    @JvmStatic fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        if (layoutParams != null && layoutParams is MarginLayoutParams) {
            val lp: MarginLayoutParams = layoutParams as MarginLayoutParams
            lp.setMargins(left, top, right, bottom)
            layoutParams = lp
        }
    }
    @JvmStatic fun View.setMargin(margin: Int) {
        setMargins(margin, margin, margin, margin)
    }
    @JvmStatic fun View.setPadding(padding: Int) {
        setPadding(padding, padding, padding, padding)
    }

    @JvmStatic fun View.show() {
        visibility = View.VISIBLE
    }
    @JvmStatic fun View.hide() {
        visibility = View.INVISIBLE
    }
    @JvmStatic fun View.gone() {
        visibility = View.GONE
    }
    @JvmStatic fun View.isVisible(): Boolean {
        return visibility == View.VISIBLE
    }
    @JvmStatic fun View.isInvisible(): Boolean {
        return visibility == View.INVISIBLE
    }
    @JvmStatic fun View.isGone(): Boolean {
        return visibility == View.GONE
    }

    @JvmStatic fun View.getPositionInParent(): Int {
        if (parent == null) return -1
        val parent = parent as ViewGroup
        return parent.indexOfChild(this)
    }
    @JvmStatic fun View.isLastInParent(): Boolean {
        if (parent == null) return false
        val parent = parent as ViewGroup
        return parent.getChildAt(parent.childCount - 1) == this
    }
    @JvmStatic fun View.removeFromParent() {
        val viewGroup = parent as ViewGroup
        viewGroup.removeView(this)
    }

    @JvmStatic fun View.getFullWidth(view: View): Int {
        return view.width + getMarginLeft() + getMarginRight()
    }
    @JvmStatic fun View.getFullHeight(view: View): Int {
        return view.height + getMarginTop() + getMarginBottom()
    }

}