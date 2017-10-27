package view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import activity.NotePageActivity;

import static android.graphics.Color.BLACK;

/**
 * @author huangwei
 * @功能 图文混排TextView，请使用{@link #setMText(CharSequence)}
 * @2017年5月19日16:59:57
 */
public class MtextViewNote extends TextView {
    /**
     * 缓存测量过的数据
     */
    private static HashMap<String, SoftReference<MeasuredData>> measuredData = new HashMap<String, SoftReference<MeasuredData>>();
    private static int hashIndex = 0;
    /**
     * 存储当前文本内容，每个item为一行
     */
    ArrayList<LINE> contentList = new ArrayList<LINE>();
    private Context context;
    /**
     * 用于测量字符宽度
     */
    private TextPaint paint = new TextPaint();

    /**
     * 用于测量上下标字符宽度
     */
    private TextPaint upDownPaint = new TextPaint();

    /**
     * 用于测量span高度
     */
    private Paint.FontMetricsInt mSpanFmInt = new Paint.FontMetricsInt();
    /**
     * 临时使用,以免在onDraw中反复生产新对象
     */
    private FontMetrics mFontMetrics = new FontMetrics();

    //	private float lineSpacingMult = 0.5f;
    private int textColor = Color.BLACK;
    //行距
    private float lineSpacing;
    private int lineSpacingDP = 5;
    /**
     * 段间距,-1为默认
     */
    private int paragraphSpacing = -1;
    /**
     * 最大宽度
     */
    private int maxWidth;
    /**
     * 只有一行时的宽度
     */
    private int oneLineWidth = -1;
    /**
     * 已绘的行中最宽的一行的宽度
     */
    private float lineWidthMax = -1;
    /**
     * 存储当前文本内容,每个item为一个字符或者一个SpanObject
     */
    private ArrayList<Object> obList = new ArrayList<Object>();
    /**
     * 是否使用默认{@link #onMeasure(int, int)}和{@link #onDraw(Canvas)}
     */
    private boolean useDefault = false;
    protected CharSequence text = "";

    private int minHeight;
    /**
     * 用以获取屏幕高宽
     */
    private DisplayMetrics displayMetrics;
    /**
     * {@link BackgroundColorSpan}用
     */
    private Paint textBgColorPaint = new Paint();
    /**
     * {@link BackgroundColorSpan}用
     */
    private Rect textBgColorRect = new Rect();
    private List<EditText> mEdtText;
    private int edtTextMun = 0;
    private List<RelativeLayout.LayoutParams> mEdtTextParams;

    private SpannableStringBuilder mStringBuilder = new SpannableStringBuilder("");
    private int mViewWidth;
    private List<ImageView> mImageViews;
    private List<RelativeLayout.LayoutParams> mImageViewParams;

    private int currentIndex = 0;
    private List<View> mViews;
    private ArrayList<RelativeLayout.LayoutParams> mViewParams;
    private float mHeighRatio;

    public MtextViewNote(Context context) {
        super(context);
        init(context);
    }

