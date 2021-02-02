package com.kekadoc.tools.android.text;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Annotation;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.kekadoc.tools.ObjectUtils;
import com.kekadoc.tools.character.Chars;
import com.kekadoc.tools.exeption.Wtf;
import com.kekadoc.tools.android.AndroidUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 * Класс для работы с Spannable текстом из ресурса.
 * Для работы передается String ресурс помеченный специальными аннотациями
 * @see Annotation
 * @see com.kekadoc.tools.android.R.string.string_spannable_test Example
 *
 */
public class SpannableText implements Spannable {
    private static final String TAG = "SpannableResText-TAG";

    /*
     * Если используется format то это значит что размерность строки изменится и необходимо помечать
     *
     * 1) Изъятие из ресурсов
     * 2) Поиск всех аннотаций
     * 3) Пометка мест начала и конца анотаций соответсвующими маркерами
     * 4) Преобразование строки
     * 5) Парсинг измененной строки на наличие маркеров
     * 6) Запоминание местоположения всех маркиво и удаление их (Так как на данный момент строка уже изменена то можно запомнить начало и длину анатированного поля)
     * 7) Обработка сохраненнных диапазонов
     *
     */

    private static @NonNull Spanned pullOutText(@NonNull Context context, @StringRes int id) {
        CharSequence txt = Objects.requireNonNull(AndroidUtils.getText(context, id), "Error inflating resource");
        if (txt instanceof Spanned) return (Spanned) txt;
        else return new SpannedString(txt);
    }

    private static CharSequence createFormatMarker(int code, boolean open) {
        return Character.toString(MARK_MARKER) + (open ? MARK_OPEN : MARK_CLOSE) + MARK_START + MARK_TYPE + MARK_FORMAT + MARK_CODE + code + MARK_END;
    }

    /**
     * Указание размера.
     * Если указано просто число то оно считается в пикселях (PX).
     * Написать "dp" или "sp" в конце числа что бы воспринимальсь как DP
     */
    private static final String ANNOTATION_KEY_SIZE = "size";
    /**
     * @see Typeface
     */
    private static final String ANNOTATION_KEY_STYLE = "style";
    /**
     * Опускание текста
     * @see SubscriptSpan
     */
    private static final String ANNOTATION_KEY_SUBSCRIPT = "subscript";
    /**
     * Возвышение текста
     * @see SuperscriptSpan
     */
    private static final String ANNOTATION_KEY_SUPERSCRIPT = "superscript";
    /**
     * Цвет фона теста
     * @see BackgroundColorSpan
     */
    private static final String ANNOTATION_KEY_BGCOLOR = "bgcolor";
    /**
     * Цвет текста
     * @see ForegroundColorSpan
     */
    private static final String ANNOTATION_KEY_FGCOLOR = "fgcolor";
    /**
     * Перечеркивание
     * @see StrikethroughSpan
     */
    private static final String ANNOTATION_KEY_STRIKETHROUGH = "strikethrough";
    /**
     * Подчеркивание
     * @see UnderlineSpan
     */
    private static final String ANNOTATION_KEY_UNDERLINE = "underline";
    /**
     * FORMAT
     * @see String#format(String, Object...)
     */
    private static final String ANNOTATION_KEY_FORMAT = "format";


    //?<{&Type#3}  ?>{&Type#3}
    protected static final char MARK_MARKER = '?';
    protected static final char MARK_OPEN = '<';
    protected static final char MARK_CLOSE = '>';
    protected static final char MARK_START = '{';
    protected static final char MARK_END = '}';
    protected static final char MARK_TYPE = '&';
    protected static final char MARK_CODE = '#';
    protected static final char MARK_FORMAT = 'F';

    private static final int POSITION_MARKER = 0;
    private static final int POSITION_OPEN_CLOSE = 1;
    private static final int POSITION_START = 2;
    private static final int POSITION_TYPE_MARK = 3;
    private static final int POSITION_TYPE = 4;
    private static final int POSITION_CODE_MARK = 5;
    private static final int POSITION_CODE = 6; //6...

