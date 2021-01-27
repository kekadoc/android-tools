package com.qeapp.tools.android.view

import android.graphics.Color
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
import com.qeapp.tools.android.QeAndroid
import com.qeapp.tools.annotations.Coefficient
import com.qeapp.tools.fraction.Fraction
import com.qeapp.tools.reflection.QeReflection
import com.qeapp.tools.shape.square.Square
import com.qeapp.tools.storage.Iteration
import com.qeapp.tools.text.QeCharSequence.createSequence
import java.lang.reflect.Field
import java.util.*

object QeViews {
    private const val TAG: String = "QeViews-TAG"

    @JvmStatic fun findAllViews(from: Any, container: View) {
        QeReflection.getAllFields(from.javaClass, object : Iteration.Single<Field> {
            override fun iteration(item: Field) {
                if (item.isAnnotationPresent(ViewById::class.java)) {
                    val id: Int =
                        Objects.requireNonNull(item.getAnnotation(ViewById::class.java)).id
                    val access = item.isAccessible
                    item.isAccessible = access
                    try {
                        item[from] = container.findViewById(id)
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
    @JvmStatic fun View.findAllViews() {
        findAllViews(this, this)
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

    @JvmStatic fun ProgressBar.setProgress(@Coefficient k: Float): Int {
        val p = (max * k).toInt()
        progress = p
        return p
    }
    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic fun ProgressBar.setProgress(@Coefficient k: Float, animate: Boolean): Int {
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

    @JvmStatic fun View.getMargin(side: Square.Side): Int {
        val lp = layoutParams as? MarginLayoutParams ?: return 0
        return when (side) {
            Square.Side.LEFT -> lp.leftMargin
            Square.Side.TOP -> lp.topMargin
            Square.Side.RIGHT -> lp.rightMargin
            Square.Side.BOTTOM -> lp.bottomMargin
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


    @JvmStatic fun View.dpToPx(dp: Float) : Float = QeAndroid.dpToPx(context, dp)
    @JvmStatic fun View.string(@StringRes res: Int): String? {
        return if (res == 0) null else context.getString(res)
    }
    @JvmStatic fun View.drawable(@DrawableRes res: Int): Drawable? {
        return if (res == 0) null else QeAndroid.getDrawable(context, res)
    }
    @JvmStatic fun View.dimen(@DimenRes res: Int): Float {
        return if (res == 0) 0f else context.resources.getDimension(res)
    }
    @JvmStatic fun View.color(@ColorRes res: Int): Int {
        return if (res == 0) Color.TRANSPARENT else QeAndroid.getColor(context, res)
    }
    @JvmStatic fun View.stringArray(@ArrayRes res: Int): Array<String?>? {
        return if (res == 0) null else context.resources.getStringArray(res)
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