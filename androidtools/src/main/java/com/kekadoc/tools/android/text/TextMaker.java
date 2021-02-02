package com.kekadoc.tools.android.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.MaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.IconMarginSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.text.style.LocaleSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuggestionSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TabStopSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.kekadoc.tools.exeption.NotImplementedException;
import com.kekadoc.tools.android.AndroidUtils;
import com.kekadoc.tools.android.R;
import com.kekadoc.tools.android.view.size.SizeValue;
import com.kekadoc.tools.android.view.size.Sizing;

import java.util.Locale;

public final class TextMaker {
    private static final String TAG = "TextBuilder-TAG";

    private TextMaker() {}

    public static CharSequence createIconText(@Nullable Drawable icon, @IconSpan.AlignType int align, int iconSize, int iconPadding, CharSequence text) {
        if (icon == null) return text;
        IconSpan span = new IconSpan(icon, align, iconSize, iconPadding);
        SpannableString string = new SpannableString(text);
        string.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }
    public static CharSequence createIconText(@Nullable Drawable icon, int iconSize, int iconPadding, CharSequence text) {
        if (icon == null) return text;
        IconSpan span = new IconSpan(icon, IconSpan.ALIGN_CENTER, iconSize, iconPadding);
        SpannableString string = new SpannableString(text);
        string.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    public static CharSequence createIconText(@NonNull Context context, @Nullable Drawable icon,
                                              @IconSpan.AlignType int align, @SizeValue int iconSize, CharSequence text) {
        if (icon == null) return text;
        int padding, size;
        switch (iconSize) {
            case Sizing.SIZE_EXTRA_SMALL:
                padding = (int) AndroidUtils.dpToPx(context, 2);
                size = (int) AndroidUtils.getDimension(context, R.dimen.size_icon_extra_small);
                break;
            case Sizing.SIZE_SMALL:
                padding = (int) AndroidUtils.dpToPx(context, 4);
                size = (int) AndroidUtils.getDimension(context, R.dimen.size_icon_small);
                break;
            case Sizing.SIZE_MEDIUM:
                padding = (int) AndroidUtils.dpToPx(context, 6);
                size = (int) AndroidUtils.getDimension(context, R.dimen.size_icon_medium);
                break;
            case Sizing.SIZE_LARGE:
                padding = (int) AndroidUtils.dpToPx(context, 8);
                size = (int) AndroidUtils.getDimension(context, R.dimen.size_icon_large);
                break;
            case Sizing.SIZE_EXTRA_LARGE:
                padding = (int) AndroidUtils.dpToPx(context, 10);
                size = (int) AndroidUtils.getDimension(context, R.dimen.size_icon_extra_large);
                break;
            default: throw new NotImplementedException();
        }
        IconSpan span = new IconSpan(icon, align, size, padding);
        SpannableString string = new SpannableString(text);
        string.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }
    public static CharSequence createIconText(@NonNull Context context, @Nullable Drawable icon, @SizeValue int iconSize, CharSequence text) {
        if (icon == null) return text;
        int padding, size;
        switch (iconSize) {
            case Sizing.SIZE_EXTRA_SMALL:
                padding = (int) AndroidUtils.dpToPx(context, 4);
                size = (int) AndroidUtils.getDimension(context, R.dimen.size_icon_extra_small);
                break;
            case Sizing.SIZE_SMALL:
                padding = (int) AndroidUtils.dpToPx(context, 6);
                size = (int) AndroidUtils.getDimension(context, R.dimen.size_icon_small);
                break;
            case Sizing.SIZE_MEDIUM:
                padding = (int) AndroidUtils.dpToPx(context, 8);
                size = (int) AndroidUtils.getDimension(context, R.dimen.size_icon_medium);
                break;
            case Sizing.SIZE_LARGE:
                padding = (int) AndroidUtils.dpToPx(context, 10);
                size = (int) AndroidUtils.getDimension(context, R.dimen.size_icon_large);
                break;
            case Sizing.SIZE_EXTRA_LARGE:
                padding = (int) AndroidUtils.dpToPx(context, 12);
                size = (int) AndroidUtils.getDimension(context, R.dimen.size_icon_extra_large);
                break;
            default: throw new NotImplementedException();
        }

        IconSpan span = new IconSpan(icon, IconSpan.ALIGN_CENTER, size, padding);
        SpannableString string = new SpannableString(text);
        string.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }
    public static CharSequence createIconText(@NonNull Context context, @Nullable Drawable icon, CharSequence text) {
        return createIconText(context, icon, Sizing.SIZE_MEDIUM, text);
    }
    public static CharSequence createIconText(@NonNull IconSpan iconSpan, CharSequence text) {
        SpannableString string = new SpannableString(text);
        string.setSpan(iconSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }


    public static CharSequence createIconText(@Nullable Bitmap icon, int iconSize, int iconPadding, CharSequence text) {
        if (icon == null) return text;
        icon = Bitmap.createScaledBitmap(icon, iconSize, iconSize, true);
        SpannableString string = new SpannableString(text);
        string.setSpan(new IconMarginSpan(icon, iconPadding), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    //SpannedString - Только чтение текста
    //SpannableString - Установка Span
    //SpannableStringBuilder - Установка большего количества Span

    //CharacterStyle - Влияние на текст на уровне персонажа
    //ParagraphStyle - Влияние на текст на уровне абзаца
    //UpdateAppearance - Влияет на внешний вид текста
    //UpdateLayout - Влияние на текстовые метрики

    // MetricAffectingSpan - Классы, которые влияют на форматирование текста на уровне символов, изменяя ширину или высоту символов, расширяют этот класс.
    /**
     * Картинка в указанной позиции
     */
    private static ImageSpan createImageSpan(ImageSpan span) {
        //return new ImageSpan();
        return span;
    }
    /**
     * Текст с указанным размером
     */
    private static AbsoluteSizeSpan createAbsoluteSizeSpan(int size, boolean dp) {
        return new AbsoluteSizeSpan(size, dp);
    }
    /**
     * Locale
     */
    private static LocaleSpan createLocaleSpan(Locale locale) {
        return new LocaleSpan(locale);
    }
    /**
     *
     */
    private static RelativeSizeSpan createRelativeSizeSpan() {
        return new RelativeSizeSpan(2f);
    }
    /**
     * Масштабирует по горизонтали размер текста, к которому он прикреплен, с помощью определенного фактора.
     */
    private static ScaleXSpan createScaleXSpan(float p) {
        return new ScaleXSpan(p);
    }
    /**
     * Style
     */
    private static StyleSpan createStyleSpan(int style) {
        return new StyleSpan(style);
    }
    /**
     * Опускание текста
     */
    private static SubscriptSpan createSubscriptSpan() {
        return new SubscriptSpan();
    }
    /**
     * Возвыщение текста
     */
    private static SuperscriptSpan createSuperscriptSpan() {
        return new SuperscriptSpan();
    }
    /**
     * TextAppearance
     */
    private static TextAppearanceSpan createTextAppearanceSpan(Context context, int style, int colorList) {
        return new TextAppearanceSpan(context, style, colorList);
    }
    /**
     * Typeface
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private static TypefaceSpan createTypefaceSpan(Typeface typeface) {
        return new TypefaceSpan(typeface);
    }


    // ParagraphStyle Изменение абзацев
    /**
     * Указать горизонтальное направление текста
     */
    private static AlignmentSpan.Standard createAlignmentSpan(Layout.Alignment alignment) {
        return new AlignmentSpan.Standard(alignment);
    }
    /**
     * Точка в начале
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private static BulletSpan createBulletSpan(int gapWidth, @ColorInt int color, @IntRange(from = 0) int bulletRadius) {
        return new BulletSpan(gapWidth, color, bulletRadius);
    }
    /**
     * Добавление Drawable в начале
     */
    private static DrawableMarginSpan createDrawableMarginSpan(Drawable drawable, int pad) {
        return new DrawableMarginSpan(drawable, pad);
    }
    /**
     * Bitmap в начале
     */
    private static IconMarginSpan createIconMarginSpan(@NonNull Bitmap bitmap, int pad) {
        return new IconMarginSpan(bitmap, pad);
    }
    /**
     * Отступы
     */
    private static LeadingMarginSpan.Standard createLeadingMarginSpan() {
        return new LeadingMarginSpan.Standard(0, 1);
    }
    /**
     * Отступы
     */
    private static LeadingMarginSpan.LeadingMarginSpan2.Standard createLeadingMarginSpan2() {
        return new LeadingMarginSpan.LeadingMarginSpan2.Standard(0, 1);
    }
    /**
     * Высота строки
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static LineHeightSpan.Standard createLineHeightSpan(int h) {
        return new LineHeightSpan.Standard(h);
    }
    /**
     * Вертикальная линия слева
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private static QuoteSpan createQuoteSpan(@ColorInt int color, @IntRange(from = 0) int stripeWidth, @IntRange(from = 0) int gapWidth) {
        return new QuoteSpan(color, stripeWidth, gapWidth);
    }
    /**
     * Табуляция первой строки
     */
    private static TabStopSpan.Standard createTabStopSpan() {
        return new TabStopSpan.Standard(20);
    }

    // CharacterStyle Изменение символов
    /**
     * Цвет фона у текста
     */
    private static BackgroundColorSpan createBackgroundColorSpan(int color) {
        return new BackgroundColorSpan(color);
    }
    /**
     * Выделение текста и делание его кликабельным
     */
    private static ClickableSpan createClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Log.e(TAG, "onClick: ");
            }
        };
    }
    /**
     * Цвет текста
     */
    private static ForegroundColorSpan createForegroundColorSpan(int color) {
        return new ForegroundColorSpan(color);
    }
    /**
     * Фильтр для текста
     */
    private static MaskFilterSpan createMaskFilterSpan(MaskFilter maskFilter) {
        return new MaskFilterSpan(maskFilter);
    }
    /**
     * Зачеркнутый текст
     */
    private static StrikethroughSpan createStrikethroughSpan() {
        return new StrikethroughSpan();
    }
    /**
     * Выбор для EditText
     */
    private static SuggestionSpan createSuggestionSpan(Context context, String[] strings, int flags) {
        return new SuggestionSpan(context, strings, flags);
    }
    /**
     * Подчеркивание
     */
    private static UnderlineSpan createUnderlineSpan() {
        return new UnderlineSpan();
    }
    /**
     * URL Clicked
     */
    private static URLSpan createUrlSpan(String url) {
        return new URLSpan(url);
    }

}