    /**
     * Текст вытащенный из ресурсов
     */
    private @Nullable Spanned baseText;
    /**
     * Конечный Spannable результат
     */
    private Spannable spannable;
    
    /**
     * Промежуточный билдер
     */
    private StringBuilder builder;

    private static final int OUT_BOUNDS = -1;
    
    /**
     * Счетчик маркеров. Используется только в StringFormat
     */
    private int markerIndex = OUT_BOUNDS;
    
    private @Nullable List<ArgPath> paths;

    /**
     * Коллекция всех аннотаций
     */
    private final @NonNull List<Region> regions = new ArrayList<>();

    private int blink = 0;

    /**
     * Стандартный конструктор без инициации
     */
    public SpannableText() {
    }

    /**
     * Конструктор с указанием Spanned и ее возможными аргументами
     */
    public SpannableText(@NonNull Spanned text, @Nullable ArgPath...paths) {
        this.baseText = text;
        if (paths != null && paths.length != 0) this.paths = Arrays.asList(paths);
        makeSpannable();
    }
    public SpannableText(@NonNull Spanned text) {
        this(text, (ArgPath[]) null);
    }

    /**
     * Конструктор с указанием ресурса
     */
    public SpannableText(@NonNull Context context, @StringRes int res, ArgPath...paths) {
        this(pullOutText(context, res), paths);
    }
    public SpannableText(@NonNull Context context, @StringRes int res) {
        this(pullOutText(context, res));
    }


    public @Nullable List<ArgPath> getArgs() {
        return paths;
    }
    public void setArgs(List<ArgPath> args) {
        this.paths = args;
    }

    public @Nullable Spanned getSpanned() {
        return baseText;
    }
    public void setSpanned(@Nullable Spanned baseText) {
        this.baseText = baseText;
        clearCaches();
    }

    public void clearCaches() {
        this.blink = 0;
        this.builder = null;
        this.regions.clear();
        this.paths = null;
        this.markerIndex = OUT_BOUNDS;
        this.spannable = null;
    }

    public @Nullable Spannable makeSpannable() {
        if (getSpanned() == null) return null;
        //Поиск всех аннотаций если они еще не были найдены
        if (regions.isEmpty()) findAllAnnotation(getSpanned());

        //Если ширина может измениться то расстановка маркеров
        if (isSizeChangeable()) {
            Object[] args = makeArgs();
            CharSequence txt = String.format(builder.toString(), Objects.requireNonNull(args));
            findAllMarkers(txt);
            this.spannable = new SpannableString(String.format(getSpanned().toString(), args));
        } else this.spannable = new SpannableString(getSpanned());

        applyRegions(this.spannable);
        return this.spannable;
    }

    public final @Nullable Spannable getSpannable() {
        return spannable;
    }

    public final boolean isSizeChangeable() {
        return markerIndex > OUT_BOUNDS;
    }

    /**
     * Вызывается когда в тесте была найдена Annotation и необходимо создать новый Region для нее.
     */
    protected Region onAnnotationFind(@NonNull Spanned sequence, @NonNull Annotation annotation) {
        if (annotation.getKey().equals(ANNOTATION_KEY_FORMAT)) return onFindStringFormatAnnotation(sequence, annotation);
        else return new Region(sequence, annotation);
    }

