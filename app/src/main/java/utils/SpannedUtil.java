package utils;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-26 17:12
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class SpannedUtil {
    public static void spannedUtil(Spanned spanned){
        if (spanned instanceof SpannableStringBuilder){
            ImageSpan[] imageSpen = spanned.getSpans(0,spanned.length(),ImageSpan.class);
            for (ImageSpan span : imageSpen) {
                int start = spanned.getSpanStart(span);
                int end = spanned.getSpanEnd(span);
                Drawable d = span.getDrawable();
                VerticalImageSpan verticalImageSpan = new VerticalImageSpan(d);
                ((SpannableStringBuilder) spanned).setSpan(verticalImageSpan,start,end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                ((SpannableStringBuilder) spanned).removeSpan(imageSpen);
            }
        }
    }

}
