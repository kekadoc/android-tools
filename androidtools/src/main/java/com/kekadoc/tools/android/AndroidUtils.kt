package com.kekadoc.tools.android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import java.util.*

object AndroidUtils {
    private const val TAG = "AndroidUtils-TAG"

    @JvmStatic
    fun View.test() {

    }

    fun getDefaultExpandableListGroupIndicator(context: Context): Drawable? {
        val drawable: Drawable?
        val expandableListViewStyle =
            context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.expandableListViewStyle))
        val groupIndicator = context.theme.obtainStyledAttributes(
            expandableListViewStyle.getResourceId(0, 0),
            intArrayOf(android.R.attr.groupIndicator)
        )
        drawable = groupIndicator.getDrawable(0)
        expandableListViewStyle.recycle()
        groupIndicator.recycle()
        return drawable
    }
    fun getDefaultExpandableListChildIndicator(context: Context): Drawable? {
        val drawable: Drawable?
        val expandableListViewStyle =
            context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.expandableListViewStyle))
        val groupIndicator = context.theme.obtainStyledAttributes(
            expandableListViewStyle.getResourceId(0, 0),
            intArrayOf(android.R.attr.childIndicator)
        )
        drawable = groupIndicator.getDrawable(0)
        expandableListViewStyle.recycle()
        groupIndicator.recycle()
        return drawable
    }

    @JvmStatic
    inline fun testLog(name: String?, action: () -> Unit) {
        val t = System.currentTimeMillis()
        val tt = System.nanoTime()
        action.invoke()
        Log.e(
            name,
            "ms: " + (System.currentTimeMillis() - t) + " " + "ns: " + (System.nanoTime() - tt)
        )
    }
    @JvmStatic
    inline fun <T> testLog(name: String?, action: () -> T): T {
        val t = System.currentTimeMillis()
        val tt = System.nanoTime()
        val r: T = action.invoke()
        Log.e(
            name,
            "ms: " + (System.currentTimeMillis() - t) + " " + "ns: " + (System.nanoTime() - tt)
        )
        return r
    }

    @JvmStatic fun fromHtml(source: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(source)
        }
    }

    @JvmStatic fun sleep(ms: Long) {
        SystemClock.sleep(ms)
    }

    @JvmStatic fun isUiThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }

    @JvmStatic fun getActivityScreenshot(activity: Activity): Bitmap? {
        Objects.requireNonNull<Any>(activity, "Activity is Null!")
        val root: View = activity.findViewById(android.R.id.content)
        val coverImage: Bitmap?
        coverImage = try {
            root.isDrawingCacheEnabled = true
            val base: Bitmap = root.drawingCache
            if (root.drawingCache == null) return base
            base.copy(base.config, false)
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to create screenshot", ex)
            null
        } finally {
            root.isDrawingCacheEnabled = false
        }
        return coverImage
    }

    @JvmStatic
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @JvmStatic fun getLocale(): Locale {
        return  Resources.getSystem().configuration.locale
    }

    @JvmStatic
    fun runInMainThread(r: Runnable): Handler {
        val handler = Handler(Looper.getMainLooper())
        handler.post(r)
        return handler
    }

    @JvmStatic fun init(context: Context) {
        ThemeColor.init(context)
    }

    @JvmStatic fun spToPx(context: Context, sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }
    @JvmStatic fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

    @JvmStatic fun requireContext(context: Context) {
        Objects.requireNonNull(context, "Context is Null!")
    }
    @JvmStatic fun getDeviceMetrics(context: Context): DisplayMetrics {
        val metrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getMetrics(metrics)
        return metrics
    }
    @ColorInt
    @JvmStatic fun getThemeColor(context: Context, attrColor: ThemeColor): Int {
        return getThemeColor(context, attrColor.attrColor)
    }
    @ColorInt
    @JvmStatic fun getThemeColors(context: Context, vararg attrColor: ThemeColor): IntArray {
        @StyleableRes val res = IntArray(attrColor.size)
        for (i in 0 until attrColor.size) res[i] = attrColor[i].attrColor
        return getThemeColors(context, *res)
    }
    @ColorInt
    @JvmStatic fun getThemeColor(context: Context, @StyleableRes @AttrRes attrColor: Int): Int {
        return getThemeColors(context, attrColor)[0]
    }
    @ColorInt
    @JvmStatic fun getThemeColors(context: Context, @StyleableRes @AttrRes vararg attrColor: Int): IntArray {
        val typedValue = TypedValue()
        val a = context.obtainStyledAttributes(typedValue.data, attrColor)
        val colors = IntArray(attrColor.size)
        for (i in 0 until attrColor.size) colors[i] = a.getColor(i, 0)
        a.recycle()
        return colors
    }

    @Dimension
    @Px
    @JvmStatic fun getThemeDimension(context: Context, defValue: Int, @AttrRes attrDimen: Int): Float {
        return getThemeDimensions(context, floatArrayOf(defValue.toFloat()), attrDimen)[0]
    }

    @Dimension
    @Px
    @JvmStatic fun getThemeDimension(context: Context, @AttrRes attrDimen: Int): Float {
        return getThemeDimensions(context, attrDimen)[0]
    }

    @Dimension
    @Px
    @JvmStatic fun getThemeDimensions(
        context: Context,
        @StyleableRes @AttrRes vararg attrDimen: Int
    ): FloatArray {
        val a = context.theme.obtainStyledAttributes(attrDimen)
        val dimens = FloatArray(attrDimen.size)
        for (i in 0 until attrDimen.size) {
            dimens[i] = a.getDimension(i, 0f)
        }
        a.recycle()
        return dimens
    }

    @Dimension
    @Px
    @JvmStatic fun getThemeDimensions(
        context: Context,
        defValues: FloatArray,
        @StyleableRes @AttrRes vararg attrDimen: Int
    ): FloatArray {
        val a = context.theme.obtainStyledAttributes(attrDimen)
        val dimens = FloatArray(attrDimen.size)
        for (i in 0 until attrDimen.size) {
            dimens[i] = a.getDimension(i, defValues[i])
        }
        a.recycle()
        return dimens
    }
    @JvmStatic fun getRippleBackgroundBorderless(context: Context): Int {
        val outValue = TypedValue()
        context.theme.resolveAttribute(
            android.R.attr.selectableItemBackgroundBorderless,
            outValue,
            true
        )
        return outValue.resourceId
    }
    @JvmStatic fun getRippleBackground(context: Context): Int {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        return outValue.resourceId
    }
    @JvmStatic @ColorInt fun getColor(context: Context, @ColorRes color: Int): Int {
        if (color == 0) return 0
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) context.getColor(color) else context.resources.getColor(
            color
        )
    }
    @JvmStatic fun getDrawable(context: Context, @DrawableRes res: Int): Drawable? {
        if (res == 0) return null
        return ResourcesCompat.getDrawable(context.resources, res, context.theme)
    }
    @JvmStatic fun getDrawables(context: Context, @DrawableRes vararg res: Int): Array<Drawable?> {
        if (res == null) return arrayOfNulls(0)
        val drawables = arrayOfNulls<Drawable>(res.size)
        for (i in 0 until res.size) drawables[i] = getDrawable(context, res[i])
        return drawables
    }
    @JvmStatic fun getDimension(context: Context, @DimenRes res: Int): Float {
        return if (res == 0) 0f else context.resources.getDimension(res)
    }
    @JvmStatic fun getValue(context: Context, @DimenRes res: Int, resolveRefs: Boolean): TypedValue {
        val tv = TypedValue()
        context.resources.getValue(res, tv, resolveRefs)
        return tv
    }
    @JvmStatic fun getValue(context: Context, @DimenRes res: Int): TypedValue {
        return getValue(context, res, true)
    }
    @JvmStatic fun getFloatValue(context: Context, @DimenRes res: Int): Float {
        return getValue(context, res, true).float
    }
    @JvmStatic fun getText(context: Context, res: Int): CharSequence? {
        return if (res == 0) null else context.resources.getText(res, null)
    }
    @JvmStatic fun getString(context: Context, @StringRes res: Int): String? {
        return if (res == 0) null else context.resources.getString(res)
    }
    @JvmStatic fun getStringArray(context: Context, @ArrayRes res: Int): Array<String>? {
        return if (res == 0) null else context.resources.getStringArray(res)
    }
    @JvmStatic fun getString(context: Context, @StringRes res: Int, vararg args: Any?): String? {
        return if (res == 0) null else context.resources.getString(res, *args)
    }

    enum class ThemeColor(val attrColor: Int) {
        PRIMARY(R.attr.colorPrimary), ON_PRIMARY(R.attr.colorOnPrimary), PRIMARY_VARIANT(R.attr.colorPrimaryVariant), PRIMARY_DARK(
            R.attr.colorPrimaryDark
        ), SECONDARY(R.attr.colorSecondary), ON_SECONDARY(R.attr.colorOnSecondary), SECONDARY_VARIANT(
            R.attr.colorSecondaryVariant
        ), ACCENT(R.attr.colorAccent), ERROR(R.attr.colorError), ON_ERROR(R.attr.colorOnError), SURFACE(
            R.attr.colorSurface
        ), ON_PRIMARY_SURFACE(R.attr.colorOnPrimarySurface), ON_SURFACE(R.attr.colorOnSurface), ON_BACKGROUND(
            R.attr.colorOnBackground
        ), RIPPLE(R.attr.colorControlHighlight);

        @ColorInt
        var color = 0
            private set

        companion object {
            fun init(context: Context) {
                val themeColors = values()
                val colors = getThemeColors(context, *themeColors)
                for (i in themeColors.indices) themeColors[i].color = colors[i]
            }
        }

    }

}