    public MtextViewNote(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MtextViewNote(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    public void init(Context context) {
        this.context = context;
        paint.setAntiAlias(true);
        upDownPaint.setAntiAlias(true);
        lineSpacing = dip2px(context, lineSpacingDP);
        minHeight = dip2px(context, 30);

        displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mHeighRatio = 0f;
        if (displayMetrics.heightPixels > displayMetrics.widthPixels) {
            float ratio = (displayMetrics.heightPixels / 800f);
            if (ratio > 1.1) {
                mHeighRatio = 5 * ratio;
            }
        }
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void setMaxWidth(int maxpixels) {
        super.setMaxWidth(maxpixels);
        maxWidth = maxpixels;
    }

    @Override
    public void setMinHeight(int minHeight) {
        super.setMinHeight(minHeight);
        this.minHeight = minHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (useDefault) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = 0, height = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                width = displayMetrics.widthPixels;
                break;
            default:
                break;
        }
        if (maxWidth > 0) {
            width = Math.min(width, maxWidth);
        }

        paint.setTextSize(this.getTextSize());
        paint.setColor(textColor);
        upDownPaint.setTextSize(this.getTextSize() / 2);
        upDownPaint.setColor(textColor);
        int realHeight = measureContentHeight( width);

        //如果实际行宽少于预定的宽度，减少行宽以使其内容横向居中
        int leftPadding = getCompoundPaddingLeft();
        int rightPadding = getCompoundPaddingRight();
        width = Math.min(width, (int) lineWidthMax + leftPadding + rightPadding);

        if (oneLineWidth > -1) {
            width = oneLineWidth;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = realHeight;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = realHeight;
                break;
            default:
                break;
        }

        height += getCompoundPaddingTop() + getCompoundPaddingBottom();

        //控件的高度
        height =  (Math.max(height, minHeight));
        //        height = (int) (Math.max(height, minHeight) + contentList.get(0).height);

        mViewWidth = width;
        setMeasuredDimension(width, height);
    }

    private float getTextHeight(float height,LINE aContentList){
        return (height + (aContentList.height + Math.abs(paint.getFontMetrics().ascent)-paint.getFontMetrics().descent) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (useDefault) {
            super.onDraw(canvas);
            return;
        }
        if (contentList.isEmpty()) {
            return;
        }
        int width;

        Object ob;

        int leftPadding = getCompoundPaddingLeft();
        int topPadding = getCompoundPaddingTop();

        float height = 0 + topPadding + lineSpacing;
        //只有一行时
        if (oneLineWidth != -1) {
            height = getMeasuredHeight() / 2 - contentList.get(0).height / 2;
        }

        for (LINE aContentList : contentList) {
            //绘制一行
            float realDrawedWidth = leftPadding;
            /** 是否换新段落*/
            boolean newParagraph = false;
            for (int j = 0; j < aContentList.line.size(); j++) {
                ob = aContentList.line.get(j);
                width = aContentList.widthList.get(j);
                paint.getFontMetrics(mFontMetrics);
                float x = realDrawedWidth;
                float y = height + aContentList.height - paint.getFontMetrics().descent;
                float top = y - aContentList.height;
                float bottom = y + mFontMetrics.descent;
                if (ob instanceof String) {

                    //总高度大于文字高度,有图片在本行,文字居中
                    if (aContentList.height > (paint.getFontMetrics().descent * 2 + Math.abs(paint.getFontMetrics().ascent))) {
                        float textX =  getTextHeight(height,aContentList);
                        canvas.drawText((String) ob, realDrawedWidth, textX, paint);
                    } else {
                        canvas.drawText((String) ob, realDrawedWidth, y, paint);
                    }

                    realDrawedWidth += width;
                    //结尾时换行符/n 则换行
                    if (((String) ob).endsWith("\n") && j == aContentList.line.size() - 1) {
                        newParagraph = true;
                    }
                } else if (ob instanceof SpanObject) { //内容是标签
                    Object span = ((SpanObject) ob).span;
                    //                    if (span instanceof DynamicDrawableSpan) {

                    if (span instanceof ImageSpan) { //是图片
                        String source = ((ImageSpan) span).getSource();
                        int start = ((Spannable) text).getSpanStart(span);
                        int end = ((Spannable) text).getSpanEnd(span);
                        int picHeight = ((ImageSpan) span).getDrawable().getBounds().height();

                        if (aContentList.height > picHeight * 1.5) {
//                            float textY = (height + ((aContentList.height + Math.abs(paint.getFontMetrics().ascent)) + picHeight / 2) / 2);
                            float textY =getTextHeight(height,aContentList)+picHeight/3;
                            float textTop = textY - aContentList.height;
                            float textBottom = textY + mFontMetrics.descent;
                            ((DynamicDrawableSpan) span).draw(canvas, text, start, end, (int) x, (int) textTop, (int) textY, (int) textBottom, paint);
                        } else {
                            ((DynamicDrawableSpan) span).draw(canvas, text, start, end, (int) x, (int) top, (int) y, (int) bottom, paint);
                            //                            Log.e("onTouchEvent", "start = " + start + "    end = " + end + "   getx = " + getX() + "   gety = " + getY());
                            //                            Log.e("onTouchEvent", "x = " + x + "    top = " + top + "    y = " + y + "    bottom = " + bottom);
                            //                            Log.e("onTouchEvent", "with = " + ((ImageSpan) span).getDrawable().getBounds().width()
                            //                                    + "    height = " + ((ImageSpan) span).getDrawable().getBounds().height());

                            /**
                             * 图片的X轴=x , y轴=top
                             * 加上宽高等于范围
                             * 数据存起来,点击可以放大图片
                             * */

                        }

                        realDrawedWidth += width;
                        if (source.contains("input_")) {  //如果是填空标签则edttext 定义位置
                            if (mViews != null && mViews.size() > 0) {
                                String mun = source.substring(source.lastIndexOf("input_"), source.lastIndexOf("_")).replaceAll("input_", "");
                                EditText editText = (EditText) mViews.get(Integer.valueOf(mun) - 1);
                                int imgWidth = ((ImageSpan) span).getDrawable().getBounds().width();
                                int imgHeight = ((ImageSpan) span).getDrawable().getBounds().height();
                                editText.setWidth(imgWidth);
                                RelativeLayout.LayoutParams params = mViewParams.get(Integer.valueOf(mun) - 1);

                                if (aContentList.height > imgHeight * 1.5 ) {
                                    float textY =  getTextHeight(height,aContentList);
                                    float textTop = textY - imgHeight;
                                    params.setMargins((int) (x), (int) textTop, 0, 0);

                                } else {
                                    if (aContentList == contentList.get(0)) {
                                        params.setMargins((int) (x), (int) top + imgHeight / 4,  0,  0);
                                    } else {
                                        params.setMargins((int) (x), (int) top,  0,  0);
                                    }
                                }

                                editText.setLayoutParams(params);
                            }
                        } else if (source.contains("note_")) {
                            if (mViews != null && mViews.size() > 0) {
                                String mun = source.substring(source.lastIndexOf("note_"), source.lastIndexOf("_")).replaceAll("note_", "");
                                ImageView imageView = (ImageView) mViews.get(Integer.valueOf(mun) - 1);
                                imageView.setMaxWidth(((ImageSpan) span).getDrawable().getBounds().width());
                                int imgWidth = ((ImageSpan) span).getDrawable().getBounds().width();
                                int imgHeight = ((ImageSpan) span).getDrawable().getBounds().height();
                                RelativeLayout.LayoutParams params = mViewParams.get(Integer.valueOf(mun) - 1);

                                if (aContentList.height > imgHeight * 1.5 ) {
                                    float textY =  getTextHeight(height,aContentList);
                                    float textTop = textY - imgHeight;
                                    params.setMargins((int) (x), (int) textTop,  0,  0);
                                } else {
                                    if (aContentList == contentList.get(0)) {
                                        params.setMargins((int) (x), (int) top + imgHeight / 4,  0,  0);
                                    } else {
                                        params.setMargins((int) (x), (int) top,  0,  0);
                                    }
                                }

                                imageView.setLayoutParams(params);
                            }
                        }

                    } else if (span instanceof BackgroundColorSpan) { //如果标签是背景色
                        textBgColorPaint.setColor(((BackgroundColorSpan) span).getBackgroundColor());
                        textBgColorPaint.setStyle(Style.FILL);
                        textBgColorRect.left = (int) realDrawedWidth;
                        int textHeight = (int) getTextSize();
                        textBgColorRect.top = (int) (height + aContentList.height - textHeight - mFontMetrics.descent);
                        textBgColorRect.right = textBgColorRect.left + width;
                        textBgColorRect.bottom = (int) (height + aContentList.height + lineSpacing - mFontMetrics.descent);
                        canvas.drawRect(textBgColorRect, textBgColorPaint);
                        canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth, height + aContentList.height - mFontMetrics.descent, paint);
                        realDrawedWidth += width;
                    } else if (span instanceof SubscriptSpan) { //如果标签是下标
                        if (aContentList.height > (paint.getFontMetrics().descent * 2 + Math.abs(paint.getFontMetrics().ascent))) {
                            float textX = getTextHeight(height,aContentList);
                            canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth, textX, upDownPaint);
                        } else {
                            canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth,
                                    height + aContentList.height - mFontMetrics.descent, upDownPaint);
                        }

                        realDrawedWidth += width / 2;
                    } else if (span instanceof SuperscriptSpan) { //如果图片是上标
                        if (aContentList.height > (paint.getFontMetrics().descent * 2 + Math.abs(paint.getFontMetrics().ascent))) {
                            float textX = getTextHeight(height,aContentList)
                                    - mFontMetrics.descent;
                            canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth, textX, upDownPaint);
                        } else {
                            canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth,
                                    height + aContentList.height - mFontMetrics.descent - (aContentList.height - mFontMetrics.descent) / 2, upDownPaint);
                        }


                        realDrawedWidth += width / 2;
                    } else//做字符串处理
                    {
                        canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth, height + aContentList.height - mFontMetrics.descent, paint);
                        realDrawedWidth += width;
                    }
                }

            }
            //如果要绘制段间距
            if (newParagraph) {
                height += aContentList.height + paragraphSpacing + getTextSize() / 2;
            } else {
                height += aContentList.height + lineSpacing + getTextSize() / 2;
            }
        }

    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        textColor = color;
    }


    /**
     * 用于带ImageSpan的文本内容所占高度测量
     *
     * @param width 预定的宽度
     * @return 所需的高度
     */
    private int measureContentHeight(int width) {
        int cachedHeight = getCachedData(text.toString(), width);

        if (cachedHeight > 0) {
            return cachedHeight;
        }

        // 已绘的宽度
        float obWidth = 0;
        float obHeight = 0;

        float textSize = this.getTextSize();
        FontMetrics fontMetrics = paint.getFontMetrics();
        //行高
        float lineHeight = fontMetrics.bottom - fontMetrics.top;
        //计算出的所需高度
        float height = lineSpacing;

        int leftPadding = getCompoundPaddingLeft();
        int rightPadding = getCompoundPaddingRight();

        float drawedWidth = 0;

        boolean splitFlag = false;//BackgroundColorSpan拆分用

        width = width - leftPadding - rightPadding;

        oneLineWidth = -1;

        contentList.clear();

        StringBuilder sb;

        LINE line = new LINE();

        for (int i = 0; i < obList.size(); i++) {
            Object ob = obList.get(i);

            if (ob instanceof String) {
                obWidth = paint.measureText((String) ob);
                obHeight = textSize;
                if ("\n".equals(ob)) { //遇到"\n"则换行
                    obWidth = width - drawedWidth;
                }
            } else if (ob instanceof SpanObject) {
                Object span = ((SpanObject) ob).span;
                if (span instanceof DynamicDrawableSpan) {
                    int start = ((Spannable) text).getSpanStart(span);
                    int end = ((Spannable) text).getSpanEnd(span);
                    obWidth = ((DynamicDrawableSpan) span).getSize(getPaint(), text, start, end, mSpanFmInt);
                    obHeight = Math.abs(mSpanFmInt.top) + Math.abs(mSpanFmInt.bottom);
                    if (obHeight > lineHeight) {
                        lineHeight = obHeight;
                    }

                } else if (span instanceof BackgroundColorSpan) {
                    String str = ((SpanObject) ob).source.toString();
                    obWidth = paint.measureText(str);
                    obHeight = textSize;

                    //如果太长,拆分
                    int k = str.length() - 1;
                    while (width - drawedWidth < obWidth) {
                        obWidth = paint.measureText(str.substring(0, k--));
                    }
                    if (k < str.length() - 1) {
                        splitFlag = true;
                        SpanObject so1 = new SpanObject();
                        so1.start = ((SpanObject) ob).start;
                        so1.end = so1.start + k;
                        so1.source = str.substring(0, k + 1);
                        so1.span = ((SpanObject) ob).span;

                        SpanObject so2 = new SpanObject();
                        so2.start = so1.end;
                        so2.end = ((SpanObject) ob).end;
                        so2.source = str.substring(k + 1, str.length());
                        so2.span = ((SpanObject) ob).span;

                        ob = so1;
                        obList.set(i, so2);
                        i--;
                    }
                }//做字符串处理
                else {
                    String str = ((SpanObject) ob).source.toString();
                    obWidth = paint.measureText(str);
                    obHeight = textSize;
                }
            }

            //这一行满了，存入contentList,新起一行
            if (width - drawedWidth < obWidth || splitFlag) {
                splitFlag = false;
                contentList.add(line);

                if (drawedWidth > lineWidthMax) {
                    lineWidthMax = drawedWidth;
                }
                drawedWidth = 0;
                //判断是否有分段
                int objNum = line.line.size();
                if (paragraphSpacing > 0
                        && objNum > 0
                        && line.line.get(objNum - 1) instanceof String
                        && "\n".equals(line.line.get(objNum - 1))) {
                    height += line.height + paragraphSpacing + getTextSize() / 2;
                } else {
                    height += line.height + lineSpacing + getTextSize() / 2;
                }

                lineHeight = obHeight;

                line = new LINE();
            }

            drawedWidth += obWidth;

            if (ob instanceof String && line.line.size() > 0 && (line.line.get(line.line.size() - 1) instanceof String)) {
                int size = line.line.size();
                sb = new StringBuilder();
                sb.append(line.line.get(size - 1));
                sb.append(ob);
                ob = sb.toString();
                obWidth = obWidth + line.widthList.get(size - 1);
                line.line.set(size - 1, ob);
                line.widthList.set(size - 1, (int) obWidth);
                line.height = (int) lineHeight;
            } else {
                line.line.add(ob);
                line.widthList.add((int) obWidth);
                line.height = (int) lineHeight;
            }

        }

        if (drawedWidth > lineWidthMax) {
            lineWidthMax = drawedWidth;
        }

        if (line != null && line.line.size() > 0) {
            contentList.add(line);
            height += lineHeight + lineSpacing;
        }
        if (contentList.size() <= 1) {
            oneLineWidth = (int) drawedWidth + leftPadding + rightPadding;
            height = lineSpacing + lineHeight + lineSpacing;
        }

        cacheData(width, (int) height);
        return (int) height;
    }

    /**
     * 获取缓存的测量数据，避免多次重复测量
     *
     * @param text
     * @param width
     * @return height
     */
    @SuppressWarnings("unchecked")
    private int getCachedData(String text, int width) {
        SoftReference<MeasuredData> cache = measuredData.get(text);
        if (cache == null) {
            return -1;
        }
        MeasuredData md = cache.get();
        if (md != null && md.textSize == this.getTextSize() && width == md.width) {
            lineWidthMax = md.lineWidthMax;
            contentList = (ArrayList<LINE>) md.contentList.clone();
            oneLineWidth = md.oneLineWidth;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < contentList.size(); i++) {
                LINE line = contentList.get(i);
                sb.append(line.toString());
            }
            return md.measuredHeight;
        } else {
            return -1;
        }
    }

    /**
     * 缓存已测量的数据
     *
     * @param width
     * @param height
     */
    @SuppressWarnings("unchecked")
    private void cacheData(int width, int height) {
        MeasuredData md = new MeasuredData();
        md.contentList = (ArrayList<LINE>) contentList.clone();
        md.textSize = this.getTextSize();
        md.lineWidthMax = lineWidthMax;
        md.oneLineWidth = oneLineWidth;
        md.measuredHeight = height;
        md.width = width;
        md.hashIndex = ++hashIndex;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contentList.size(); i++) {
            LINE line = contentList.get(i);
            sb.append(line.toString());
        }

        SoftReference<MeasuredData> cache = new SoftReference<MeasuredData>(md);
        measuredData.put(text.toString(), cache);
    }

    /**
     * 用本函数代替{@link #setText(CharSequence)}
     *
     * @param cs
     */
    public void setMText(CharSequence cs) {
        text = cs;

        obList.clear();

        ArrayList<SpanObject> isList = new ArrayList<SpanObject>();
        useDefault = false;

        if (cs instanceof Spannable) {
            CharacterStyle[] spans = ((Spannable) cs).getSpans(0, cs.length(), CharacterStyle.class);
            for (int i = 0; i < spans.length; i++) {
                int s = ((Spannable) cs).getSpanStart(spans[i]);
                int e = ((Spannable) cs).getSpanEnd(spans[i]);
                SpanObject iS = new SpanObject();
                iS.span = spans[i];
                iS.start = s;
                iS.end = e;
                iS.source = cs.subSequence(s, e);
                isList.add(iS);
            }
        }

        //对span进行排序，以免不同种类的span位置错乱
        SpanObject[] spanArray = new SpanObject[isList.size()];
        isList.toArray(spanArray);
        Arrays.sort(spanArray, 0, spanArray.length, new SpanObjectComparator());
        isList.clear();
        for (int i = 0; i < spanArray.length; i++) {
            isList.add(spanArray[i]);
        }

        String str = cs.toString();

        for (int i = 0, j = 0; i < cs.length(); ) {
            if (j < isList.size()) {
                SpanObject is = isList.get(j);
                if (i < is.start) {
                    Integer cp = str.codePointAt(i);
                    //支持增补字符
                    if (Character.isSupplementaryCodePoint(cp)) {
                        i += 2;
                    } else {
                        i++;
                    }

                    obList.add(new String(Character.toChars(cp)));

                } else if (i >= is.start) {
                    obList.add(is);
                    j++;
                    i = is.end;
                }
            } else {
                Integer cp = str.codePointAt(i);
                if (Character.isSupplementaryCodePoint(cp)) {
                    i += 2;
                } else {
                    i++;
                }

                obList.add(new String(Character.toChars(cp)));
            }
        }

        requestLayout();
    }

    public void setUseDefault(boolean useDefault) {
        this.useDefault = useDefault;
        if (useDefault) {
            this.setText(text);
            this.setTextColor(textColor);
        }
    }

    /**
     * 设置行距
     *
     * @param lineSpacingDP 行距，单位dp
     */
    public void setLineSpacingDP(int lineSpacingDP) {
        this.lineSpacingDP = lineSpacingDP;
        lineSpacing = dip2px(context, lineSpacingDP);
    }

    public void setParagraphSpacingDP(int paragraphSpacingDP) {
        paragraphSpacing = dip2px(context, paragraphSpacingDP);
    }

    /**
     * 获取行距
     *
     * @return 行距，单位dp
     */
    public int getLineSpacingDP() {
        return lineSpacingDP;
    }

    public void setEdtText(List<EditText> edtText) {
        mEdtText = edtText;
        mEdtTextParams = new ArrayList<>();
        if (edtText.size() > 0) {
            for (EditText editText : edtText) {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.MarginLayoutParams(edtText.get(0).getLayoutParams());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(layoutParams);
                mEdtTextParams.add(params);
            }
        }
    }

    public void setImageView(List<ImageView> imageView) {
        mImageViews = imageView;
        mImageViewParams = new ArrayList<>();
        if (imageView.size() > 0) {
            for (ImageView imageViewa : imageView) {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.MarginLayoutParams(imageView.get(0).getLayoutParams());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(layoutParams);
                mImageViewParams.add(params);
            }
        }
    }

    public void setView(List<View> Views) {
        mViews = Views;
        mViewParams = new ArrayList<>();
        if (Views.size() > 0) {
            for (View View : Views) {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.MarginLayoutParams(View.getLayoutParams());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(layoutParams);
                mViewParams.add(params);
            }
        }
    }

    /**
     * @author huangwei
     * @功能: 存储Span对象及相关信息
     * @2014年5月27日
     * @下午5:21:37
     */
    class SpanObject {
        public Object span;
        public int start;
        public int end;
        public CharSequence source;
    }

    /**
     * @author huangwei
     * @功能: 对SpanObject进行排序
     * @2017年5月19日16:59:06
     */
    class SpanObjectComparator implements Comparator<SpanObject> {
        @Override
        public int compare(SpanObject lhs, SpanObject rhs) {

            return lhs.start - rhs.start;
        }
    }

    /**
     * @author huangwei
     * @功能: 存储测量好的一行数据
     * @2017年5月19日16:59:23
     */
    class LINE {
        public ArrayList<Object> line = new ArrayList<Object>();
        public ArrayList<Integer> widthList = new ArrayList<Integer>();
        public float height;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("height:" + height + "   ");
            for (int i = 0; i < line.size(); i++) {
                sb.append(line.get(i) + ":" + widthList.get(i));
            }
            return sb.toString();
        }
    }

    /**
     * @author huangwei
     * @功能: 缓存的数据
     * @2017年5月19日16:59:31
     */
    class MeasuredData {
        public int measuredHeight;
        public float textSize;
        public int width;
        public float lineWidthMax;
        public int oneLineWidth;
        public int hashIndex;
        ArrayList<LINE> contentList;
    }

    public static class TuyaViewPage extends View {

        private final String TAG = "TuyaView";
        private Context context;
        private Bitmap mBitmap;
        private Bitmap mInBitmap;
        private Bitmap mSaveBitmap;
        private Canvas mCanvas;
        private Canvas mSaveCanvas;
        private Path mPath;
        private Paint mBitmapPaint;// 画布的画笔
        public Paint mPaint;// 真实的画笔
        private float mX, mY;// 临时点坐标
        private static final float TOUCH_TOLERANCE = 2;
        // 保存Path路径的集合,用List集合来模拟栈
        private static List<DrawPath> savePath = new ArrayList<>();
        // 保存已删除Path路径的集合
        private static List<DrawPath> deletePath = new ArrayList<>();
        // 记录Path路径的对象
        private DrawPath dp;
        private int screenWidth, screenHeight;
        public int currentColor = BLACK;
        public int backgroundColor = Color.WHITE;
        private int currentSize = 10;
        public int currentStyle = 1;
        private boolean isRedo = false;

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();
        ArrayList<Path> mPaths = new ArrayList<Path>();
        private NotePageActivity mNoteActivity;
        private RelativeLayout.LayoutParams mParams;
        private InputMethodManager mImm;


        public TuyaViewPage(Context context, Bitmap bitmapc, int w, int h) {
            super(context);
            this.context = context;
            screenWidth = w;
            screenHeight = h;
            mInBitmap = bitmapc;
            initCanvas();

            setBackground(new BitmapDrawable(bitmapc));

            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中

            mSaveBitmap = Bitmap.createBitmap(bitmapc).copy(Bitmap.Config.ARGB_8888, true);
            mSaveCanvas = new Canvas(mSaveBitmap);
        }


        private class DrawPath {
            public Path path;// 路径
            public Paint paint;// 画笔
        }

        public TuyaViewPage(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.context = context;
            initDraftCanvas();
        }

        public void setInBitmap(Bitmap bitmap) {
            mInBitmap = bitmap;
        }


        public TuyaViewPage(Context context, int w, int h) {
            super(context);
            this.context = context;
            screenWidth = w;
            screenHeight = h;
            initCanvas();
            setBackgroundColor(backgroundColor);
            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
            mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中

            mSaveBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            mSaveCanvas = new Canvas(mSaveBitmap);
            mSaveCanvas.drawColor(backgroundColor);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            screenWidth = MeasureSpec.getSize(widthMeasureSpec);
            screenHeight = MeasureSpec.getSize(heightMeasureSpec);
            if (mBitmap == null && mInBitmap != null) {
                setBackground(new BitmapDrawable(mInBitmap));
                mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
                mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
                mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
                mSaveBitmap = Bitmap.createBitmap(mInBitmap).copy(Bitmap.Config.ARGB_8888, true);
                mSaveCanvas = new Canvas(mSaveBitmap);
            } else if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
                mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
                mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
            }
        }

        private void initDraftCanvas() {
            mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            setPaintStyle();
            mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        }

        public void initCanvas() {
            initDraftCanvas();
            mNoteActivity = (NotePageActivity) context;
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    new ViewGroup.MarginLayoutParams(mNoteActivity.mNoteWriteIvEraser.getLayoutParams());
            mParams = new RelativeLayout.LayoutParams(marginLayoutParams);
        }

        //初始化画笔样式
        private void setPaintStyle() {
            mPaint = new Paint();
            mPaint.setStyle(Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
            mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setFilterBitmap(true);
            if (currentStyle == 1) {
                mPaint.setStrokeWidth(currentSize);
                mPaint.setColor(currentColor);
            } else {//橡皮擦
                //            mPaint.setAlpha(0);
                //            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                mPaint.setColor(backgroundColor);
                mPaint.setStrokeWidth(80);
            }


            //        // 设置光源的方向
            //        float[] direction = new float[]{1, 1, 1};
            //        //设置环境光亮度
            //        float light = 0.4f;
            //        // 选择要应用的反射等级
            //        float specular = 6;
            //        // 向mask应用一定级别的模糊
            //        float blur = 4f;
            //
            //
            //        EmbossMaskFilter maskFilter = new EmbossMaskFilter(direction,
            //                light, specular, blur);
            //        mPaint.setMaskFilter(maskFilter);

        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // 将前面已经画过得显示出来
            canvas.drawBitmap(mBitmap, 0, 0, null);
            // 实时的显示
            if (mPath != null) {
                canvas.drawPath(mPath, mPaint);
            }
        }

        private void touch_start(float x, float y) {
            mPath.moveTo(x, y);
            mPath.lineTo(x + 1, y + 1);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {

            float endX = (mX + x) / 2;
            float endY = (mY + y) / 2;
            mPath.quadTo(mX, mY, endX, endY);
            //        mCanvas.drawLine(mX,mY,x,y,mPaint);
            mX = x;
            mY = y;
        }

        private void touch_up(float x, float y) {
            float endX = (mX + x) / 2;
            float endY = (mY + y) / 2;
            mPath.quadTo(mX, mY, endX, endY);
            mCanvas.drawPath(mPath, mPaint);
            //将一条完整的路径保存下来(相当于入栈操作)
            savePath.add(dp);
            Log.d("TuyaView", dp.paint + "   " + dp.path);
            mPath = null;// 重新置空
        }

        /**
         * 撤销
         * 撤销的核心思想就是将画布清空，
         * 将保存下来的Path路径最后一个移除掉，
         * 重新将路径画在画布上面。
         */
        public void undo() {
            if (savePath != null && savePath.size() > 0) {
                DrawPath drawPath = savePath.get(savePath.size() - 1);
                deletePath.add(drawPath);
                savePath.remove(savePath.size() - 1);
                if (mInBitmap != null && !isRedo) {
                    setPaintStyle();

                    mBitmap = Bitmap.createBitmap(mInBitmap).copy(Bitmap.Config.ARGB_8888, true);
                    mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
                    mSaveBitmap = Bitmap.createBitmap(mInBitmap).copy(Bitmap.Config.ARGB_8888, true);
                    mSaveCanvas = new Canvas(mSaveBitmap);

                    Iterator<DrawPath> iter = savePath.iterator();
                    while (iter.hasNext()) {
                        DrawPath drawPath1 = iter.next();
                        mCanvas.drawPath(drawPath1.path, drawPath1.paint);
                        mSaveCanvas.drawPath(drawPath1.path, drawPath1.paint);
                    }
                } else {
                    redrawOnBitmap();
                }
                invalidate();// 刷新
            }
        }

        /**
         * 重做
         */
        public void redo() {
            isRedo = true;
            if (savePath != null && savePath.size() > 0) {
                savePath.clear();
                redrawOnBitmap();
            }

            if (mInBitmap != null) {
                redrawOnBitmap();
            }
        }

        private void redrawOnBitmap() {
            setPaintStyle();
            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
            mSaveBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            mSaveCanvas = new Canvas(mSaveBitmap);
            mSaveCanvas.drawColor(backgroundColor);

            Iterator<DrawPath> iter = savePath.iterator();
            while (iter.hasNext()) {
                DrawPath drawPath = iter.next();
                mCanvas.drawPath(drawPath.path, drawPath.paint);
                mSaveCanvas.drawPath(drawPath.path, drawPath.paint);
            }
            invalidate();// 刷新
        }

        /**
         * 恢复，恢复的核心就是将删除的那条路径重新添加到savapath中重新绘画即可
         */
        public void recover() {
            if (deletePath.size() > 0) {
                //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
                DrawPath dp = deletePath.get(deletePath.size() - 1);
                savePath.add(dp);
                //将取出的路径重绘在画布上
                mCanvas.drawPath(dp.path, dp.paint);
                //将该路径从删除的路径列表中去除
                deletePath.remove(deletePath.size() - 1);
                invalidate();
            }
        }

        private int mode = 0;
        private float oldDist;
        private boolean isMulti = false;
        private boolean isWrite = false;

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();
            int historySize = event.getHistorySize();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mode = 1;
                    resetDirtyRect(x, y);
                    // 每次down下去重新new一个Path
                    mPath = new Path();
                    //每一次记录的路径对象是不一样的
                    dp = new DrawPath();
                    dp.path = mPath;
                    dp.paint = mPaint;
                    mPath.moveTo(x, y);
                    mX = x;
                    mY = y;
                    initEraser(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:

                    if (mImm != null) {
                        mImm.hideSoftInputFromWindow(mNoteActivity.getWindow().getDecorView().getWindowToken(),
                                0);
                    }

                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                    }

                    if (mode >= 2) {
                        Log.i(TAG, "mode = " + mode);
                    } else {
                        if (!isMulti) {
                            Log.i(TAG, "mode = " + mode);
                            if (Math.abs(x - mX) > 4 || Math.abs(y - mY) > 4) {
                                float endX = (mX + x) / 2;
                                float endY = (mY + y) / 2;
                                mPath.quadTo(mX, mY, endX, endY);
                                isWrite = true;
                                initEraser(x, y);
                            }
                        }
                    }

                    mX = x;
                    mY = y;

                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "ACTION_UP = " + MotionEvent.ACTION_UP);
                    mode = 0;
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                    }

                    if (!isMulti) {
                        float endX2 = (mX + x) / 2;
                        float endY2 = (mY + y) / 2;
                        mPath.quadTo(mX, mY, endX2, endY2);
                        mCanvas.drawPath(mPath, mPaint);
                        mSaveCanvas.drawPath(mPath, mPaint);
                        savePath.add(dp);
                    }
                    mPath = null;// 重新置空
                    isMulti = false;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.i(TAG, "ACTION_POINTER_UP = " + MotionEvent.ACTION_POINTER_UP);
                    mode -= 1;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.i(TAG, "ACTION_POINTER_DOWN = " + MotionEvent.ACTION_POINTER_DOWN);
                    if (isWrite) {
                        float endX2 = (mX + x) / 2;
                        float endY2 = (mY + y) / 2;
                        mPath.quadTo(mX, mY, endX2, endY2);
                        mCanvas.drawPath(mPath, mPaint);
                        mSaveCanvas.drawPath(mPath, mPaint);
                        savePath.add(dp);
                        isWrite = false;
                    }
                    isMulti = true;
                    oldDist = spacing(event);
                    mode += 1;
                    break;
            }

            invalidate((int) (dirtyRect.left - currentSize),
                    (int) (dirtyRect.top - currentSize),
                    (int) (dirtyRect.right - currentSize),
                    (int) (dirtyRect.bottom - currentSize));


            lastTouchX = x;
            lastTouchY = y;

            return true;
        }

        private void initEraser(float x, float y) {

            if (currentStyle == 0) {
                int x1 = (int) x;
                int y1 = (int) y;
                int width = mNoteActivity.mNoteWriteIvEraser.getWidth() / 2;
                int height = mNoteActivity.mNoteWriteIvEraser.getHeight() / 2;
                mParams.setMargins(x1 - width, y1 - height, width - x1 + width,
                        height - y1 + height);
                mNoteActivity.mNoteWriteIvEraser.setLayoutParams(mParams);
            }

        }

        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }
            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        /**
         * Resets the dirty region when the motion event occurs.
         */
        private void resetDirtyRect(float eventX, float eventY) {
            // The lastTouchX and lastTouchY were set when the ACTION_DOWN
            // motion event occurred.
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

        //保存到sd卡
        public String saveToSDCard(String time, String content) {

            //        for (int i = 0; i < savePath.size(); i++) {
            //            mSaveCanvas.drawPath(savePath.get(i).path, savePath.get(i).paint);
            //        }

            Log.d("TuyaView", "savePath.size() = " + savePath.size());
            String sdPath = Environment.getExternalStorageDirectory() + "/ScrawlNote";

            //获得系统当前时间，并以该时间作为文件名
            File f = new File(sdPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            Log.d("TuyaView", "文件 = " + f.exists());

            String path = sdPath + "/" + content + "__" + time + ".png";
            File file = new File(path);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TuyaView", "e = " + e);
            }
            Log.d("TuyaView", "fos = " + fos + " path = " + path);
            mSaveBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            return file.getPath();
        }

        public String getDiskCacheDir(Context context) {
            String cachePath = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalCacheDir().getPath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
            return cachePath;
        }

        public void selectorCanvasColor(int color) {
            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
            mSaveBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            mSaveCanvas = new Canvas(mSaveBitmap);
            mSaveCanvas.drawColor(color);

            Iterator<DrawPath> iter = savePath.iterator();
            while (iter.hasNext()) {
                DrawPath drawPath = iter.next();
                if (drawPath.paint.getColor() == backgroundColor) {
                    drawPath.paint.setColor(color);
                }
                mCanvas.drawPath(drawPath.path, drawPath.paint);
                mSaveCanvas.drawPath(drawPath.path, drawPath.paint);
            }
            invalidate();// 刷新

            backgroundColor = color;
            setBackgroundColor(color);
        }

        //选择画笔大小
        public void selectPaintSize(int which) {
            currentSize = which;
            setPaintStyle();
            mPaint.setStrokeWidth(which);

        }


        //设置画笔颜色
        public void selectPaintColor(int which) {
            currentStyle = 1;
            currentColor = which;
            setPaintStyle();

        }

        //设置橡皮擦
        public void selectEraser() {
            currentStyle = 0;
            setPaintStyle();
            //        mPaint.setColor(Color.TRANSPARENT);
            //        mPaint.setAlpha(0);
            //        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

    }
}