    protected void applySpan(@NonNull Spannable text, @NonNull Region region) {
        switch (region.getKey()) {
            case ANNOTATION_KEY_SIZE: applySpan_size(text, region); return;
            case ANNOTATION_KEY_STYLE: applySpan_style(text, region); return;
            case ANNOTATION_KEY_SUBSCRIPT: applySpan_subscript(text, region); return;
            case ANNOTATION_KEY_SUPERSCRIPT: applySpan_superscript(text, region); return;
            case ANNOTATION_KEY_BGCOLOR: applySpan_bgcolor(text, region); return;
            case ANNOTATION_KEY_FGCOLOR: applySpan_fgcolor(text, region); return;
            case ANNOTATION_KEY_STRIKETHROUGH: applySpan_strikethrough(text, region); return;
            case ANNOTATION_KEY_UNDERLINE: applySpan_underline(text, region); return;
            case ANNOTATION_KEY_FORMAT: applySpan_format(text, region); return;
            default: throw new Wtf();
        }
    }

    protected void applySpan_size(@NonNull Spannable text, @NonNull Region region) {
        String val = region.getValue();
        if (!Chars.isNumber(val.charAt(0))) throw new Wtf();
        int s = 0;
        boolean dp = false;
        for (int i = 0; i < val.length(); i++) {
            if (!Chars.isNumber(val.charAt(i))) {
                s = Integer.parseInt(val.substring(0, i));
                CharSequence last = val.subSequence(val.length() - 2, val.length());
                if (last.equals("dp") || last.equals("sp")) dp = true;
                break;
            }
        }
        text.setSpan(new AbsoluteSizeSpan(s, dp), region.start, region.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    protected void applySpan_style(@NonNull Spannable text, @NonNull Region region) {
        String val = region.getValue();
        Object what;
        switch (val) {
            case "bold": what = new StyleSpan(Typeface.BOLD); break;
            case "bold_italic": what = new StyleSpan(Typeface.BOLD_ITALIC); break;
            case "italic": what = new StyleSpan(Typeface.ITALIC); break;
            default: what = new StyleSpan(Typeface.NORMAL); break;
        }
        text.setSpan(what, region.getStart(), region.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    protected void applySpan_subscript(@NonNull Spannable text, @NonNull Region region) {
        text.setSpan(new SubscriptSpan(), region.getStart(), region.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    protected void applySpan_superscript(@NonNull Spannable text, @NonNull Region region) {
        text.setSpan(new SuperscriptSpan(), region.getStart(), region.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    protected void applySpan_bgcolor(@NonNull Spannable text, @NonNull Region region) {
        text.setSpan(new BackgroundColorSpan(Color.parseColor(region.getValue())), region.start, region.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    protected void applySpan_fgcolor(@NonNull Spannable text, @NonNull Region region) {
        text.setSpan(new ForegroundColorSpan(Color.parseColor(region.getValue())), region.start, region.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    protected void applySpan_strikethrough(@NonNull Spannable text, @NonNull Region region) {
        text.setSpan(new StrikethroughSpan(), region.start, region.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    protected void applySpan_underline(@NonNull Spannable text, @NonNull Region region) {
        text.setSpan(new UnderlineSpan(), region.start, region.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    protected void applySpan_format(@NonNull Spannable text, @NonNull Region region) {
        Region.Formatter formatter = (Region.Formatter) region;
        for (Object w : formatter.getPath().getWhats())
            text.setSpan(w, formatter.getStart(), formatter.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private interface MarkerFinder {
        void onMarkerFind(@NonNull CharSequence sequence, boolean open, int startMarker, CharSequence marker, char type, int code);
    }

    private void findAllMarkers(@NonNull CharSequence sequence) {
        findMarker(sequence, true, 0, new MarkerFinder() {
            @Override
            public void onMarkerFind(@NonNull CharSequence sequence, boolean open, int startMarkerPos, CharSequence startMarker, char startType, int startCode) {
                findMarker(sequence, false, startMarkerPos + startMarker.length(), new MarkerFinder() {
                    @Override
                    public void onMarkerFind(@NonNull CharSequence sequence, boolean open, int endMarker, CharSequence marker, char type, int code) {
                        if (type == startType && startCode == code) onFindTwinsMarker(sequence, startMarkerPos, startMarker, endMarker, marker, type, code);
                    }
                });
            }
        });
    }
    private void findMarker(@NonNull CharSequence sequence, boolean open, int start, MarkerFinder markerFinder) {
        char c;
        for (int i = start; i < sequence.length(); i++) {
            c = sequence.charAt(i);
            if (c == MARK_MARKER) {
                boolean o;
                if (sequence.charAt(i + POSITION_OPEN_CLOSE) == MARK_OPEN && open) o = true;
                else if (sequence.charAt(i + POSITION_OPEN_CLOSE) == MARK_CLOSE && !open) o = false;
                else continue;
                if (sequence.charAt(i + POSITION_START) != MARK_START) continue;
                if (sequence.charAt(i + POSITION_TYPE_MARK) != MARK_TYPE) continue;
                char type = sequence.charAt(i + POSITION_TYPE);
                if (sequence.charAt(i + POSITION_CODE_MARK) != MARK_CODE) continue;
                int code = Chars.parseNumber(sequence, i + POSITION_CODE_MARK);
                for (int j = i + POSITION_CODE; j < sequence.length(); j++) {
                    if (sequence.charAt(j) == MARK_END) {
                        markerFinder.onMarkerFind(sequence, o, i, sequence.subSequence(i, j + 1), type, code);
                        break;
                    }
                }
            }
        }
    }
    private void onFindTwinsMarker(@NonNull CharSequence sequence, int startMarker, CharSequence startM, int endMarker, CharSequence endM, char type, int code) {
        int markerSpace = (endMarker) - (startMarker + startM.length());

        Region thisRegion = null;
        int oldSpace = 0;
        for (Region region : regions) {
            if (region instanceof Region.Formatter)
                if (((Region.Formatter) region).equals(type, code)) {
                    thisRegion = region;
                }
        }
        oldSpace = Objects.requireNonNull(thisRegion).range();
        int difference = markerSpace - oldSpace;
        thisRegion.end += difference;
        for (Region region : regions) {
            if (region.equals(thisRegion)) continue;
            if (thisRegion.start < region.start) {
                region.start += difference;
            }
            if (thisRegion.start < region.end) {
                region.end += difference;
            }
        }
    }
    private Region onFindStringFormatAnnotation(@NonNull Spanned sequence, @NonNull Annotation annotation) {
        markerIndex++;
        Region.Formatter region = new Region.Formatter(sequence, annotation, MARK_FORMAT, markerIndex);

        CharSequence format = region.subSequence(sequence);
        if (format.charAt(0) != '%') throw new Wtf("Not correct format");
        if (Chars.isNumber(format.charAt(1))) {
            int number = Integer.parseInt(Character.toString(format.charAt(1)));
            if (Chars.isNumber(format.charAt(2))) throw new Wtf("Число должен быть меньще 10!");
            if (paths == null) throw new Wtf("Args not found");
            region.setPath(paths.get(number - 1));
        }

        CharSequence openMarker = createFormatMarker(markerIndex, true);
        CharSequence closeMarker = createFormatMarker(markerIndex, false);
        insertInText(region.getStart() + blink, openMarker);
        insertInText(region.getStart() + openMarker.length() + region.range() + blink, closeMarker);
        blink += openMarker.length() + closeMarker.length();
        return region;
    }
    private void applyRegions(Spannable text) {
        for (Region region : regions) applySpan(text, region);
    }
    /**
     * Поиск всех аннотаций и отправка в callback
     */
    private void findAllAnnotation(@NonNull Spanned text) {
        Annotation[] annotations = text.getSpans(0, text.length(), Annotation.class);
        for (Annotation annotation : annotations) {
            Region region = onAnnotationFind(text, annotation);
            this.regions.add(region);
        }
    }
    private Object[] makeArgs() {
        if (paths == null || paths.isEmpty()) return null;
        Object[] args = new Object[paths.size()];
        for (int i = 0; i < args.length; i++) {
            ArgPath path = paths.get(i);
            args[i] = path.getArg();
        }
        return args;
    }
    private void insertInText(int pos, CharSequence sequence) {
        if (builder == null) builder = new StringBuilder(baseText);
        this.builder.insert(pos, sequence);
    }

    //region Spannable
    @Override
    public void setSpan(Object what, int start, int end, int flags) {
        if (spannable != null) spannable.setSpan(what, start, end, flags);
    }
    @Override
    public void removeSpan(Object what) {
        if (spannable != null) spannable.removeSpan(what);
    }
    @Override
    public <T> T[] getSpans(int start, int end, Class<T> type) {
        if (spannable != null) return spannable.getSpans(start, end, type);
        return null;
    }
    @Override
    public int getSpanStart(Object tag) {
        return spannable == null ? 0 : spannable.getSpanStart(tag);
    }
    @Override
    public int getSpanEnd(Object tag) {
        return spannable == null ? 0 : spannable.getSpanStart(tag);
    }
    @Override
    public int getSpanFlags(Object tag) {
        return spannable == null ? 0 : spannable.getSpanStart(tag);
    }
    @Override
    public int nextSpanTransition(int start, int limit, Class type) {
        return spannable == null ? 0 : spannable.nextSpanTransition(start, limit, type);
    }
    @Override
    public int length() {
        return spannable == null ? 0 : spannable.length();
    }
    @Override
    public char charAt(int index) {
        return spannable == null ? 0 : spannable.charAt(index);
    }
    @NonNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return spannable == null ? "" : spannable.subSequence(start, end);
    }
    //endregion

    @NonNull
    @Override
    public String toString() {
        if (getSpannable() == null) {
            if (getSpanned() == null) return ObjectUtils.toSimpleString(this) + " Spanned is null";
            else return getSpanned().toString();
        } else return getSpannable().toString();
    }

    protected static class Region {

        private static class Formatter extends Region {

            private ArgPath path;

            private int code;
            private char type;

            public Formatter(Spanned spanned, Annotation annotation, char type, int code) {
                super(spanned, annotation);
                this.type = type;
                this.code = code;
            }

            public boolean equals(char type, int code) {
                return this.type == type && this.code == code;
            }

            public ArgPath getPath() {
                return path;
            }
            public void setPath(ArgPath path) {
                this.path = path;
            }

            public int getCode() {
                return code;
            }
            public void setCode(int code) {
                this.code = code;
            }
            public char getType() {
                return type;
            }

            public void setType(char type) {
                this.type = type;
            }

            @NonNull
            @Override
            public String toString() {
                return super.toString() + " code: " + code  + " type: " + type;
            }
        }

        private Annotation annotation;
        private int start;
        private int end;
        private String key;
        private String value;

        public Region(Spanned spanned, Annotation annotation) {
            this.annotation = annotation;
            this.start = spanned.getSpanStart(annotation);
            this.end = spanned.getSpanEnd(annotation);
            this.key = annotation.getKey();
            this.value = annotation.getValue();
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public int range() {
            return end - start;
        }

        public boolean isTypeFormat() {
            return key.equals(ANNOTATION_KEY_FORMAT);
        }

        public CharSequence subSequence(CharSequence sequence) {
            return sequence.subSequence(start, end);
        }

        @NonNull
        @Override
        public String toString() {
            return ObjectUtils.toSimpleString(this) + " [ " + start  + " ... " + end  + "] key: {" + key + "} value: {" + value + "} ";
        }
    }

    public static class ArgPath {

        private Object arg;
        private Object[] what;

        public ArgPath(Object arg, Object... what) {
            this.arg = arg;
            this.what = what;
        }

        public void setArg(Object arg) {
            this.arg = arg;
        }
        public void setWhat(Object[] what) {
            this.what = what;
        }

        public Object getArg() {
            return arg;
        }
        public Object[] getWhats() {
            return what;
        }

    }

}
